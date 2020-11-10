package gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/* Método implementado do PDF das aulas, pois é... */
	/* Inspirado em um tópico do StackOverflo, link no PDF das aulas */

	// Método responsável por formatar as datas em colunas
	// E que nesse caso será usado para formatar as datas da coluna <TableColumn> tableColumnBirthDate da listinha <TableView> de Sellers da classe SellerListController
	// - TableColumn<T, Date> tableColumn: nome da coluna <TableColumn> da listinha <TableView>
	// - String format: formato de como serão as datas. Ex: dd/MM/yyy
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}

	/* Método implementado do PDF das aulas, pois é... */

	// Método responsável por formatar valores de pontos flutuantes
	// E que nesse caso será usado para formatar os valores de pontos flutuantes da coluna <TableColumn> tableColumnBaseSalary da listinha <TableView> de Sellers da classe SellerListController
	// - TableColumn<T, Double> tableColumn: nome da coluna 
	// - int decimalPlaces: quantas casas decimais esse valor terá
	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
}
