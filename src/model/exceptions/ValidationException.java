// Exceção para validar um formulário
// Irá carregar as mensagens de erro caso existam

package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// Serve para guardar quais são os erros dos campos <TextField> Id e Name da View DepartmentForm
	private Map<String, String> errors = new HashMap<>(); // <nome do campo, mensagem de erro>
	
	// Forçar insntanciação da exceção com um construtor
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}
	
	// Método que permitirá adicionar um elemento na coleção "errors"
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
