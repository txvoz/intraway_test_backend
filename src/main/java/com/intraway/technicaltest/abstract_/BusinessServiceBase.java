package com.intraway.technicaltest.abstract_;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.intraway.technicaltest.exception.ClientException;
import com.intraway.technicaltest.exception.IntawayException;
import com.intraway.technicaltest.exception.ServerException;
import com.intraway.technicaltest.util.Validations;


/**
 *
 * @author Gustavo Rodriguez
 * @param <E>
 * @param <ID>
 */
@Service
public abstract class BusinessServiceBase<E, ID extends Serializable> {

    private static final Logger LOG = Logger.getLogger(BusinessServiceBase.class.getName());

    @PersistenceContext
    private EntityManager em;

    /**
     * Return class facade to data access
     *
     * @return
     */
    protected abstract MDao<E, ID> getDao();

    /**
     * Reference to id entity
     *
     * @param entity
     * @return
     */
    protected EntityManager getEntityManager() {
        return em;
    }

    protected ID getId(E entity) {
        Class<? extends Object> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();

            for (Annotation fieldAnnotation : fieldAnnotations) {
                if (fieldAnnotation instanceof javax.persistence.Id) {
                    try {
                        return runGetter(field, entity);
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
        return null;
    }

    protected ID runGetter(Field field, E entity) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        String fieldName = field.getName();
        Method[] methods = entity.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().toLowerCase().equals("get" + fieldName)) {
                return (ID) method.invoke(entity);
            }
        }

        return null;
    }

    /**
     * Return reference to empty object
     *
     * @return
     */
    protected E getEmptyObject() {
        try {
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class<E> eClazz = (Class<E>) type.getActualTypeArguments()[0];

            return eClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

    /**
     * return collection (List) Entity
     *
     * @return
     */
    public List<E> findAll() {
        return getDao().findAll();
    }

    /*public Page<E> findAll(int pageNumber, int size) {
    	Short s = new Short("0");
        PageRequest page = new PageRequest(pageNumber, size);
        return getDao().findAll(page);
    }*/

    /**
     * Return one Entity by id
     *
     * @param primaryKey
     * @return
     */
    public E findOne(ID primaryKey) {
        if (primaryKey != null && getDao().existsById(primaryKey)) {
            return getOne(primaryKey);
        }

        return null;
    }

    public E getOne(ID primaryKey) {
        E obj = getDao().getOne(primaryKey);

        return obj;
    }

    /**
     * Save or edit a object
     *
     * @param newE
     * @return
     */
    public E save(E newE) {
        try {
            return getDao().save(newE);
        } catch (ConstraintViolationException ex) {

            throw new ServerException("Error al realizar el registro!", ex);

//            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
//            StringBuilder sb = new StringBuilder();
//
//            for (ConstraintViolation<?> violation : violations) {
//                sb.append(violation.getMessage() + "\n");
//            }
//
//            throw new MuverangException(sb.toString());
        }

    }

    /**
     * Delete physically one object
     *
     * @param toDeleteE
     */
    public void delete(E toDeleteE) throws IntawayException {
        getDao().delete(toDeleteE);
    }

    public void active(E toActive) throws IntawayException {

    }

    public void edit(E toEditE) {
    	validateCreateAndEdit(toEditE, true);
        try {
            getDao().save(fillSavedObject(toEditE));
        } catch (Exception ex) {
            throw new ServerException("Inconvenientes actualizando registro!", ex);
        }
    }

    public Page<E> getWithFilters(Map<String, String[]> filterValues, Pageable pageable) {

        return getTokenSearch(filterValues, pageable, false);
    }
    
    protected boolean isDisctinctRowsByGetTokenSearch() {
    	return false;
    }

    public Page<E> getWithFiltersForFinder(Map<String, String[]> filterValues, Pageable pageable) {
        return getWithFilters(filterValues, pageable);
    }

    protected Page<E> getTokenSearch(Map<String, String[]> filterValues, Pageable pageable, boolean isFinder) {
        EntityManager em = getEntityManager();
       
        CriteriaBuilder cb = em.getCriteriaBuilder();
        E entity = getEmptyObject();
        Class<E> clazz = (Class<E>) entity.getClass();
        CriteriaQuery<E> cq = cb.createQuery(clazz);
        cq.distinct(isDisctinctRowsByGetTokenSearch());
        Root<E> root = cq.from(clazz);

        List<Predicate> wherePredicates = getWherePredicates(filterValues, clazz, cb, root, isFinder,false);
        wherePredicates.addAll(getPostConditions(cb, root, filterValues));

        if (isFinder) {
            wherePredicates.addAll(getFinderPostConditions(cb, root, filterValues));
        }

        cq.where(wherePredicates.toArray(new Predicate[0]));
        List<javax.persistence.criteria.Order> orderBy = new LinkedList<>();
        addOrderBy(cb, orderBy, root);
        addOrderByWithFilters(cb, orderBy, root, filterValues);                
        if (!orderBy.isEmpty()) {
            cq.orderBy(orderBy);
        }

        TypedQuery<E> query = em.createQuery(cq);

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<E> countEntity = countQuery.from(clazz);

        countQuery.distinct(isDisctinctRowsByGetTokenSearch());
        countQuery.select(cb.count(countEntity));
        List<Predicate> countWhere = getWherePredicates(filterValues, clazz, cb, countEntity, isFinder, true);
        countWhere.addAll(getPostConditions(cb, root, filterValues));

        if (isFinder) {
            countWhere.addAll(getFinderPostConditions(cb, root, filterValues));
        }
        countQuery.where(countWhere.toArray(new Predicate[0]));
        Long count = em.createQuery(countQuery).getSingleResult();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, count);
    }

    protected List<Predicate> getWherePredicates(Map<String, String[]> filterValues, Class<E> clazz, CriteriaBuilder cb,
            Root<E> root, boolean isFinder, boolean count)
            throws SecurityException {

        String filter = getDefaultFilter(filterValues);
        String[] tokens = StringUtils.split(filter, " ");
        if (tokens == null) {
            tokens = new String[1];
            tokens[0] = filter;
        }

        List<Predicate> andPredicates = new LinkedList<>();
        andPredicates.add(cb.equal(cb.literal(1), 1));

        for (String token : tokens) {
            List<Predicate> orPredicates = new LinkedList<>();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(javax.persistence.Column.class)) {
                    String canonical = field.getType().getCanonicalName();

                    if (isJavaNumber(canonical) && isNumber(token)) {
                        orPredicates.add(cb.equal(root.get(field.getName()), token));
                    } else if (canonical.equals("java.lang.String")) {
                        orPredicates.add(cb.like(cb.upper(root.get(field.getName())),
                                "%" + token.toUpperCase() + "%"));
                    }
                }
            }

            orPredicates.addAll(getConditions(cb, root, token));

            if (isFinder) {
                orPredicates.addAll(getFinderConditions(cb, root, token));
            }

            andPredicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
        }

