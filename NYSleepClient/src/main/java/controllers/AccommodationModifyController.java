package controllers;

import it.unipi.lsmsd.nysleep.DTO.AccommodationDTO;
import it.unipi.lsmsd.nysleep.DTO.AccommodationDetailsDTO;
import it.unipi.lsmsd.nysleep.DTO.ModifiedAccDTO;
import it.unipi.lsmsd.nysleep.DTO.RenterDetailsDTO;
import it.unipi.lsmsd.nysleep.RMI.RenterServicesRMI;
import it.unipi.lsmsd.nysleep.RMI.UserServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

public class AccommodationModifyController extends Controller{

    private ObservableList<Integer> beds = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

    @FXML
    private TextArea amenities;

    @FXML
    private Label amenitiesLabel;

    @FXML
    private VBox amenitiesVBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox<Integer> bedsChoice;

    @FXML
    private Label bedsLabel;

    @FXML
    private VBox bedsVBox;

    @FXML
    private HBox hBoxAP;

    @FXML
    private HBox hBoxNP;

    @FXML
    private HBox hBoxNumbers;

    @FXML
    private Pane mainPane;

    @FXML
    private VBox mainVBox;

    @FXML
    private Button modifyButton;

    @FXML
    private TextField name;

    @FXML
    private Label nameLabel;

    @FXML
    private VBox nameVBox;

    @FXML
    private TextField price;

    @FXML
    private Label priceLabel;

    @FXML
    private VBox priceVBox;

    @FXML
    private ChoiceBox<Integer> roomsChoice;

    @FXML
    private Label roomsLabel;

    @FXML
    private VBox roomsVBox;

    @FXML
    private Label title;


    private AccommodationDTO acc;

    private AccommodationDetailsDTO accDetails;

    @FXML
    private void modifyAccommodation(MouseEvent event) throws NotBoundException, RemoteException {
        try {
            Registry registry = getRegistry();
            RenterServicesRMI rent = (RenterServicesRMI) registry.lookup("renterServices");
            List<String> amenitiesList = Arrays.asList(amenities.getText().split("\\s*,\\s*"));
            ModifiedAccDTO oldAcc = new ModifiedAccDTO(accDetails.getId(), accDetails.getName(), null,
                    accDetails.getNumBeds(), 0, accDetails.getNumRooms(), accDetails.getAmenities(), accDetails.getPrice());
            ModifiedAccDTO newAcc = new ModifiedAccDTO(accDetails.getId(), name.getText(), null, bedsChoice.getValue().intValue(),
                    0, roomsChoice.getValue().intValue(), amenitiesList, Double.parseDouble(price.getText()));
            rent.modifyAccommodation(oldAcc, newAcc);
            printAlert(Alert.AlertType.INFORMATION, "Accommodation correctly modified", ButtonType.OK);
        }catch (NotBoundException | BusinessException | RemoteException e){
            e.printStackTrace();
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }

    public void cleanFields() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        UserServicesRMI rentServices = (UserServicesRMI) registry.lookup("userServices");
        AccommodationDetailsDTO details = rentServices.showAccDetails(getAcc());
        setAccDetails(details);
        bedsChoice.setItems(beds);
        roomsChoice.setItems(beds);
        name.setText(getAccDetails().getName());
        price.setText(String.valueOf(getAccDetails().getPrice()));
        bedsChoice.setValue(getAccDetails().getNumBeds());
        roomsChoice.setValue(getAccDetails().getNumRooms());
        String amenitiesString = "";
        for (String a: getAccDetails().getAmenities()){
            if(!a.equals(getAccDetails().getAmenities().get(getAccDetails().getAmenities().size() - 1))){
                amenitiesString += a + ", ";
            }
            else{
                amenitiesString += a + ".";
            }

        }
        amenities.setText(amenitiesString);


    }

    public AccommodationDTO getAcc() {
        return acc;
    }

    public void setAcc(AccommodationDTO acc) {
        this.acc = acc;
    }

    public AccommodationDetailsDTO getAccDetails() {
        return accDetails;
    }

    public void setAccDetails(AccommodationDetailsDTO accDetails) {
        this.accDetails = accDetails;
    }

    @FXML
    public void backToAccommodations(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(getScene());
    }
}
