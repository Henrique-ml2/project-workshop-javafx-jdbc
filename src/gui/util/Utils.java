package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	// Acessar o Stage onde o Controller que recebeu o evento, de click por exemplo, está
	// Assim sabendo desse Stage, poderá ser colocada por cima dessa View, a View <DepartmentForm>
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
