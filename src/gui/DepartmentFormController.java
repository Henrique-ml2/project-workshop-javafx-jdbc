package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{

	private Department entity;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	public void setDeparment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	@FXML 
	public void OnBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			
			// Agora esse método getFormData() pode lançar uma exceção
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		
		// Pegar a exceção lançada pelo método getFormData() caso exista
		catch (ValidationException e) {
			
			// ------  DEFINIR <Label> labelErroName COM MENSAGEM DE ERRO  ------  //
			
			// O que acontece:
			// 1) chama-se o método getFormData() para retornar os dados dos campos <TetField> Id e Name
			// 2) se o campo <TextField> Name estiver vazio, o objeto de exceção "exception" adicionará um objeto de erro à seu Map<> errors
			// 3) no método getFormData(), caso exista uma key com valor "name" no objeto de exceção "exception" o método lançará uma exceção/objeto de exceção "exception"
			// 4) o objeto de exeção "e" capturado aponta para o objeto de exceção "exception" lançado pelo método getFormData()
			// 5) exceção capturada - e.getErrors(): retorna a coleção de Map<> errors do objeto "e"
			// 6) - setErrorMessages(): define o <Label> labelErrorMessage com a String (mensagem de erro) que tiver na key "name" do Map<> errors do objeto de exceção "e"
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML 
	public void OnBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	private Department getFormData() {
		Department obj = new Department();

		// Será criado um objeto de exceção vazio "exception" sempre que o getFormData() for chamado para retornar os dados dos campos <TextField> Id e Name
		// Assim, o Map<> errors do objeto "exception" sempre começará vazio, adicionando um objeto de erro posteriormente caso exista (campo <TextField> Name for vazio)
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		// O campo <TextField> Name não pode ser vazio
		// Então verificar se esse campo <TextField> Name não está vazio
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			
			// Adiciona um erro, um objeto à coleão Map<> errors do objeto "exception"
			exception.addError("name", "Field can't be empty!*");
		}
		
		// Por enquanto o campo <TextField> Name ainda está vazio pois no método onBtNewAction() da classe DepartmentListController, a instanciação do Department está vazia
		obj.setName(txtName.getText());
		
		// Verificar se na coleção de "errors" do objeto "exception" há pelo menos um erro
		if (exception.getErrors().size() > 0) {
			
			// E se houver, lançar a exceção/objeto de exceção
			throw exception;
		}
		
		return obj;
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	
	// Método responsável por preencher as mensagens de erro no <Label> labelErrorName (vazio) da View DepartmentForm
	private void setErrorMessages(Map<String, String> errors) {
		
		// Os nomes dos campos serão armazenados nessa coleção "fields" para verificar se existe uma key específica na coleção do Map<> "errors"
		Set<String> fields = errors.keySet();
		
		// Testar se nessa coleção "fields" existe a key "name"
		if (fields.contains("name")) {
			
			// Caso exista, definir o <Label> labelErrorName com o valor da key "name" 
			labelErrorName.setText(errors.get("name"));
		}
	}
}
