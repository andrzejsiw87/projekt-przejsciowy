package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class MainController {

    @FXML private TextField autorTextField;
    @FXML private TextField szerokoscTextField;
    @FXML private TextField obrazTextField;
    @FXML private TextField wysokoscTextField;
    @FXML private TextField wagaTextField;

    @FXML private TableView<Masterpiece> employeesTable;

    private File openedFile;

    // metoda która jest automatycznie wywoływana przez JavaFX - w niej możemy np. ustawiać różne listenery
    public void initialize() {

        // dodajemy listener zaznaczenia rzędu w tabeli
        employeesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Masterpiece>() {
            @Override
            public void changed(ObservableValue<? extends Masterpiece> observable, Masterpiece oldValue, Masterpiece newValue) {
                if (newValue != null) {
                    MainController.this.onEmployeesRowSelect(newValue);
                }
            }
        });
    }

    // handler wciśnięcia przycisku 'Wczytaj' ustawiony w pliku fxml
    @FXML
    public void handleWczytaj(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otwórz plik z bazą pracowników");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Masterpiece database file", "db"));
        openedFile = fileChooser.showOpenDialog(employeesTable.getScene().getWindow());

        // ładujemy dane pracowników z pliku z użyciem osobnej klasy z metodą statyczną
        ObservableList<Masterpiece> masterpieces = FileDatabaseManager.loadEmployeesFromFile(openedFile);
        // ustawiamy załadowaną listę pracowników w TableView - mapowanie pól modelu do odpowiednich kolumn
        // zostało skonfigurowane w pliku fxml
        employeesTable.setItems(masterpieces);
        // żeby po wczytaniu nowych danych stare nie zostawały w polach tekstowych
        clearTextFields();
    }

    @FXML
    public void handleZapisz(ActionEvent actionEvent) {
        // pobieramy aktualnie wyświetlaną listę pracowników i zapisujemy ją w zapamiętanym wcześniej pliku
        FileDatabaseManager.saveEmployeesToFile(employeesTable.getItems(), openedFile);
    }

    @FXML
    public void handleDodaj(ActionEvent actionEvent) {
        // w osobnej metodzie tworzymy nowy obiekt Masterpiece z wartości wpisanych aktualnie w pola tekstowe
        Masterpiece newMasterpiece = getEmployeeFromTextFields();
        // jeśli nowy obiekt nie jest nullem - dodajemy go do modelu tabeli
        if (newMasterpiece != null) {
            employeesTable.getItems().add(newMasterpiece);
        }
    }



    @FXML
    public void handleRaport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz raport w pliku");
        fileChooser.setInitialFileName(createReportFileName());
        File reportFile = fileChooser.showSaveDialog(employeesTable.getScene().getWindow());

        FileDatabaseManager.saveEmployeesReport(employeesTable.getItems(), reportFile);
    }

    // tworzymy nazwę pliku zawierającą datę, żeby była w miarę unikalna
    private String createReportFileName() {
        String dateString = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        return "raport-" + dateString + ".db";
    }

    private Masterpiece getEmployeeFromTextFields() {
        // w osobnej metodzie sprawdzamy czy któreś z pól tekstowych jest puste
        if (anyOfFieldsAreEmpty()) {
            // jeśli tak - zwracamy null - nie chcemy dodawać niepełnego rekordu pracownika
            return null;
        }

        return new Masterpiece(
                autorTextField.getText().trim(),
                obrazTextField.getText().trim(),
                Integer.parseInt(wagaTextField.getText().trim()),
                Integer.parseInt(szerokoscTextField.getText().trim()),
                Integer.parseInt(wysokoscTextField.getText().trim())
        );
    }

    // zwraca true jesli ktorekolwiek z pol tekstowych jest puste lub zawiera tylko biale znaki (np. spacje)
    private boolean anyOfFieldsAreEmpty() {
        return autorTextField.getText().trim().isEmpty()
                || obrazTextField.getText().trim().isEmpty()
                || wagaTextField.getText().trim().isEmpty()
                || szerokoscTextField.getText().trim().isEmpty()
                || wysokoscTextField.getText().trim().isEmpty();
    }

    // funkcja wywoływana w listenerze zaznaczenia rzędu w tabeli - ustawiamy wartości pól tekstowych
    private void onEmployeesRowSelect(Masterpiece selectedMasterpiece) {
        autorTextField.setText(selectedMasterpiece.getAuthor());
        obrazTextField.setText(selectedMasterpiece.getPicture());
        wagaTextField.setText(String.valueOf(selectedMasterpiece.getWeight()));
        szerokoscTextField.setText(String.valueOf(selectedMasterpiece.getWidth()));
        wysokoscTextField.setText(String.valueOf(selectedMasterpiece.getHeight()));
    }

    private void clearTextFields() {
        autorTextField.clear();
        obrazTextField.clear();
        wagaTextField.clear();
        szerokoscTextField.clear();
        wysokoscTextField.clear();
    }

    public void sortByAuthor(ActionEvent actionEvent) {
        employeesTable.getItems().sort(Comparator.comparing(Masterpiece::getAuthor));
    }

    public void sortByWeight(ActionEvent actionEvent) {
        employeesTable.getItems().sort(Comparator.comparing(Masterpiece::getWeight));
    }

    public void sortByArea(ActionEvent actionEvent) {
        employeesTable.getItems().sort(Comparator.comparing(Masterpiece::getArea));
    }
}
