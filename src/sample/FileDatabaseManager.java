package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FileDatabaseManager {

    public static ObservableList<Masterpiece> loadEmployeesFromFile(File file) {

        ObservableList<Masterpiece> masterpieces = FXCollections.observableArrayList();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {

            while (scanner.hasNext()) {
                masterpieces.add(new Masterpiece(
                        scanner.next(),
                        scanner.next(),
                        Integer.parseInt(scanner.next()),
                        Integer.parseInt(scanner.next()),
                        Integer.parseInt(scanner.next())

                ));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return masterpieces;
    }

    public static void saveEmployeesToFile(List<Masterpiece> masterpieces, File file) {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

            for (Masterpiece masterpiece : masterpieces) {
                out.write(masterpiece.getAuthor());
                out.write(" ");
                out.write(masterpiece.getPicture());
                out.write(" ");
                out.write(String.valueOf(masterpiece.getWeight()));
                out.write(" ");
                out.write(String.valueOf(masterpiece.getWidth()));
                out.write(" ");
                out.write(String.valueOf(masterpiece.getHeight()));
                out.newLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveEmployeesReport(List<Masterpiece> masterpieces, File reportFile) {
        saveEmployeesReportToFile(masterpieces, reportFile);
    }

    // nie wiem czy to wystarczy jako "samodzielnie napisana" funkcja sortująca
    private static void sortEmployees(List<Masterpiece> masterpieces) {
        masterpieces.sort(new Comparator<Masterpiece>() {
            @Override
            public int compare(Masterpiece e1, Masterpiece e2) {
                return e1.getArea().compareTo(e2.getArea());
            }
        });
    }

    private static void saveEmployeesReportToFile(List<Masterpiece> masterpieces, File file) {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

            for (Masterpiece masterpiece : masterpieces) {
                out.write(masterpiece.getAuthor());
                out.write(" ");
                out.write(masterpiece.getPicture());
                out.write(" ");
                out.write(String.valueOf(masterpiece.getWeight()));
                out.write(" ");
                out.write(String.valueOf(masterpiece.getWidth()));
                out.write(" ");
                out.write(String.valueOf(masterpiece.getHeight()));
                out.write(" ");
                out.write(String.valueOf(masterpiece.getArea()));
                out.newLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
