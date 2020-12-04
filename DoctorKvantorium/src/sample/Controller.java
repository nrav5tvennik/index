package sample;

import com.client.Connect;
import com.client.ConnectInfo;
import com.client.clients.Doctor;
import com.client.clients.StatusDoctor;
import com.client.clients.events.DoctorEvents;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private Label label;
    @FXML
    private Button next;
    @FXML
    private Button work;
    @FXML
    private Button end;

    private StatusDoctor statusDoctor = StatusDoctor.NONE;
    private Doctor doctor;

    @FXML
    private void initialize() {
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
            doctor = Connect.authorizeDoctor(new ConnectInfo("localhost", 5000), idReception, new MyDoctorEvents());
        }
        catch (IOException exp) {
            exp.printStackTrace();
        }

        work.setDisable(true);
        end.setDisable(true);
    }

    private class MyDoctorEvents implements DoctorEvents {
        @Override
        public void setCountPatients(int i) {
            label.setText("Кол-во пациентов: " + i);
        }
    }

    @FXML
    private void pressNext(ActionEvent event) {
        next.setDisable(true);
        work.setDisable(false);
        try {
            doctor.setStatus(StatusDoctor.NEXT);
        }
        catch (IOException exp) {
            System.exit(0);
        }
    }

    @FXML
    private void pressWork(ActionEvent event) {
        work.setDisable(true);
        end.setDisable(false);
        try {
            doctor.setStatus(StatusDoctor.WORK);
        }
        catch (IOException exp) {
            System.exit(0);
        }
    }

    @FXML
    private void pressEnd(ActionEvent event) {
        end.setDisable(true);
        next.setDisable(false);
        try {
            doctor.setStatus(StatusDoctor.END);
        }
        catch (IOException exp) {
            System.exit(0);
        }
    }
}