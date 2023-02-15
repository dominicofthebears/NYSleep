package controllers;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.RMI.*;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import javafx.scene.input.MouseEvent;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class UserModifyController extends Controller{
    @FXML
    private Label LabelLN;

    @FXML
    private Label LalbelEmail;

    @FXML
    private TextField lastName;

    @FXML
    private TextField address;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField country;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private HBox hBoxACW;

    @FXML
    private HBox hBoxEP;

    @FXML
    private HBox hBoxNS;

    @FXML
    private HBox hBoxTP;

    @FXML
    private Label labelAddress;

    @FXML
    private Label labelCountry;

    @FXML
    private Label labelFN;

    @FXML
    private Label labelPassword;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelTitle;

    @FXML
    private Label labelWorkEmail;

    @FXML
    private VBox mainVBox;

    @FXML
    private Button modifyButton;

    @FXML
    private Label modifyTitle;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField title;

    @FXML
    private VBox vBoxAddress;

    @FXML
    private VBox vBoxCountry;

    @FXML
    private VBox vBoxEmail;

    @FXML
    private VBox vBoxFN;

    @FXML
    private VBox vBoxLN;

    @FXML
    private VBox vBoxPassword;

    @FXML
    private VBox vBoxPhone;

    @FXML
    private VBox vBoxTitle;

    @FXML
    private VBox vBoxWorkEmail;

    @FXML
    private TextField workEmail;

    public void cleanFields() throws NotBoundException, RemoteException, BusinessException {
        firstName.setText(getLoggedUser().getFirstName());
        lastName.setText(getLoggedUser().getLastName());
        email.setText(getLoggedUser().getEmail());
        password.setText(getLoggedUser().getPassword());
        if(getLoggedUser().getType().equals("customer")){
            CustomerServicesRMI c = (CustomerServicesRMI) getRegistry().lookup("customerServices");
            ModifiedCustomerDTO toModify = c.getAdditionalInformation((CustomerDTO) getLoggedUser());
            country.setText(toModify.getCountry());
            address.setText(toModify.getAddress());
            phone.setText(toModify.getPhone());
            workEmail.setDisable(true);
            title.setDisable(true);
        }
        else if(getLoggedUser().getType().equals("renter")){
            RenterDTO rent = (RenterDTO) getLoggedUser();
            workEmail.setText(rent.getWorkEmail());
            phone.setText(rent.getPhone());
            country.setDisable(true);
            address.setDisable(true);
            title.setDisable(true);
        }
        else{
            AdminDTO admin = (AdminDTO) getLoggedUser();
            title.setText(admin.getTitle());
            workEmail.setDisable(true);
            country.setDisable(true);
            address.setDisable(true);
            phone.setDisable(true);
        }
    }


    public void modifyUser(MouseEvent event) {
        try {
            if(getLoggedUser().getType().equals("customer")){
                CustomerServicesRMI c = (CustomerServicesRMI) getRegistry().lookup("customerServices");
                CustomerDTO cust = (CustomerDTO) getLoggedUser();
                ModifiedCustomerDTO oldCustomer = new ModifiedCustomerDTO();
                oldCustomer.setId(cust.getId());
                oldCustomer.setEmail(cust.getEmail().toLowerCase());
                oldCustomer.setFirstName(cust.getFirstName());
                oldCustomer.setLastName(cust.getLastName());
                oldCustomer.setCountry(cust.getCountry());
                ModifiedCustomerDTO modifiedCust = new ModifiedCustomerDTO(getLoggedUser().getId(), firstName.getText(), lastName.getText(),
                        country.getText(), email.getText(), password.getText(), phone.getText(), "", address.getText());


                c.modifyUser(oldCustomer, modifiedCust);
                UnregisteredUserServicesRMI uus = (UnregisteredUserServicesRMI) getRegistry().lookup("unregisteredUserServices");
                this.setLoggedUser(uus.login(email.getText(), password.getText()));
                printAlert(Alert.AlertType.INFORMATION, "Information updated succefully", ButtonType.OK);

            }
            else if(getLoggedUser().getType().equals("renter")){
                RenterServicesRMI c = (RenterServicesRMI) getRegistry().lookup("renterServices");
                ModifiedRenterDTO oldRenter = new ModifiedRenterDTO();
                RenterDTO rent = (RenterDTO) getLoggedUser();
                oldRenter.setId(getLoggedUser().getId());
                oldRenter.setEmail(getLoggedUser().getEmail());
                oldRenter.setFirstName(rent.getFirstName());
                oldRenter.setLastName(rent.getLastName());
                oldRenter.setWorkEmail(rent.getWorkEmail());
                oldRenter.setPhone(rent.getPhone());
                ModifiedRenterDTO modifiedRent = new ModifiedRenterDTO(getLoggedUser().getId(), firstName.getText(), lastName.getText(),
                        workEmail.getText(), phone.getText(), email.getText(), password.getText(), "");
                System.out.println(modifiedRent.getEmail());
                System.out.println(oldRenter.getEmail());
                c.modifyUser(oldRenter, modifiedRent);
                UnregisteredUserServicesRMI uus = (UnregisteredUserServicesRMI) getRegistry().lookup("unregisteredUserServices");
                this.setLoggedUser(uus.login(email.getText(), password.getText()));
                printAlert(Alert.AlertType.INFORMATION, "Information updated succefully", ButtonType.OK);
            }

            else{
                AdminServicesRMI c = (AdminServicesRMI) getRegistry().lookup("adminServices");
                AdminDTO oldAdmin = new AdminDTO();
                oldAdmin.setId(getLoggedUser().getId());
                oldAdmin.setEmail(getLoggedUser().getEmail());
                AdminDTO modifiedAdmin = new AdminDTO(getLoggedUser().getId(), firstName.getText(), lastName.getText(),
                        email.getText(), password.getText(), title.getText());
                c.modifyUser(oldAdmin, modifiedAdmin);
                UnregisteredUserServicesRMI uus = (UnregisteredUserServicesRMI) getRegistry().lookup("unregisteredUserServices");
                this.setLoggedUser(uus.login(email.getText(), password.getText()));
                printAlert(Alert.AlertType.INFORMATION, "Information updated succefully", ButtonType.OK);
            }

        }catch (BusinessException|NotBoundException|RemoteException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }



    }
}
