package controllers;

import it.unipi.lsmsd.nysleep.DTO.RegisteredUserDTO;
import it.unipi.lsmsd.nysleep.RMI.UnregisteredUserServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LoginController extends Controller{

        @FXML
        private AnchorPane AnchorPane;

        @FXML
        private Label EmailLabel;

        @FXML
        private Label LoginTitle;

        @FXML
        private Label PasswordLabel;

        @FXML
        private TextField email;

        @FXML
        private VBox loginBox;

        @FXML
        private Button loginButton;

        @FXML
        private PasswordField password;


    @FXML
    public void login(MouseEvent event) throws NotBoundException {
        try {
            UnregisteredUserServicesRMI uus = (UnregisteredUserServicesRMI) getRegistry().lookup("unregisteredUserServices");
            this.setLoggedUser(uus.login(email.getText(), password.getText()));
            printAlert(Alert.AlertType.INFORMATION, "Login successful", ButtonType.OK);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }catch (BusinessException|RemoteException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }

    @FXML
    public void initialize(){
        loginBox.setAlignment(Pos.CENTER);
    }

}
