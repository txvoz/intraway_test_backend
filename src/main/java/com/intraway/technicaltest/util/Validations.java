package com.intraway.technicaltest.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import com.intraway.technicaltest.exception.ClientException;

/**
 * 
 * @author gustavo.rodriguez
 *
 */
public class Validations {

	private static final String EMAIL_PATTERN = "^[^@]+@[^@]+\\.[a-zA-Z]{1,}$";

	private static final String TAXID_PATTERN = "^([0-9]){10}-[0-9]{1}$";

	private static final String CODE_VERIFICATION_PATTERN = "^[0-9]{6}$";

	private static final String CELLPHONE_PATTERN = "(300|301|302|304|305|303|304|305|310|311|312|313|314|320|321|322|323|315|316|317|318|319|350|351)[0-9]{7}";

	private static boolean validatePattern(String textValidate, String strPattern) {
		Pattern pattern = Pattern.compile(strPattern);
		Matcher mather = pattern.matcher(textValidate);
		if (mather.find()) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo para validar formato de correo electronico
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email.length() >= 5 && email.length() <= 100) {
			return validatePattern(email, EMAIL_PATTERN);
		}
		return false;
	}

	/**
	 * Metodo para validar formato de taxid / NIT
	 * 
	 * @param taxid
	 * @return
	 */
	public static boolean isTaxId(String taxid) {
		return validatePattern(taxid, TAXID_PATTERN);
	}

	/**
	 * Metodo para validar formato de password Minimo una mayuscula, una minuscula,
	 * un numero, longitud de 8 a 20 caracteres
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isValidPassword(String password, boolean validateSpecialCharacter) {
		if (password.length() >= 8 && password.length() <= 50) {
			boolean uppercase = false;
			boolean lowercase = false;
			boolean number = false;
			boolean specialCharacter = !validateSpecialCharacter;
			for (int i = 0; i < password.length(); i++) {
				if (password.charAt(i) >= 65 && password.charAt(i) <= 90) {
					uppercase = true;
				} else if (password.charAt(i) >= 97 && password.charAt(i) <= 122) {
					lowercase = true;
				} else if (password.charAt(i) >= 48 && password.charAt(i) <= 57) {
					number = true;
				} else {
					specialCharacter = true;
				}
				
				if(lowercase && uppercase && number && specialCharacter) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metodo para validar un codigo de verificacion de 6 digitos
	 * 
	 * @param code
	 * @return
	 */
	public static boolean isValidCodeVerification(String code) {
		return validatePattern(code, CODE_VERIFICATION_PATTERN);
	}

	/**
	 * Metodo para validar un telefono de celular con operadores validos en colombia
	 * 
	 * @param cellNumber
	 * @return
	 */
	public static boolean isValidCellPhone(String cellNumber) {
		return Pattern.matches(CELLPHONE_PATTERN, cellNumber.replaceAll("\\s", "").replace("-", ""));
	}

	public static boolean isEmpty(String text) {
		return (text == null || text.trim().isEmpty());
	}

	public static void validateTaxId(String taxId, String message) {
		if (!isTaxId(taxId)) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateCodeVerification(String code, String message) {
		if (!isValidCodeVerification(code)) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateCellPhone(String cellPhone, String message) {
		if (!isValidCellPhone(cellPhone)) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateEmail(String email, String message) {
		if (!isEmail(email)) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validatePassword(String password, String message) {
		if (!isValidPassword(password, false)) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}
	
	public static void validatePasswordWithSpecialCharacters(String password, String message) {
		if (!isValidPassword(password, true)) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateText(String text, String message) {
		if (isEmpty(text)) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateInteger(Integer number, String message) {
		if (number == null) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateLong(Long number, String message) {
		if (number == null) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateDouble(Double number, String message) {
		if (number == null) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}

	public static void validateDate(Date date, String message) {
		if (date == null) {
			throw new ClientException(message, HttpStatus.BAD_REQUEST);
		}
	}
}
