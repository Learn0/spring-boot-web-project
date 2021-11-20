package shop.ourshopping.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

// 유효성 체크
public class ValidUtils {

	public static Map<String, String> validateHandling(Errors errors) {
		Map<String, String> validatorResult = new HashMap<String, String>();
		for (FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			if (validatorResult.containsKey(validKeyName)) {
				if (!error.getCode().equals("NotBlank")) {
					continue;
				}
			}
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}

		return validatorResult;
	}
}