package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class MainController {

    @FXML private TextField autorFilterTextField;
    @FXML private TextField autorTextField;
    @FXML private TextField szerokoscTextField;
    @FXML private TextField obrazTextField;
    @FXML private TextField wysokoscTextField;
    @FXML private TextField wagaTextField;

    @FXML private TableView<Masterpiece> masterpiecesTable;

    private File openedFile;
    private ObservableList<Masterpiece> masterpieces = FXCollections.observableArrayList();

    // metoda która jest automatycznie wywoływana przez JavaFX - w niej możemy np. ustawiać różne listenery
    public void initialize() {

        // dodajemy listener zaznaczenia rzędu w tabeli
        masterpiecesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Masterpiece>() {
            @Override
            public void changed(ObservableValue<? extends Masterpiece> observable, Masterpiece oldValue, Masterpiece newValue) {
                if (observable != null) {
                    MainController.this.onEmployeesRowSelect(observable);
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
        openedFile = fileChooser.showOpenDialog(masterpiecesTable.getScene().getWindow());

        // czyscimy dane z poporzedniego pliku (jesli sa)
        masterpieces.clear();
        // ładujemy dane pracowników z pliku z użyciem osobnej klasy z metodą statyczną
        masterpieces.addAll(FileDatabaseManager.loadEmployeesFromFile(openedFile));

        // opakowujemy liste w liste filtrowalna
        FilteredList<Masterpiece> filteredData = new FilteredList<>(masterpieces, m -> true);
        // opakowujemy liste w liste sortowalna
        SortedList<Masterpiece> sortedData = new SortedList<>(filteredData);
        // ustawiamy opakowana liste jako zrodlo danych w tableview
        masterpiecesTable.setItems(sortedData);

        // dodajemy listener zmiany wartosci pola filtrujacego
        autorFilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(masterpiece -> { // wykonywane dla kazdego elementu listy

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // sprowadzamy wartosc filtra do malych liter, zeby porownac bez wzgledu na wielkosc liter
                String lowerCaseFilter = newValue.toLowerCase();
                // sprowadzamy nazwisko autora do malych liter
                String author = masterpiece.getAuthor().toLowerCase();
                if (author.startsWith(lowerCaseFilter)) {
                    return true; // znaleziono dopasowanie
                }
                return false;
            });
        });


        // żeby po wczytaniu nowych danych stare nie zostawały w polach tekstowych
        clearTextFields();
    }

    @FXML
    public void handleZapisz(ActionEvent actionEvent) {
        // pobieramy aktualnie wyświetlaną listę pracowników i zapisujemy ją w zapamiętanym wcześniej pliku
        FileDatabaseManager.saveEmployeesToFile(masterpiecesTable.getItems(), openedFile);
    }

    @FXML
    public void handleDodaj(ActionEvent actionEvent) {
        // w osobnej metodzie tworzymy nowy obiekt Masterpiece z wartości wpisanych aktualnie w pola tekstowe
        Masterpiece newMasterpiece = getEmployeeFromTextFields();
        // jeśli nowy obiekt nie jest nullem - dodajemy go do modelu tabeli
        if (newMasterpiece != null) {
            masterpiecesTable.getItems().add(newMasterpiece);
        }
    }



    @FXML
    public void handleRaport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz raport w pliku");
        fileChooser.setInitialFileName(createReportFileName());
        File reportFile = fileChooser.showSaveDialog(masterpiecesTable.getScene().getWindow());

        FileDatabaseManager.saveEmployeesReport(masterpiecesTable.getItems(), reportFile);
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
    private void onEmployeesRowSelect(ObservableValue<? extends Masterpiece> observable) {

        Masterpiece selectedMasterpiece = observable.getValue();

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
        SortedList<Masterpiece> items = (SortedList<Masterpiece>) masterpiecesTable.getItems();
        items.setComparator(Comparator.comparing(Masterpiece::getAuthor));
    }

    public void sortByWeight(ActionEvent actionEvent) {
        SortedList<Masterpiece> items = (SortedList<Masterpiece>) masterpiecesTable.getItems();
        items.setComparator(Comparator.comparing(Masterpiece::getWeight));
    }

    public void sortByArea(ActionEvent actionEvent) {
        SortedList<Masterpiece> items = (SortedList<Masterpiece>) masterpiecesTable.getItems();
        items.setComparator(Comparator.comparing(Masterpiece::getArea));
    }

    public void authorEdited(KeyEvent keyEvent) {
        Masterpiece selectedItem = masterpiecesTable.getSelectionModel().getSelectedItem();
        selectedItem.setAuthor(autorTextField.getText());
        masterpiecesTable.refresh();
    }
}
