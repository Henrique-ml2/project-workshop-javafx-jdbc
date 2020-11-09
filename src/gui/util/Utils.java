package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	// Método que auxiliara a passar o String do <Textfiel> para Integer
	public static Integer tryParseToInt(String str) {
		
		// try-catch: se a String "str" for um valor diferente de um valor Integer, o método retornará apenas NULL
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
}
