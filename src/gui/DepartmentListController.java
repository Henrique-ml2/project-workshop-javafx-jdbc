package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML	
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	// - (ActionEvent event): a partir dele ter condição de acessar o Stage de onde está esse botão
	@FXML
	public void onBtNewAction(ActionEvent event) {
		
		// Seleciona o Stage atual, onde o botão "New" fica
		Stage parentStage = Utils.currentStage(event);
		
		// Cria a janela por cima
		createDailogForm("/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}

	// Abrir a View DepartmentForm sobre outra
	// - (Stage parentStage): informar qual Stage que criou essa outra janela na frente, nesse caso a janela de diálogo
	
	// E chamar essa função na função onBtNewAction(), para quando o botão "New" for clicado, aparecer a View DepartmentForm na frente
	private void createDailogForm(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			// Para carregar uma janela modal na frente da janela existente, tem-se que instanciar outro Stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			
			// Novo Stage, também tem que ter uma nova Scene
			dialogStage.setScene(new Scene(pane));
			
			// Diz se a janela pode ou não ser redimensionada
			dialogStage.setResizable(false);
			
			// Quem é o Stage pai dessa janela (a janela que fez a DepartmentForm ser aberta)
			dialogStage.initOwner(parentStage);
			
			// Se a janela vai ser modal ou ter outro comportamento
			// - Modality.WINDOW_MODAL: a janela fica travada - enquanto essa janela não ser fechada, não poderá acessar a janela anterior
			dialogStage.initModality(Modality.WINDOW_MODAL);
			
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
