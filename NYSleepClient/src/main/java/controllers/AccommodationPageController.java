package controllers;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.RMI.AdminServicesRMI;
import it.unipi.lsmsd.nysleep.RMI.CustomerServicesRMI;
import it.unipi.lsmsd.nysleep.RMI.UserServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AccommodationPageController extends Controller{

    @FXML
    private VBox AccInfoVBox;

    @FXML
    private Label AccName;

    @FXML
    private Label Amenities;

    @FXML
    private Label AmenitiesLabel;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button ButtonReserve;

    @FXML
    private HBox DateHBox;

    @FXML
    private DatePicker EndDate;

    @FXML
    private Label EndDateLabel;

    @FXML
    private Pane MainPane;

    @FXML
    private ImageView MainPic;

    @FXML
    private HBox NeighHBox;

    @FXML
    private Label NeighLabel;

    @FXML
    private Label Neighborhood;

    @FXML
    private Label NumBeds;

    @FXML
    private HBox NumBedsHBox;

    @FXML
    private Label NumBedsLabel;

    @FXML
    private Label NumRooms;

    @FXML
    private HBox NumRoomsHBox;

    @FXML
    private Label NumRoomsLabel;

    @FXML
    private Label Price;

    @FXML
    private HBox PriceHBox;

    @FXML
    private Label PriceLabel;

    @FXML
    private Label PropertyType;

    @FXML
    private HBox PropertyTypeHBox;

    @FXML
    private Label PropertyTypeLabel;

    @FXML
    private Label Rating;

    @FXML
    private HBox RatingHBox;

    @FXML
    private Label RatingLabel;

    @FXML
    private Label RenterLabel;

    @FXML
    private Label RenterMail;

    @FXML
    private Label RenterNames;

    @FXML
    private Label RenterPhone;

    @FXML
    private VBox SideVBOx;

    @FXML
    private DatePicker StartDate;

    @FXML
    private Label StartDateLabel;

    @FXML
    private Button showReviews;

    @FXML
    private Button backButton;

    private AccommodationDTO accommodation;

    @FXML
    private Button deleteAccButton;

    @FXML
    private Button reservationsButton;

    public void initialize(){

    }


    public void createReservation(ActionEvent actionEvent) throws NotBoundException, RemoteException, BusinessException {
        RegisteredUserDTO loggedUser = getLoggedUser();

        try {

            if(loggedUser == null){
                printAlert(Alert.AlertType.ERROR, "Please, log in or register to reserve an accommodation", ButtonType.OK);
                return;
            }

            Registry registry = getRegistry();
            CustomerServicesRMI usr = (CustomerServicesRMI) registry.lookup("customerServices");

            if(!usr.checkAvailability(accommodation, StartDate.getValue(), EndDate.getValue())){
                printAlert(Alert.AlertType.ERROR, "Accommodation not available for those dates", ButtonType.OK);
                return;
            }

            CustomerDTO cust = (CustomerDTO) loggedUser;
            double totalCost = (Double.parseDouble(Price.getText())*(ChronoUnit.DAYS.between(StartDate.getValue(), EndDate.getValue())));

            ReservationDTO reservation = new ReservationDTO(0, StartDate.getValue(), EndDate.getValue(), totalCost,
                    loggedUser.getId(), loggedUser.getFirstName(), loggedUser.getLastName(), cust.getCountry(), accommodation.getId(),
                    accommodation.getName(), accommodation.getNeighborhood());
            usr.insertReservation(reservation);
            printAlert(Alert.AlertType.INFORMATION, "Reservation succefully created!", ButtonType.OK);
        }catch (BusinessException e){
        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }


        }


    public void showReviews(ActionEvent actionEvent) throws NotBoundException, RemoteException, BusinessException {
        Stage stage=getStage();
        AccommodationDTO acc = new AccommodationDTO();
        acc.setId(accommodation.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listPage.fxml"));
            Parent root = loader.load();
            ListPageController controller = loader.getController();
            Scene revS = new Scene(root);
            stage.setTitle("Show reviews");
            stage.setScene(revS);
            setStage(stage);
            getStage().show();
            controller.setLoggedUser(getLoggedUser());
            controller.setRevList(acc);
            controller.setScene(((Node)(actionEvent.getSource())).getScene());

        }catch (BusinessException | IOException| NotBoundException e){
            throw new RuntimeException(e);
        }
    }

    public void setAccommodation(AccommodationDTO acc) throws NotBoundException, IOException, BusinessException {
        Registry registry = getRegistry();
        UserServicesRMI usr = (UserServicesRMI) registry.lookup("userServices");
        if(getLoggedUser() != null && getLoggedUser().getType().equals("renter")){
            StartDate.setDisable(true);
            EndDate.setDisable(true);
            ButtonReserve.setDisable(true);
        }

        else if(getLoggedUser() != null && getLoggedUser().getType().equals("admin")){
            deleteAccButton.setDisable(false);
            deleteAccButton.setVisible(true);
            reservationsButton.setVisible(true);
            reservationsButton.setDisable(false);
            StartDate.setDisable(true);
            EndDate.setDisable(true);
            ButtonReserve.setDisable(true);
        }
        AccommodationDetailsDTO details = usr.showAccDetails(acc);
        accommodation = acc;
        Neighborhood.setText(details.getNeighborhood());
        AccName.setText(details.getName());
        NumBeds.setText(Integer.toString(details.getNumBeds()));
        NumRooms.setText(Integer.toString(details.getNumRooms()));
        PropertyType.setText(details.getPropertyType());
        Rating.setText(Double.toString(details.getRating()));
        Price.setText(Double.toString(details.getPrice()));
        String amenitiesString = "";
        for (String a: details.getAmenities()){
            if(!a.equals(details.getAmenities().get(details.getAmenities().size() - 1))){
                amenitiesString += a + ", ";
            }
            else{
                amenitiesString += a + ".";
            }

        }
        Amenities.setText(amenitiesString);
        RenterNames.setText(details.getRenterDetailsDTO().getFirstName() + " " + details.getRenterDetailsDTO().getLastName());
        RenterMail.setText(details.getRenterDetailsDTO().getWorkEmail());
        RenterPhone.setText(details.getRenterDetailsDTO().getPhone());
    }

    public void setStartDate(LocalDate date){
        this.StartDate.setValue(date);
    }

    public void setEndDate(LocalDate date){
        this.EndDate.setValue(date);
    }

    @FXML
    public void backToRentAccommodations(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(getScene());
    }

    public Button getBackButton(){
        return this.backButton;
    }

    @FXML
    void deleteAccommodation(MouseEvent event) {
        try{
            Registry registry = getRegistry();
            AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
            admin.removeAccommodation(accommodation);
            printAlert(Alert.AlertType.INFORMATION, "Accommodation succefully deleted", ButtonType.OK);
            getStage().close();
        }catch (BusinessException|NotBoundException|RemoteException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }

    }

    @FXML
    void showAccReservations(MouseEvent event) throws NotBoundException, RemoteException, BusinessException {
        Stage stage = getStage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listPage.fxml"));
            Parent root = loader.load();
            ListPageController controller = loader.getController();
            Scene revS = new Scene(root);
            stage.setScene(revS);
            setStage(stage);
            getStage().show();
            controller.setAccReservations(accommodation);
            controller.setScene(((Node)(event.getSource())).getScene());
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
