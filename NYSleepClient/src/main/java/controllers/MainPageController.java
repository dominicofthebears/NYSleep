package controllers;

import it.unipi.lsmsd.nysleep.DTO.AccommodationDTO;
import it.unipi.lsmsd.nysleep.DTO.CustomerDTO;
import it.unipi.lsmsd.nysleep.DTO.PageDTO;
import it.unipi.lsmsd.nysleep.DTO.RenterDTO;
import it.unipi.lsmsd.nysleep.RMI.UserServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class MainPageController extends Controller {

    private ObservableList<Integer> people = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    @FXML
    private Pane mainPane;

    @FXML
    private ScrollPane pageScroll;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button loginButton;

    @FXML
    private TextField neighborhood;

    @FXML
    private TextField price;

    @FXML
    private ChoiceBox<Integer> numPeople;

    @FXML
    private Button registerButton;

    @FXML
    private Button searchButton;

    @FXML
    private Pagination pagination;

    @FXML
    private DatePicker startDate;

    @FXML
    private ContextMenu menu;

    @FXML
    private Button menuButton;

    @FXML
    private Button logoutButton;



    private EventHandler<WindowEvent> loggedUserHomepage() throws IOException {
        try {
            if (this.getLoggedUser() != null) {
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/mainPageRegistered.fxml"));
                setParent(loader2.load());
                MainPageController registered = loader2.getController();
                setScene(new Scene(getParent()));
                getStage().setScene(getScene());
                getStage().show();
                registered.setLoggedUser(getLoggedUser());
                registered.setStage(getStage());
                if (getLoggedUser().getType().equals("customer")) {
                    MenuItem item1 = new MenuItem("View your own reviews");
                    MenuItem item2 = new MenuItem("View your own reservations");
                    MenuItem item3 = new MenuItem("Modify your account informations");
                    MenuItem item4 = new MenuItem("View our suggested accommodations for you");
                    item1.setOnAction(t -> {
                        try {
                            customerMenuManager(t, 1);
                        } catch (BusinessException | NotBoundException | RemoteException e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    item2.setOnAction(t -> {
                        try {
                            customerMenuManager(t, 2);
                        } catch (BusinessException | NotBoundException | RemoteException e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    item3.setOnAction(t -> {
                        try {
                            customerMenuManager(t, 3);
                        } catch (BusinessException | NotBoundException | RemoteException e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    item4.setOnAction(t -> {
                        try {
                            customerMenuManager(t, 4);
                        } catch (BusinessException | NotBoundException | RemoteException e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    registered.menu.getItems().addAll(item1, item2, item3, item4);


                } else if (getLoggedUser().getType().equals("renter")) {
                    MenuItem item1 = new MenuItem("Insert a new accommodation");
                    MenuItem item2 = new MenuItem("View your own accommodations");
                    MenuItem item3 = new MenuItem("Modify your account informations");
                    MenuItem item4 = new MenuItem("View your reservations");
                    item1.setOnAction(t -> {
                        try {
                            renterMenuManager(t, 1);
                        } catch (Exception e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    item2.setOnAction(t -> {
                        try {
                            renterMenuManager(t, 2);
                        } catch (Exception e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    item3.setOnAction(t -> {
                        try {
                            renterMenuManager(t, 3);
                        } catch (Exception e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    item4.setOnAction(t -> {
                        try {
                            renterMenuManager(t, 4);
                        } catch (Exception e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    registered.menu.getItems().addAll(item1, item2, item3, item4);
                }

                else{
                    MenuItem item1 = new MenuItem("Modify your account informations");
                    MenuItem item2 = new MenuItem("View analytics");
                    item1.setOnAction(t -> {
                        try {
                            adminMenuManager(t, 1);
                        } catch (Exception e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });

                    item2.setOnAction(t -> {
                        try {
                            adminMenuManager(t, 2);
                        } catch (Exception e) {
                            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                        }
                    });
                    registered.menu.getItems().addAll(item1, item2);
                }
            }
            } catch(IOException e){
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                System.exit(-1);
            }
            return null;
        }

    private void adminMenuManager(ActionEvent t, int i) throws IOException {
        Stage stage = getStage();
        Stage s = new Stage();
        try{
            if(i == 1){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyInfoPage.fxml"));
                Parent root = loader.load();
                UserModifyController controller = loader.getController();
                Scene scene = new Scene(root);
                s.setScene(scene);
                controller.setScene(getScene());
                controller.setStage(s);
                controller.setLoggedUser(getLoggedUser());
                controller.cleanFields();
                s.showAndWait();
                this.setLoggedUser(controller.getLoggedUser());}

            else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/analyticsPage.fxml"));
                Parent root = loader.load();
                AnalyticsController controller = loader.getController();
                Scene scene = new Scene(root);
                s.setScene(scene);
                controller.setScene(getScene());
                controller.setStage(s);
                controller.setLoggedUser(getLoggedUser());
                s.show();}
        }catch (BusinessException|NotBoundException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);}
        }


    private void renterMenuManager(ActionEvent t, int i) {
        Stage stage = getStage();
        Stage s = new Stage();
        try {
            if(i == 4 || i == 2){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/listPage.fxml"));
                Parent root = loader.load();
                ListPageController controller = loader.getController();
                Scene scene = new Scene(root);
                s.setScene(scene);
                controller.setScene(scene);
                controller.setStage(s);
                switch (i){
                    case 2:
                        stage.setTitle("Your accommodations");
                        controller.setLoggedUser(getLoggedUser());
                        controller.setRentAccList((RenterDTO) getLoggedUser());
                        break;

                    case 4:
                        stage.setTitle("Your reservations");
                        controller.setLoggedUser(getLoggedUser());
                        controller.setRentResList((RenterDTO) getLoggedUser());
                        break;
                }
                s.show();

            }
            if(i == 3){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyInfoPage.fxml"));
                Parent root = loader.load();
                UserModifyController controller = loader.getController();
                Scene scene = new Scene(root);
                s.setScene(scene);
                controller.setScene(getScene());
                controller.setStage(s);
                controller.setLoggedUser(getLoggedUser());
                controller.cleanFields();
                s.showAndWait();
                this.setLoggedUser(controller.getLoggedUser());
            }

            if(i == 1){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/insertAccommodation.fxml"));
                Parent root = loader.load();
                InsertAccommodationController controller = loader.getController();
                controller.setLoggedUser(getLoggedUser());
                Scene scene = new Scene(root);
                s.setScene(scene);
                controller.setScene(getScene());
                controller.setStage(s);
                s.show();
            }

        }catch (IOException| BusinessException| NotBoundException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }




    @FXML
    private void loginUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginPage.fxml"));
            Parent root = loader.load();
            LoginController log = loader.getController();
            Stage s = new Stage();
            s.setTitle("Login");
            Scene scene = new Scene(root);
            s.setScene(scene);
            s.showAndWait();
            this.setLoggedUser(log.getLoggedUser());
            s.setOnHidden(loggedUserHomepage());


        }catch (IOException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    @FXML
    private void registerUser(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/registration.fxml"));
            Parent root = loader.load();
            RegistrationController reg = loader.getController();
            Stage s = new Stage();
            s.setTitle("Registration");
            Scene scene = new Scene(root);
            s.setScene(scene);
            s.showAndWait();
            this.setLoggedUser(reg.getLoggedUser());
            s.setOnHidden(loggedUserHomepage());
        } catch (IOException e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    @FXML
    private void logoutUser(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainPageUnregistered.fxml"));
            Parent root = loader.load();
            setParent(root);
            MainPageController unregistered = loader.getController();
            setScene(new Scene(getParent()));
            Stage stage=this.getStage();
            stage.setScene(getScene());
            stage.show();
            unregistered.setLoggedUser(null);
            unregistered.setStage(getStage());
            printAlert(Alert.AlertType.INFORMATION, "Logout successful", ButtonType.OK);
        }catch (IOException e) {
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            System.exit(-1);
        }
    }

    @FXML
    private void searchAccommodation(ActionEvent event) {
        try {
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return showSearchedAccommodations(pageIndex);
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


    @FXML
    public void initialize() {
        try {

            numPeople.setItems(people);
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    try {
                        return createPage(pageIndex);
                    } catch (BusinessException|RemoteException e) {
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

    private Node createPage(Integer pageIndex) throws BusinessException, RemoteException {
        try {
            UserServicesRMI usr = (UserServicesRMI) getRegistry().lookup("userServices");
            ListView listView = new ListView();

            PageDTO<AccommodationDTO> accs = usr.showHomePage((pageIndex) * 10, 10);

            for (int i = 0; i < 10; i++) {
                if (i < accs.getEntries().size()) {
                    Label label = new Label();
                    label.setWrapText(true);
                    label.setText(accs.getEntries().get(i).toString());
                    label.setMaxWidth(pagination.getPrefWidth());
                    int finalI = i;
                    Image image = new Image("/house.jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(100);
                    label.setGraphic(imageView);
                    label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                                if (mouseEvent.getClickCount() == 2) {
                                    try {
                                        AccommodationDTO acc = accs.getEntries().get(finalI);

                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/accommodationPage.fxml"));
                                        Parent root = loader.load();
                                        Stage s = new Stage();
                                        s.setTitle("Accommodation");
                                        Scene scene = new Scene(root);
                                        s.setScene(scene);
                                        AccommodationPageController controller = loader.getController();
                                        controller.setLoggedUser(getLoggedUser());

                                        try{
                                            controller.setAccommodation(acc);
                                        }catch (BusinessException e){
                                            printAlert(Alert.AlertType.WARNING, "Accommodation has been deleted, reloading page", ButtonType.OK);
                                            initialize();
                                            return;
                                        }
                                        controller.setStage(s);
                                        s.show();
                                    } catch (IOException | NotBoundException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            }
                        }
                    });
                    listView.getItems().add(label);
                }
            }
        VBox vbox = new VBox(listView);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        return vbox;
    }catch (Exception e) {
        throw new BusinessException(e);
    }}

    private Node showSearchedAccommodations(Integer pageIndex) throws NotBoundException, RemoteException, BusinessException {
        UserServicesRMI usr = (UserServicesRMI) getRegistry().lookup("userServices");
        double priceValue;
        int people;
        ListView listView = new ListView();

        if (price.getText().isEmpty()) {
            priceValue = 0;
        } else {
            priceValue = Double.parseDouble(price.getText());
        }

        if (numPeople.getValue() == null) {
            people = 0;
        } else {
            people = numPeople.getValue();
        }

        PageDTO<AccommodationDTO> accs = usr.showSearchAcc(startDate.getValue(), endDate.getValue(), people, neighborhood.getText(),
                priceValue, (pageIndex) * 10, 10);
        for (int i = 0; i < 10; i++) {
            if (i < accs.getEntries().size()) {
                Label label = new Label();
                label.setWrapText(true);
                label.setText(accs.getEntries().get(i).toString());
                label.setMaxWidth(pagination.getPrefWidth());
                int finalI = i;
                Image image = new Image("/house.jpg");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(150);
                imageView.setFitHeight(100);
                label.setGraphic(imageView);
                label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 2) {
                                try {
                                    AccommodationDTO acc = accs.getEntries().get(finalI);
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/accommodationPage.fxml"));
                                    Parent root = loader.load();
                                    Stage s = new Stage();
                                    s.setTitle("Accommodation");
                                    Scene scene = new Scene(root);
                                    s.setScene(scene);
                                    AccommodationPageController controller = loader.getController();
                                    controller.setLoggedUser(getLoggedUser());

                                    try {
                                        controller.setAccommodation(acc);
                                    } catch (BusinessException e) {
                                        printAlert(Alert.AlertType.WARNING, "Accommodation has been deleted, reloading page", ButtonType.OK);
                                        searchAccommodation(null);
                                        return;
                                    }
                                    controller.setStage(s);
                                    s.show();
                                } catch (IOException | NotBoundException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    }
                });
                listView.getItems().add(label);
            }
        }
        VBox vbox = new VBox(listView);
        vbox.getChildren().forEach(child -> VBox.setVgrow(child, Priority.ALWAYS));
        return vbox;
    }

    public void showMenu(MouseEvent actionEvent) throws NotBoundException, RemoteException {
            menu.show(menuButton, actionEvent.getScreenX(), actionEvent.getScreenY());
        }


    private void customerMenuManager(ActionEvent t, int i) throws NotBoundException, RemoteException, BusinessException {
        Stage stage=getStage();
        Stage s = new Stage();
        try {
            if(i == 1 || i == 2){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/listPage.fxml"));
                Parent root = loader.load();
                ListPageController controller = loader.getController();
                Scene scene = new Scene(root);
                s.setScene(scene);
                controller.setScene(getScene());
                controller.setStage(s);
                switch (i){
                    case 1:
                        stage.setTitle("Your reviews");
                        controller.setLoggedUser(getLoggedUser());
                        controller.setCusRevList((CustomerDTO) getLoggedUser());
                        break;

                    case 2:
                        stage.setTitle("Your reservations");
                        controller.setLoggedUser(getLoggedUser());
                        controller.setCusResList((CustomerDTO) getLoggedUser());
                        break;
                }
                s.show();

            }
            if(i == 3){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyInfoPage.fxml"));
                Parent root = loader.load();
                UserModifyController controller = loader.getController();
                Scene scene = new Scene(root);
                s.setScene(scene);
                controller.setScene(getScene());
                controller.setStage(s);
                controller.setLoggedUser(getLoggedUser());
                controller.cleanFields();
                s.showAndWait();
                this.setLoggedUser(controller.getLoggedUser());
            }

            if(i == 4){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/customerSuggestions.fxml"));
                Parent root = loader.load();
                SuggestionsController controller = loader.getController();
                controller.setLoggedUser(getLoggedUser());
                Scene scene = new Scene(root);
                stage.setScene(scene);
                controller.setScene(getScene());
                controller.setStage(s);
            }

        }catch (BusinessException | IOException e){
            printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        }
    }
    }

