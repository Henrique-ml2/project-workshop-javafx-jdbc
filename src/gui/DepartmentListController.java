package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{

	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML	
	private TableColumn<Department, Integer> tableColumnId; // <tipo da <TableView> que a coluna está inserida, tipo da coluna>
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}


	private void initializeNodes() {
		
		// Padrão do JavaFX para iniciar o comportamento das colunas
		// - "id"-"name": nome das colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// ------ FAZER A <TableView> ACOMPANHAR O TAMANHO DA JANELA ------//
		
		// Referenciar o Stage
		// - (Stage): o tipo Stage é uma subclasse de Window, então ocorre o DOWNCASTING
		// - getWindow(): referencia a janela, e depois referencia ao Stage pelo DOWNCASTING 
		Stage stage = (Stage) Main.getMainScene().getWindow();
		
		// Macete para a <TableView> acompanhar a altura da janela (e também redimensiona)
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}

}
