package controllers;

import it.unipi.lsmsd.nysleep.DTO.AccommodationDTO;
import it.unipi.lsmsd.nysleep.DTO.CustomerDTO;
import it.unipi.lsmsd.nysleep.DTO.PageDTO;
import it.unipi.lsmsd.nysleep.RMI.CustomerServicesRMI;
import it.unipi.lsmsd.nysleep.RMI.UserServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SuggestionsController extends Controller{

    @FXML
    private Button backButton;

    @FXML
    private Pane mainPane;

    @FXML
    private Pagination pagination;

    @FXML
    private Pagination pagination1;

    @FXML
    private GridPane researchBar;

    @FXML
    private Label title;

    @FXML
    public void initialize() {
        try {
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return createPage(pageIndex, "userSuggestion");
                    } catch (BusinessException | RemoteException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        return null;
                    }
                }
            });

            pagination1.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return createPage(pageIndex, "renterSuggestion");
                    } catch (BusinessException | RemoteException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }


    private Node createPage(Integer pageIndex, String suggestionType) throws BusinessException, RemoteException {
        try{
            CustomerServicesRMI usr = (CustomerServicesRMI) getRegistry().lookup("customerServices");
            ListView listView = new ListView();
            PageDTO<AccommodationDTO> accs;

            if(suggestionType.equals("userSuggestion")){
                accs = usr.showSuggestedAccommodations((CustomerDTO) getLoggedUser());
            }
            else{
                accs = usr.showAccommodationOfSuggestedRenter((CustomerDTO) getLoggedUser());
            }

            for (AccommodationDTO acc : accs.getEntries()){
                listView.getItems().add(acc);
            }
            VBox vbox = new VBox(listView);

            for (Node child: vbox.getChildren()){
                VBox.setVgrow(child, Priority.ALWAYS);
                child.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            if(mouseEvent.getClickCount() == 2){
                                try{
                                    ListView l = (ListView) vbox.getChildren().get(0);
                                    AccommodationDTO acc = (AccommodationDTO) l.getSelectionModel().getSelectedItem();
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/accommodationPage.fxml"));
                                    Parent root = loader.load();
                                    Stage s = new Stage();
                                    s.setTitle("Accommodation");
                                    Scene scene = new Scene(root);
                                    s.setScene(scene);
                                    s.show();
                                    AccommodationPageController controller = loader.getController();
                                    controller.setAccommodation(acc);
                                    controller.setLoggedUser(getLoggedUser());
                                    controller.setStage(s);
                                }catch (BusinessException | IOException | NotBoundException e){
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    }
                });
            }

            return vbox;
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @FXML
    void backToMainPage(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(getScene());
    }

}

