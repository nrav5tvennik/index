
import com.client.Connect;
import com.client.ConnectInfo;
import com.client.clients.Reception;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class MainController
{
    @FXML
    private TreeView<String> treeView;
    @FXML
    private TextField idField;

    private Reception reception;

    private TreeItem<String> rootItem = new TreeItem<>();

    private String ip = "192.168.0.102";
    private int port = 5000;

    @FXML
    public void initialize() {
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        String idReception = "";

        TextInputDialog idReceptionDialog = new TextInputDialog();
        idReceptionDialog.setHeaderText("Please, enter your id");
        idReceptionDialog.setTitle("Dialog");
        idReceptionDialog.setContentText("Your id: ");
        Optional<String> result = idReceptionDialog.showAndWait();
        if (result.isPresent()){
            idReception = result.get();
        }

        try {
            reception = Connect.authorizeReception(new ConnectInfo("localhost", 5000), idReception);
        }
        catch (IOException exp) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Error authorization");
            alert.showAndWait();
            System.exit(0);
        }
    }

    @FXML
    private void pressAddToQueue(ActionEvent event) {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item == null)
            return;

        try {
            Process p = Runtime.getRuntime().exec("python pyqr/main.py");

            String idPatient = idField.getText();
            reception.addPatientToStack(item.getValue(), idPatient);
            IOConnection ioConnection = new IOConnection(new Socket("localhost", 5001));
            ioConnection.write(ip + ";" + port + ";" + idPatient + ";" + item.getValue());
            String path = ioConnection.read();
            ioConnection.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/image.fxml"));
            fxmlLoader.setController(new ImageController(new Image(new FileInputStream(path))));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        idField.setText("");
    }

    @FXML
    private void pressUpdate(ActionEvent event)
    {
        rootItem.getChildren().clear();

        try {
            for (String stack : reception.getStacks()) {
                TreeItem<String> treeItem = new TreeItem<>(stack);

                rootItem.getChildren().add(treeItem);
            }
        }
        catch (IOException exp) {

        }
    }
}