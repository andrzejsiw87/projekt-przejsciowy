package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ByeController {

    public Button cancelButton;
    public Button closeButton;
    public Label goodbyeText;

    public void backPressed(ActionEvent actionEvent) {
        Main.showPane(1); // wracamy do głównego
    }

    public void closePressed(ActionEvent actionEvent) {
        // zamykamy okno tak, żeby nie odpalić handlera
        // nie wiem dlaczego to tak działa - z jakiegoś powodu zamknięcie okna w ten sposób nie wyzwala eventu,
        // którego nasłuchujemy w Main w setOnCloseRequest()
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
