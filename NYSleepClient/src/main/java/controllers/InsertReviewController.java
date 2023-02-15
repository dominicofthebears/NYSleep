package controllers;

import it.unipi.lsmsd.nysleep.DTO.AccReviewDTO;
import it.unipi.lsmsd.nysleep.DTO.CustomerDTO;
import it.unipi.lsmsd.nysleep.DTO.CustomerReviewDTO;
import it.unipi.lsmsd.nysleep.DTO.ReservationDTO;
import it.unipi.lsmsd.nysleep.RMI.CustomerServicesRMI;
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

public class InsertReviewController extends Controller{

    private ObservableList<Integer> rates = FXCollections.observableArrayList(1, 2, 3, 4, 5);
    private int accID;

    @FXML
    private HBox accommodationHBox;

    @FXML
    private Label accommodationLabel;

    @FXML
    private Label accommodationName;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button backButton;

    @FXML
    private Label bottomLabel;

    @FXML
    private TextArea comment;

    @FXML
    private Label commentLabel;

    @FXML
    private VBox commentVBox;

    @FXML
    private Button confirmButton;

    @FXML
    private Pane mainPane;

    @FXML
    private VBox mainVBox;

    @FXML
    private ChoiceBox<Integer> rate;

    @FXML
    private HBox rateCommentHBox;

    @FXML
    private Label rateLabel;

    @FXML
    private VBox rateVbox;

    @FXML
    private Label title;

    public void initialize(){
        rate.setItems(rates);
    }

    public void setAcc(ReservationDTO res){
        accommodationName.setText(res.getAccommodationName());
        accID= res.getId();
    }

    public void insertReview(){
        try {
            if (rate.getValue() != null) {
                AccReviewDTO accReviewDTO = new AccReviewDTO();
                accReviewDTO.setCustomerId(getLoggedUser().getId());
                accReviewDTO.setCustomerFirstName(getLoggedUser().getFirstName());
                accReviewDTO.setCustomerLastName(getLoggedUser().getLastName());
                accReviewDTO.setCustomerCountry(((CustomerDTO) getLoggedUser()).getCountry());
                accReviewDTO.setRate(rate.getValue());
                if (comment.getText() == null) {
                    accReviewDTO.setComment("");
                } else {
                    accReviewDTO.setComment(comment.getText());
                }
                CustomerReviewDTO customerReviewDTO = new CustomerReviewDTO();
                customerReviewDTO.setAccommodationId(accID);
                customerReviewDTO.setAccommodationName(accommodationName.getText());
                Registry registry = getRegistry();
                CustomerServicesRMI cus = (CustomerServicesRMI) registry.lookup("customerServices");
                cus.insertReview(accReviewDTO, customerReviewDTO);
                printAlert(Alert.AlertType.INFORMATION, "Review inserted", ButtonType.OK);
            } else {
                printAlert(Alert.AlertType.WARNING, "You have to select a rate from 1 to 5", ButtonType.OK);
            }
        }catch (BusinessException | RemoteException | NotBoundException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }

    public void backToResPage(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(getScene());
    }
}

