package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable{

	private Seller entity;
	
	private SellerService service;
	
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	private ObservableList<Department> obsList;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
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
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		
		catch (ValidationException e) {
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
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtBaseSalary);
	
		initializeComboBoxDepartment();
	}
	
	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty!*");
		}
		obj.setName(txtName.getText());
		
		// O campo <TextField> Email não pode ser vazio
		// Então verificar se esse campo <TextField> Email não está vazio
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			
			// Adiciona um erro, um objeto à coleção Map<> errors do objeto "exception"
			exception.addError("email", "Field can't be empty!*");
		}
		obj.setEmail(txtEmail.getText());
		
		// O campo <DatePicker> Birth Date não pode ser vazio
		// Então verificar se esse campo <TextField> Email não está vazio
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			
			// Adiciona um erro, um objeto à coleção Map<> errors do objeto "exception"
			exception.addError("birthDate", "Field can't be empty!*");
		}
		else {
			
			// Pegar o valor da data de nascimento do campo <DatePicker> Birth Date  e definir no objeto caso esse campo não estiver vazio
			// - Instant instant: independe de horários de localidade
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		
		// O campo <TextField> Base Salary não pode ser vazio
		// Então verificar se esse campo <TextField> BaseSalary não está vazio
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			
			// Adiciona um erro, um objeto à coleção Map<> errors do objeto "exception"
			exception.addError("baseSalary", "Field can't be empty!*");
		}
		
		// - tryParseToDouble(): método que tentará converter o campo do <TextField> para Double, e se não conseguir retornará apenas NULL
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

		// O Department selecionado na ComboBox
		obj.setDepartment(comboBoxDepartment.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		Locale.setDefault(Locale.US);
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}
	
	public void loadAssociatedObjects() {
		if (departmentService == null ) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		// Troca dos if-else's por Operador ternário, verificando se há erros no Map<> errors
		// Caso houver, escrever a mensagem de erro correspondente do campo na View SellerForm
		// Limpar a mensagem de erro caso o usuário agora tenha escrito algo no campo 
		labelErrorName.setText(fields.contains("name") ? errors.get("name") : "");
		labelErrorEmail.setText(fields.contains("email") ? errors.get("email") : "");
		labelErrorBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");
		labelErrorBaseSalary.setText(fields.contains("baseSalary") ? errors.get("baseSalary") : "");
	}
	
	private void initializeComboBoxDepartment() {
		 Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
		
			 @Override
			 protected void updateItem(Department item, boolean empty) {
				 super.updateItem(item, empty);
				 setText(empty ? "" : item.getName());
			 }
		 };
		 comboBoxDepartment.setCellFactory(factory);
		 comboBoxDepartment.setButtonCell(factory.call(null));
		} 
}
