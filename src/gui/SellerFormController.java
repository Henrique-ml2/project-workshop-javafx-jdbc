package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
	
	// Dependência
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
	
	// Adicionar uma ComboBox para selecionar um Department an View SellerForm
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
	
	// Criar uma lista ObservableList para adicioná-la no <ComboBox> comboBoxDepartment
	private ObservableList<Department> obsList;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	// Agora o método faz duas injeções de dependência por inversão de controle
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
		
		// Verifica se a entidade para preencher os campos da View SellerForm está vazio, significando que é um novo Seller
		if (entity.getDepartment() == null) {
			
			// - getSelectionModel(): seleciona as opções da <ComboBox> comboBoxDepartment
			// - selectFirst(): já preenche a <ComboBox> comboBoxDepartment com o primeiro Department da lista (já que é um novo Seller)
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}
	
	// Método responsável por:
	// 1) chamar o objeto de dependência "departmentService" selecionando todos os Departments do banco de dados
	// 2) carregando esses Departments em uma lista
	// 3) preenchendo a lista ObservableList "obsList" com essa lista com os Departments
	// 4) definir essa "obsList" como a lista associada à <ComboBox> comboBoxDepartment
	public void loadAssociatedObjects() {
		
		// Programação defensiva caso o programador se esqueça de fazer a injeção do DepartmentService service
		// Faz-se essa Programação defensiva pois as Injeções de Dependência estão sendo feitas manualmente e não com um Framework e et
		if (departmentService == null ) {
			throw new IllegalStateException("DepartmentService was null");
		}
		
		// 1) e 2)
		List<Department> list = departmentService.findAll();
		
		// 3)
		obsList = FXCollections.observableArrayList(list);
		
		// 4) 
		comboBoxDepartment.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
	
	/* Método implementado do PDF das aulas, o mesmo do capítulo de Interface Gráfica */

	// Método responsável por inicializar a <ComboBox> comboBoxDepartment da View SellerForm
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
