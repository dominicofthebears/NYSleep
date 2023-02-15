package controllers;

import it.unipi.lsmsd.nysleep.DTO.*;
import it.unipi.lsmsd.nysleep.RMI.AdminServicesRMI;
import it.unipi.lsmsd.nysleep.RMI.CustomerServicesRMI;
import it.unipi.lsmsd.nysleep.RMI.RenterServicesRMI;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.time.LocalDate;

public class ListPageController extends Controller{
    @FXML
    private Button backButton;

    @FXML
    private Pane mainPane;

    @FXML
    private Pagination pagination;


    @FXML
    private Button deleteButton;


    @FXML
    private Button reviewButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Label title;

    @FXML
    private AnchorPane topPane;


    public void initialize(){

    }

    public void setRevList(AccommodationDTO acc) throws NotBoundException, RemoteException, BusinessException{
        try {
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showRevList(acc, pageIndex);
                    } catch (BusinessException | RemoteException | NotBoundException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        initialize();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    public Node showRevList(AccommodationDTO acc, int pageIndex) throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        UserServicesRMI usr = (UserServicesRMI) registry.lookup("userServices");

        AccommodationDetailsDTO accDet = usr.showAccDetails(acc);
        PageDTO<AccReviewDTO> pageDTO = usr.showAccommodationReviews(accDet);
        ListView l = new ListView<Label>();

        for(int i=pageIndex*5; i<(pageIndex+1)*5; i++){
            if(i< pageDTO.getEntries().size()){
                Label label = new Label();
                label.setWrapText(true);
                label.setText(pageDTO.getEntries().get(i).toString());
                label.setMaxWidth(pagination.getPrefWidth());
                l.getItems().add(label);
                if (getLoggedUser() != null && getLoggedUser().getType().equals("admin")){
                    title.setFont(Font.font("System", FontWeight.BOLD, 16));
                    int finalI = i;
                    label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            try{
                                Registry registry = getRegistry();
                                AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
                                deleteButton.setVisible(true);
                                deleteButton.setDisable(false);

                                deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        try{
                                            AccReviewDTO rev = (AccReviewDTO) pageDTO.getEntries().get(finalI);
                                            CustomerReviewDTO cust = new CustomerReviewDTO();
                                            cust.setId(rev.getId());
                                            cust.setAccommodationId(acc.getId());
                                            admin.deleteReview(cust, rev);
                                            printAlert(Alert.AlertType.INFORMATION, "Review correctly deleted", ButtonType.OK);
                                            setRevList(acc);
                                        }catch (BusinessException|RemoteException|NotBoundException e){
                                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                        }

                                    }
                                });
                            }catch (RemoteException | NotBoundException e){
                                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            }

                        }
                    });
            }

            }
            else{
                break;
            }
        }

        title.setText(accDet.getName()+"'s reviews");
        VBox vbox = new VBox(l);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));

        return vbox;
    }

    public void setResList(RenterDTO ren) throws NotBoundException, RemoteException, BusinessException{
        try {
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showResList(ren, pageIndex);
                    } catch (BusinessException | RemoteException | NotBoundException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        initialize();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    public Node showResList(RenterDTO ren, int pageIndex) throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        RenterServicesRMI renServ = (RenterServicesRMI) registry.lookup("userServices");
        PageDTO<ReservationDTO> pageDTO= renServ.showRenterReservation(ren);
        ListView l = new ListView<>();
        for(int i=pageIndex*5; i<(pageIndex+1)*5; i++){
            if(i< pageDTO.getEntries().size()){
                l.getItems().add(pageDTO.getEntries().get(i));
            }
            else{
                break;
            }
        }
        title.setText("Reservation on your accommodations");
        VBox vbox = new VBox(l);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        return vbox;
    }

    public void setCusResList(CustomerDTO cus) throws NotBoundException, RemoteException, BusinessException{
        try {
            backButton.setVisible(false);
            backButton.setDisable(true);
            title.setText("Your reservations");
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showCusResList(cus, pageIndex);
                    } catch (BusinessException | RemoteException | NotBoundException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        initialize();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    public Node showCusResList(CustomerDTO cus, int pageIndex) throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        CustomerServicesRMI cusServ = (CustomerServicesRMI) registry.lookup("customerServices");
        PageDTO<ReservationDTO> pageDTO= cusServ.viewReservations(cus);
        ListView l = new ListView<>();
        for(int i=pageIndex*5; i<(pageIndex+1)*5; i++){
            if(i< pageDTO.getEntries().size()){
                l.getItems().add(pageDTO.getEntries().get(i));
            }
            else{
                break;
            }
        }
        VBox vbox = new VBox(l);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        for (Node child: vbox.getChildren()){
            VBox.setVgrow(child, Priority.ALWAYS);
            child.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        ListView lv = (ListView) vbox.getChildren().get(0);
                        ReservationDTO res = (ReservationDTO) lv.getSelectionModel().getSelectedItem();
                        deleteButton.setVisible(true);
                        deleteButton.setDisable(false);
                        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                try{
                                    cusServ.deleteReservation(res);
                                    printAlert(Alert.AlertType.INFORMATION, "Reservation correctly deleted", ButtonType.OK);
                                    setCusResList((CustomerDTO) getLoggedUser());
                                }catch (BusinessException|RemoteException|NotBoundException e){
                                    printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                }

                            }
                        });
                        if(res.getendDate().isBefore(LocalDate.now())){
                            Stage s = getStage();
                            reviewButton.setVisible(true);
                            reviewButton.setDisable(false);
                            reviewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                                        try {
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/insertReviewPage.fxml"));
                                            Parent root = loader.load();
                                            InsertReviewController controller = loader.getController();
                                            controller.setScene(reviewButton.getScene());
                                            controller.setLoggedUser(getLoggedUser());
                                            Scene revS = new Scene(root);
                                            s.setTitle("Insert review");
                                            s.setScene(revS);
                                            setStage(s);
                                            getStage().show();
                                            controller.setStage(s);
                                            controller.setAcc((ReservationDTO) lv.getSelectionModel().getSelectedItem());
                                        } catch (IOException  e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        return vbox;
    }

    public void setCusRevList(CustomerDTO cus) throws NotBoundException, RemoteException, BusinessException{
        try {
            backButton.setVisible(false);
            backButton.setDisable(true);
            title.setText("Your reviews");
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showCusRevList(cus, pageIndex);
                    } catch (BusinessException | RemoteException | NotBoundException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        initialize();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    public Node showCusRevList(CustomerDTO cus, int pageIndex) throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        CustomerServicesRMI cusServ = (CustomerServicesRMI) registry.lookup("customerServices");
        PageDTO<CustomerReviewDTO> pageDTO= cusServ.getOwnReviews(cus);
        ListView l = new ListView<Label>();
        for(int i=pageIndex*5; i<(pageIndex+1)*5; i++){
            if(i< pageDTO.getEntries().size()){
                Label label = new Label();
                label.setWrapText(true);
                label.setText(pageDTO.getEntries().get(i).toString());
                label.setMaxWidth(pagination.getPrefWidth());
                int finalI = i;
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            deleteButton.setDisable(false);
                            deleteButton.setVisible(true);
                            deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    CustomerReviewDTO rev = (CustomerReviewDTO) pageDTO.getEntries().get(finalI);
                                    AccReviewDTO acc = new AccReviewDTO();
                                    acc.setCustomerId(getLoggedUser().getId());
                                    try{
                                        cusServ.deleteReview(rev, acc);
                                        printAlert(Alert.AlertType.INFORMATION, "Review correctly deleted", ButtonType.OK);
                                        setCusRevList((CustomerDTO) getLoggedUser());
                                    }catch (BusinessException|RemoteException|NotBoundException e){
                                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                    }

                                }
                            });
                        }
                    }});
                l.getItems().add(label);
            }
            }
        VBox vbox = new VBox(l);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        return vbox;
    }

    public void backToAccPage(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(getScene());
    }

    public void setRentAccList(RenterDTO rent) {
        try {
            backButton.setVisible(false);
            backButton.setDisable(true);
            title.setText("Your accommodations");
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showRentAccList(rent, pageIndex);
                    } catch (BusinessException | RemoteException | NotBoundException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        initialize();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    public Node showRentAccList(RenterDTO rent, int pageIndex) throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        RenterServicesRMI rentServices = (RenterServicesRMI) registry.lookup("renterServices");
        PageDTO<AccommodationDTO> pageDTO= rentServices.showRenterAccommodations(rent);
        ListView l = new ListView<Label>();
        for(int i=pageIndex*5; i<(pageIndex+1)*5; i++){
            if(i< pageDTO.getEntries().size()){
                Label label = new Label();
                label.setWrapText(true);
                label.setText(pageDTO.getEntries().get(i).toString());
                label.setMaxWidth(pagination.getPrefWidth());
                int finalI = i;
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            deleteButton.setDisable(false);
                            deleteButton.setVisible(true);
                            modifyButton.setDisable(false);
                            modifyButton.setVisible(true);
                            deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    AccommodationDTO acc = pageDTO.getEntries().get(finalI);
                                    try{
                                        rentServices.removeAccommodation(acc);
                                        printAlert(Alert.AlertType.INFORMATION, "Accommodation correctly deleted", ButtonType.OK);
                                        setRentAccList((RenterDTO) getLoggedUser());
                                    }catch (BusinessException|RemoteException e){
                                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                    }
                                }
                            });

                            modifyButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    AccommodationDTO acc = pageDTO.getEntries().get(finalI);
                                    try{
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyAccommodationPage.fxml"));
                                        Parent root = loader.load();
                                        AccommodationModifyController controller = loader.getController();
                                        Scene scene = new Scene(root);
                                        Stage s = (Stage) mainPane.getScene().getWindow();
                                        s.setScene(scene);
                                        controller.setAcc(acc);
                                        controller.setScene(getScene());
                                        controller.setStage(s);
                                        controller.cleanFields();
                                        s.show();
                                        setRentAccList((RenterDTO) getLoggedUser());
                                    }catch (IOException|BusinessException|NotBoundException e){
                                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                    }
                                }
                            });
                        }
                        if(mouseEvent.getClickCount() == 2){
                            try{
                                AccommodationDTO acc = pageDTO.getEntries().get(finalI);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/accommodationPage.fxml"));
                                Parent root = loader.load();
                                AccommodationPageController controller = loader.getController();
                                Scene scene = new Scene(root);
                                Stage s = (Stage) mainPane.getScene().getWindow();
                                s.setScene(scene);
                                controller.setLoggedUser(getLoggedUser());
                                controller.setAccommodation(acc);
                                controller.getBackButton().setVisible(true);
                                controller.getBackButton().setDisable(false);
                                controller.setScene(getScene());
                                controller.setStage(s);
                                s.show();
                            }catch (IOException|BusinessException|NotBoundException e){
                                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            }

                        }
                    }});
                l.getItems().add(label);
            }
        }
        VBox vbox = new VBox(l);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        return vbox;
    }

    public void setRentResList(RenterDTO rent) {
        try {
            backButton.setVisible(false);
            backButton.setDisable(true);
            title.setText("Your reservations");
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showRentResList(rent, pageIndex);
                    } catch (BusinessException | RemoteException | NotBoundException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        initialize();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    public Node showRentResList(RenterDTO rent, int pageIndex) throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        RenterServicesRMI rentServices = (RenterServicesRMI) registry.lookup("renterServices");
        PageDTO<ReservationDTO> pageDTO= rentServices.showRenterReservation(rent);
        ListView l = new ListView<Label>();
        for(int i=pageIndex*5; i<(pageIndex+1)*5; i++){
            if(i< pageDTO.getEntries().size()){
                Label label = new Label();
                label.setWrapText(true);
                label.setText(pageDTO.getEntries().get(i).toString());
                label.setMaxWidth(pagination.getPrefWidth());
                int finalI = i;
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            deleteButton.setDisable(false);
                            deleteButton.setVisible(true);
                            deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    ReservationDTO res = (ReservationDTO) pageDTO.getEntries().get(finalI);
                                    try{
                                        if(res.getstartDate().isAfter(LocalDate.now())){
                                            rentServices.deleteReservation(res);
                                            printAlert(Alert.AlertType.INFORMATION, "Reservation correctly deleted", ButtonType.OK);
                                            setRentResList((RenterDTO) getLoggedUser());
                                        }
                                        else{
                                            printAlert(Alert.AlertType.WARNING, "This reservation already started, you can't delete it", ButtonType.OK);
                                        }
                                    }catch (BusinessException|RemoteException e){
                                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                    }

                                }
                            });
                        }
                    }});
                l.getItems().add(label);
            }
        }
        VBox vbox = new VBox(l);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        return vbox;
    }

    public void setAccReservations(AccommodationDTO accommodation) {
        try {
            backButton.setVisible(true);
            backButton.setDisable(false);
            title.setText(accommodation.getName() + "'s reservations");
            title.setFont(Font.font("System", FontWeight.BOLD, 16));
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showAccReservations(accommodation, pageIndex);
                    } catch (BusinessException | RemoteException | NotBoundException e) {
                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        initialize();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }
    public Node showAccReservations(AccommodationDTO acc, int pageIndex) throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        PageDTO<ReservationDTO> pageDTO= admin.showAccReservations(acc);
        ListView l = new ListView<Label>();
        for(int i=pageIndex*5; i<(pageIndex+1)*5; i++){
            if(i< pageDTO.getEntries().size()){
                Label label = new Label();
                label.setWrapText(true);
                label.setText(pageDTO.getEntries().get(i).toString());
                label.setMaxWidth(pagination.getPrefWidth());
                int finalI = i;
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            deleteButton.setDisable(false);
                            deleteButton.setVisible(true);
                            deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    ReservationDTO res = pageDTO.getEntries().get(finalI);
                                    res.setAccommodationId(acc.getId());
                                    try{
                                        if(res.getstartDate().isAfter(LocalDate.now())){
                                            admin.deleteReservation(res);
                                            printAlert(Alert.AlertType.INFORMATION, "Reservation correctly deleted", ButtonType.OK);
                                            setAccReservations(acc);
                                        }
                                        else{
                                            printAlert(Alert.AlertType.WARNING, "This reservation already started, you can't delete it", ButtonType.OK);
                                        }
                                    }catch (BusinessException|RemoteException e){
                                        printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                    }

                                }
                            });
                        }
                    }});
                l.getItems().add(label);
            }
        }
        VBox vbox = new VBox(l);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        return vbox;
    }

}
