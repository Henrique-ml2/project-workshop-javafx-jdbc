package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		System.out.println("onMenuItemDepartmentAction");
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	// "absoluteName" pois é o caminho completo dos arquivos
	// synchronized: para garantir que todo o processamento ocorra sem ser interrompido durante o multi-threading
	private synchronized void loadView(String absoluteName) {
		try {
			
			// Para carregar uma tela usa-se um objeto tipo FXMLLoader
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			// Todas as outras Views serão apenas uma nova <VBox> para sobrepor a MainView
			VBox newVBox = loader.load();
			
			// ---------- MUDAR PARA A VIEW DO ABOUT ---------- //
			
			// Aqui temos a Scene inteira
			// - getMainScene(): método criado para referenciar a Scene criada na MainView
			Scene mainScene = Main.getMainScene(); 

			// 1) Referenciar a <VBox> principal
			// - getRoot(): pegar o primeiro elemento da View	
			// - (ScrollPane): para o compilador entender que estou pegando MESMO o <ScrollPane>
			// - getContent(): pegar o <content> do <ScrollPane>
			// - (VBox): para o compilador entender ao selecionar o <content>, selecionar o <VBox>
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); 
			
			// Guardar a barra de Menu <MenuBar> em um lugar para adicioná-lo de novo depois
			// - getChildren(): para selecionar o <children>
			// - get(0): para selecionar o <MenuBar> (primeira tag)
			Node mainMenu = mainVBox.getChildren().get(0);
			
			// 2) Exclui todo(s) o(s) <children> da <VBox> principal
			mainVBox.getChildren().clear();
			
			// 3) Adicionar a barra de Menu <MenuBar>
			mainVBox.getChildren().add(mainMenu);
			
			// 4) Adicionar todo(s) o(s) <children> da <VBox> da View do About para o <VBox> principal
			 
			// Como só será trocado o(s) <children> a <VBox> principal, as outras telas começarão com <VBox> em seu código e não <ScrollPane>
			// Por isso simplesmente "newBox.getChildren() diferente do passo 1), que percorreu tags até chegar na <VBox> principal "
			mainVBox.getChildren().addAll(newVBox.getChildren());

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
