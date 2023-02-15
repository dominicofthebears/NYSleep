package controllers;

import it.unipi.lsmsd.nysleep.DTO.AccommodationDetailsDTO;
import it.unipi.lsmsd.nysleep.DTO.RenterDTO;
import it.unipi.lsmsd.nysleep.DTO.RenterDetailsDTO;
import it.unipi.lsmsd.nysleep.RMI.RenterServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InsertAccommodationController extends Controller{

    private ObservableList<Integer> numBeds = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    @FXML
    private TextArea Amenities;

    @FXML
    private Label AmenitiesLabel;

    @FXML
    private VBox AmenitiesVBox;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private ChoiceBox<Integer> Beds;

    @FXML
    private Label BedsLabel;

    @FXML
    private VBox BedsVBox;

    @FXML
    private HBox HBoxNN;

    @FXML
    private HBox HBoxNumbers;

    @FXML
    private HBox HBoxTP;

    @FXML
    private Button InsertButton;

    @FXML
    private Pane MainPane;

    @FXML
    private VBox MainVBox;

    @FXML
    private TextField Name;

    @FXML
    private Label NameLabel;

    @FXML
    private VBox NameVBox;

    @FXML
    private TextField Neighborhood;

    @FXML
    private Label NeighborhoodLabel;

    @FXML
    private VBox NeighborhoodVBox;

    @FXML
    private VBox PicVBox;

    @FXML
    private TextField Price;

    @FXML
    private Label PriceLabel;

    @FXML
    private VBox PriceVBox;

    @FXML
    private ChoiceBox<Integer> Rooms;

    @FXML
    private Label RoomsLabel;

    @FXML
    private VBox RoomsVBox;

    @FXML
    private Label Title;

    @FXML
    private TextField Type;

    @FXML
    private Label TypeLabel;

    @FXML
    private VBox TypeVBox;

    public void initialize(){
        Beds.setItems(numBeds);
        Rooms.setItems(numBeds);
    }

    public void insertAccommodation(){
        try {
            Registry registry = getRegistry();
            RenterServicesRMI rent = (RenterServicesRMI) registry.lookup("renterServices");
            if(Name.getText()==null || Neighborhood.getText()==null || Beds.getValue()==null || Rooms.getValue()==null || Type.getText()==null || Price.getText()==null || Amenities.getText()==null ) {
                printAlert(Alert.AlertType.WARNING, "Set all the fields", ButtonType.OK);
            }else{
                AccommodationDetailsDTO accDTO = new AccommodationDetailsDTO();
                accDTO.setId(0);
                accDTO.setName(Name.getText());
                accDTO.setNeighborhood(Neighborhood.getText());
                accDTO.setNumBeds(Beds.getValue());
                accDTO.setNumRooms(Rooms.getValue());
                accDTO.setPropertyType(Type.getText());
                Double price = Double.parseDouble(Price.getText());
                accDTO.setPrice(price);
                List<String> amenities = Arrays.asList(Amenities.getText().split("\\s*,\\s*"));
                accDTO.setAmenities(amenities);
                accDTO.setImagesURL(null);
                RenterDTO renterDTO = (RenterDTO) getLoggedUser();
                RenterDetailsDTO renterDetailsDTO = new RenterDetailsDTO(renterDTO.getId(), renterDTO.getFirstName(), renterDTO.getLastName(), renterDTO.getWorkEmail(), renterDTO.getPhone());
                accDTO.setRenterDetailsDTO(renterDetailsDTO);
                rent.addAccommodation(accDTO);
                printAlert(Alert.AlertType.INFORMATION, "Accommodation correctly inserted", ButtonType.OK);
            }
        }catch (RemoteException | NotBoundException | BusinessException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }

}