        return andPredicates;
    }

    protected List<Predicate> getConditions(CriteriaBuilder cb, Root<E> root, String token) {
        return new LinkedList<>();
    }

    protected List<Predicate> getPostConditions(CriteriaBuilder cb, Root<E> root, Map<String, String[]> filterValues) {
        return new LinkedList<>();
    }

    protected void addOrderBy(CriteriaBuilder cb, List<javax.persistence.criteria.Order> orderBy, Root<E> root) {

    }
    
    protected void addOrderByWithFilters(CriteriaBuilder cb, List<javax.persistence.criteria.Order> orderBy, Root<E> root,Map<String, String[]> filterValues) {

    }

    public boolean isNumber(String token) {
        try {
            Double number = Double.parseDouble(token);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    public boolean isJavaNumber(String canonical) {
        return canonical.equals("java.lang.Integer")
                || canonical.equals("java.lang.Long")
                || canonical.equals("java.lang.Double")
                || canonical.equals("java.lang.Float")
                || canonical.equals("java.lang.Number")
                || canonical.equals("int")
                || canonical.equals("long")
                || canonical.equals("float")
                || canonical.equals("double");
    }

    public void validateCreateAndEdit(E entity, boolean isEdit) throws ClientException {

    }

    public E create(E entity) {
        validateCreateAndEdit(entity, false);
        return save(entity);
    }

    public E create(E entity, Object[] details) throws IntawayException {
        return null;
    }

    public E edit(E entity, Object[] details) throws IntawayException {
        return null;
    }

    public E fillSavedObject(E toEdit) {
        E saved = findOne(getId(toEdit));

        try {
            Class entClazz = toEdit.getClass();

            Method[] entMethods = entClazz.getMethods();

            for (Method entMethod : entMethods) {
                if (entMethod.getName().startsWith("get") && (entMethod.invoke(toEdit) != null)) {
                    StringBuilder setMet = new StringBuilder(entMethod.getName());
                    setMet.setCharAt(0, 's');

                    for (Method savMethod : entMethods) {
                        if (savMethod.getName().equals(setMet.toString())) {
                            savMethod.invoke(saved, entMethod.invoke(toEdit));
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return saved;
    }

    public Long count() {
        return getDao().count();
    }

    public boolean isEmpty(String text) {
        return Validations.isEmpty(text);
    }

    public void validateText(String text, String message) {
    	Validations.validateText(text,message);
    }

    public void validateInteger(Integer number, String message) {
    	Validations.validateInteger( number,  message);
    }

    public void validateLong(Long number, String message) {
    	Validations.validateLong( number,  message);
    }

    public void validateDouble(Double number, String message) {
    	Validations.validateDouble( number,  message);
    }

    public void validateDate(Date date, String message) {
    	Validations.validateDate(date, message);
    }

    public void validateEntity(E entity, String messageNull, String messageEmpty)
            throws IntawayException {
        if (entity == null) {
            throw new ClientException(messageNull, HttpStatus.BAD_REQUEST);
        }

        if (findOne(entity) == null) {
            throw new ClientException(messageEmpty, HttpStatus.BAD_REQUEST);
        }

    }

    public void exist(ID primaryKey, String message) {
        E pivot = findOne(primaryKey);

        if (pivot != null) {
            throw new ClientException(message);
        }
    }

    public String getDefaultFilter(Map<String, String[]> filterValues) {
        if (filterValues == null) {
            return "";
        }

        return (filterValues.get("filter") == null) ? "" : filterValues.get("filter")[0];
    }

    public E findOne(E entity) {
        return findOne(getId(entity));
    }

    public List<Predicate> getFinderPostConditions(CriteriaBuilder cb, Root<E> root, Map<String, String[]> filterValues) {
        return new LinkedList<>();
    }

    public List<Predicate> getFinderConditions(CriteriaBuilder cb, Root<E> root, String token) {
        return new LinkedList<>();
    }

}
