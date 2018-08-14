package Src;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextArea textArea;//where the text is input
    private Stage stage;
    private final FileChooser file = new FileChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        file.setInitialDirectory(new File(System.getProperty("user.home")));
        file.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text", "*.txt"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
    }

    @FXML
    public void newFile() {
        if (textArea.getText().trim().length() == 0) {
            textArea.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Create New File without saving?");

            ButtonType buttonTypeOne = new ButtonType("Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                save();
            } else {
                //its cancelled
            }
        }
    }
    @FXML
    public void openFile() {
        if (textArea.getText().trim().length() == 0) {
            textArea.clear();//this removes any existing text
            file.setTitle("Open File");
            File F = file.showOpenDialog(stage);
            if (F != null) {

                readText(F);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Open new file without saving current?");

            ButtonType buttonTypeOne = new ButtonType("Save and Open");
            ButtonType buttonTypeTwo = new ButtonType("Open without saving");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                save();
                textArea.clear();//this removes any existing text
                file.setTitle("Open File");
                File F = file.showOpenDialog(stage);
                if (F != null) {
                    readText(F);
                }
            } else if(result.get() == buttonTypeTwo) {
                file.setTitle("Open File");
                File F = file.showOpenDialog(stage);
                if (F != null) {
                    textArea.clear();//this removes any existing text
                    readText(F);
                }
            }else  {
                //its cancelled
            }
        }
    }
    @FXML
    private void save() {
        try {
            file.setTitle("Save As");
            File F = file.showSaveDialog(stage);

            if (F != null) {
                PrintWriter savedText = new PrintWriter(F);
                BufferedWriter out = new BufferedWriter(savedText);
                out.write(textArea.getText());
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void Close() {
        Platform.exit();
    }

    private void readText(File F) {
        String T;
        try (BufferedReader br = new BufferedReader(new FileReader(F))) {
            while ((T = br.readLine()) != null) { textArea.appendText(T + " \n"); }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}