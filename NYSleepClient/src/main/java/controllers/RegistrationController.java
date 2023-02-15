package controllers;

import it.unipi.lsmsd.nysleep.DTO.RegisteredUserDTO;
import it.unipi.lsmsd.nysleep.RMI.UnregisteredUserServicesRMI;
import it.unipi.lsmsd.nysleep.RMI.UserServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController extends Controller{

    @FXML
    private TextField Address;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private TextField Country;

    @FXML
    private TextField Email;

    @FXML
    private TextField FirstName;

    @FXML
    private HBox HBoxACW;

    @FXML
    private HBox HBoxEP;

    @FXML
    private HBox HBoxNS;

    @FXML
    private HBox HBoxTP;

    @FXML
    private Label LabelAddress;

    @FXML
    private Label LabelCountry;

    @FXML
    private Label LabelEmail;

    @FXML
    private Label LabelFN;

    @FXML
    private Label LabelLN;

    @FXML
    private Label LabelPassword;

    @FXML
    private Label LabelPhone;

    @FXML
    private Label LabelType;

    @FXML
    private Label LabelWorkEmail;

    @FXML
    private TextField LastName;

    @FXML
    private PasswordField Password;

    @FXML
    private TextField Phone;

    @FXML
    private Button RegisterButton;

    @FXML
    private Label RegisterTitle;

    @FXML
    private ScrollPane ScrollPane;

    @FXML
    private ChoiceBox<String> Types;

    @FXML
    private VBox VBox;

    @FXML
    private VBox VBoxAddress;

    @FXML
    private VBox VBoxCountry;

    @FXML
    private VBox VBoxEmail;

    @FXML
    private VBox VBoxFN;

    @FXML
    private VBox VBoxLN;

    @FXML
    private VBox VBoxPassword;

    @FXML
    private VBox VBoxPhone;

    @FXML
    private VBox VBoxType;

    @FXML
    private VBox VBoxWorkEmail;

    @FXML
    private TextField WorkEmail;

    private ObservableList<String> users = FXCollections.observableArrayList("Customer", "Renter");

    @FXML
    public void initialize() {
        Types.setItems(users);
        Types.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("Customer")) {
                WorkEmail.setDisable(true);
                Country.setDisable(false);
                Address.setDisable(false);
            } else if (newVal.equals("Renter")){
                Country.setDisable(true);
                Address.setDisable(true);
                WorkEmail.setDisable(false);
            }

        });
    }

    @FXML
    public void register(MouseEvent event) throws NotBoundException, RemoteException {
        UnregisteredUserServicesRMI usr = (UnregisteredUserServicesRMI) getRegistry().lookup("unregisteredUserServices");
        RegisteredUserDTO logged_in = null;
        Matcher matcher;
        Pattern validEmail =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        try{
            if(Types.getValue().equals("Customer")) {
                if (FirstName.getText().isEmpty() || LastName.getText().isEmpty() || Email.getText().isEmpty() || Password.getText().isEmpty() ||
                        Phone.getText().isEmpty() || Address.getText().isEmpty() || Country.getText().isEmpty()
                || !validEmail.matcher(Email.getText()).find()){
                    printAlert(Alert.AlertType.ERROR, "Fill all the fields with valid values", ButtonType.OK);
                }
                else{
                    setLoggedUser(usr.register(FirstName.getText(), LastName.getText(), Email.getText(), Password.getText(), "", "customer", Address.getText(),
                            Country.getText(), Phone.getText(), ""));
                    printAlert(Alert.AlertType.INFORMATION, "Registration succesful", ButtonType.OK);
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }

            }

            if(Types.getValue().equals("Renter")) {
                if (FirstName.getText().isEmpty() || LastName.getText().isEmpty() ||
                        Email.getText().isEmpty() || Password.getText().isEmpty() || Phone.getText().isEmpty() || WorkEmail.getText().isEmpty()
                        || !validEmail.matcher(Email.getText()).find() || !validEmail.matcher(WorkEmail.getText()).find()) {
                    printAlert(Alert.AlertType.ERROR, "Fill all the fields with valid values", ButtonType.OK);
                }
                else{
                    setLoggedUser(usr.register(FirstName.getText(), LastName.getText(), Email.getText(), Password.getText(), "", "renter", "",
                            "", Phone.getText(), WorkEmail.getText()));
                    printAlert(Alert.AlertType.INFORMATION, "Registration succesful", ButtonType.OK);

                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
            }
        }catch (BusinessException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }

    }

}