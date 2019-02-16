package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


    private static AnchorPane root;
    private static List<Parent> panes = new ArrayList<>();
    private static int currentIndex = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("root.fxml"));

        panes.add(FXMLLoader.load(getClass().getResource("welcome.fxml")));
        panes.add(FXMLLoader.load(getClass().getResource("main.fxml")));
        panes.add(FXMLLoader.load(getClass().getResource("bye.fxml")));

        primaryStage.setOnCloseRequest(event -> {
            showPane(2); // pokazujemy okienko wyjścia
            event.consume(); // "zjadamy" event, żeby nie wyzwolił rzeczywistego zakmnięcia okna
        });

        root.getChildren().add(panes.get(0));

        primaryStage.setTitle("Masterpiece");
        primaryStage.setScene(new Scene(root, 1200, 750));
        primaryStage.show();
    }

    public static void showPane(int index) {
        root.getChildren().remove(panes.get(currentIndex));
        root.getChildren().add(panes.get(index));
        currentIndex = index;
    }

    public static void main(String[] args) {
        launch(args);
    }



}
