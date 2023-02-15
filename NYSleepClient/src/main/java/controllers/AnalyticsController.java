package controllers;

import com.mongodb.client.DistinctIterable;
import it.unipi.lsmsd.nysleep.RMI.AdminServicesRMI;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.bson.Document;
import org.neo4j.driver.Record;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsController extends Controller{


    @FXML
    private Label Label;

    @FXML
    private Pane MainPane;

    @FXML
    private AnchorPane TopSide;


    @FXML
    private MenuButton choiceMenu;

    @FXML
    private VBox resultsVbox;

    @FXML
    public void initialize(){
        choiceMenu.getItems().clear();
        MenuItem item1 = new MenuItem("Customer who has spent the most");
        MenuItem item2 = new MenuItem("Most reserved accommodation for each neighborhood");
        MenuItem item3 = new MenuItem("Most reserving country for neighborhood");
        MenuItem item4 = new MenuItem("Most and least expensive accommodation for property type");
        MenuItem item5 = new MenuItem("Average rating by country");
        MenuItem item6 = new MenuItem("Most active user");
        MenuItem item7 = new MenuItem("Renter with most accommodations");
        MenuItem item8 = new MenuItem("Customer with highest average expense");
        MenuItem item9 = new MenuItem("Best reviewed renter");
        MenuItem item10 = new MenuItem("Renter with most accommodation for neighborhood");
        MenuItem item11 = new MenuItem("Neighborhood rented by most number of countries");
        MenuItem item12 = new MenuItem("Most reserved accommodation for season");
        item1.setOnAction(t -> {
            try {
                choiceMenu.setText("Customer who has spent the most");
                customerWhoSpentTheMostAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item2.setOnAction(t -> {
            try {
                choiceMenu.setText("Most reserved accommodation for each neighborhood");
                mostReservedAccommodationForEachNeighborhoodAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item3.setOnAction(t -> {
            try {
                choiceMenu.setText("Most reserving country for neighborhood");
                mostReservingCountryForEachNeighborhoodAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item4.setOnAction(t -> {
            try {
                choiceMenu.setText("Most and least expensive accommodation for property type");
                mostAndLeastExpensiveAccommodationForPropertyTypeAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item5.setOnAction(t -> {
            try {
                choiceMenu.setText("Average rating by country");
                averageRatingByCountryAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });
        item6.setOnAction(t -> {
            try {
                choiceMenu.setText("Most active user");
                mostActiveUserAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item7.setOnAction(t -> {
            try {
                choiceMenu.setText("Renter with most accommodations");
                renterWithMostAccommodationsAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item8.setOnAction(t -> {
            try {
                choiceMenu.setText("Customer with highest average expense");
                customerWithHighestAverageExpenseAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item9.setOnAction(t -> {
            try {
                choiceMenu.setText("Best reviewed renter");
                bestReviewedRenterAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item10.setOnAction(t -> {
            try {
               choiceMenu.setText("Renter with most accommodation for neighborhood");
                renterWithMostAccommodationForNeighborhoodAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item11.setOnAction(t -> {
            try {
                choiceMenu.setText("Neighborhood rented by most number of countries");
                neighborhoodRentedByMostCountriesAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        item12.setOnAction(t -> {
            try {
                choiceMenu.setText("Most reserved accommodation for season");
                mostReservedForSeasonAnalytic();
            } catch (BusinessException | NotBoundException | RemoteException e) {
                printAlert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            }
        });

        choiceMenu.getItems().addAll(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12);
    }

    private void bestReviewedRenterAnalytic() throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        int min = 10;
        Map<String, Object> r = admin.bestReviewedRenter(min);
        ListView listView = new ListView();
        Label label = new Label();
        Text custText= new Text();
        custText.setText("Renter: "+r.get("first_name")+" "+r.get("last_name"));
        custText.setFont(Font.font("System", FontWeight.BOLD, 16));
        Text infoText = new Text();
        infoText.setText("Average rating: "+r.get("avg_rate") +"\n" +
                "Minimum reviews: " + min);
        label.setGraphic(new VBox(custText, infoText));
        listView.getItems().add(label);
        resultsVbox.getChildren().add(listView);
    }

    private void customerWithHighestAverageExpenseAnalytic() throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        Document doc = admin.customerWithHighestAverageExpense();
        ListView listView = new ListView();
        Label label = new Label();
        Text custText= new Text();
        Document nested = (Document) doc.get("_id");
        custText.setText("Customer: "+ nested.get("first_name")+" "+ nested.get("last_name"));
        custText.setFont(Font.font("System", FontWeight.BOLD, 16));
        Text infoText = new Text();
        infoText.setText("From: " + (nested.get("country")) + "\n" +
                "Num reservations: "+doc.get("num_res") + "\n" +
                "Average expense: " + doc.get("avg_cost"));
        label.setGraphic(new VBox(custText, infoText));
        listView.getItems().add(label);
        resultsVbox.getChildren().add(listView);
    }

    private void renterWithMostAccommodationsAnalytic() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        Map<String, Object> r = admin.renterWithMostAccommodation();
        ListView listView = new ListView();
        Label label = new Label();
        Text custText= new Text();
        custText.setText("Renter: "+r.get("first_name")+" "+r.get("last_name"));
        custText.setFont(Font.font("System", FontWeight.BOLD, 16));
        Text infoText = new Text();
        infoText.setText("Num accommodations: "+r.get("num_accommodations"));
        label.setGraphic(new VBox(custText, infoText));
        listView.getItems().add(label);
        resultsVbox.getChildren().add(listView);
    }

    private void mostActiveUserAnalytic() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        Map<String, Object> r = admin.mostActiveUser();
        ListView listView = new ListView();
        Label label = new Label();
        Text custText= new Text();
        custText.setText("User: "+r.get("first_name")+" "+r.get("last_name"));
        custText.setFont(Font.font("System", FontWeight.BOLD, 16));
        Text infoText = new Text();
        infoText.setText("Num Reviews: "+r.get("num_reviews"));
        label.setGraphic(new VBox(custText, infoText));
        listView.getItems().add(label);
        resultsVbox.getChildren().add(listView);
    }

    private void averageRatingByCountryAnalytic() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        List<Document> results = admin.averageRatingByCountry();
        ListView listView = new ListView();
        for(Document doc : results){
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(resultsVbox.getPrefWidth());
            Document country =(Document) doc.get("_id");
            Text countryText = new Text();
            countryText.setText("Country: "+country.get("country"));
            countryText.setFont(Font.font("System", FontWeight.BOLD, 16));
            Text ratingText = new Text();
            ratingText.setText("Average rate: "+doc.get("average_rate"));
            label.setGraphic(new VBox(countryText, ratingText));
            listView.getItems().add(label);
        }
        resultsVbox.getChildren().add(listView);
    }

    private void mostAndLeastExpensiveAccommodationForPropertyTypeAnalytic() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        List<Document> results = admin.mostAndLeastExpensiveAccommodationForPropertyType();
        ListView listView = new ListView();
        for(Document doc : results){
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(resultsVbox.getPrefWidth());
            Text typeText = new Text();
            typeText.setText("Type: "+doc.get("_id"));
            typeText.setFont(Font.font("System", FontWeight.BOLD, 16));
            Text mostExpText = new Text();
            mostExpText.setText("Most expensive: "+doc.get("most_expensive_name")+"  neigborhood: "+
                    doc.get("most_expensive_neigh")+"  cost: "+doc.get("highest_cost"));
            Text leastExpText = new Text();
            leastExpText.setText("Least expensive: "+doc.get("least_expensive")+"  neigborhood: "+
                    doc.get("least_expensive_neigh")+"  cost: "+doc.get("lowest_cost"));
            label.setGraphic(new VBox(typeText, mostExpText, leastExpText));
            listView.getItems().add(label);
        }
        resultsVbox.getChildren().add(listView);
    }

    private void renterWithMostAccommodationForNeighborhoodAnalytic() throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        List<String> neighborhoods = admin.getNeighborhoods();

        Pagination p = new Pagination();
        p.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                ListView listView = new ListView();
                for(int i = 10 * pageIndex; i < 10 * (pageIndex + 1); i++){
                    Map<String, Object> r;
                    try {
                        r = admin.renterWithMostAccommodationForNeighborhood(neighborhoods.get(i));
                    } catch (BusinessException |RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    Label label = new Label();
                    label.setWrapText(true);
                    label.setMaxWidth(resultsVbox.getPrefWidth());
                    Text neighText = new Text();
                    neighText.setText("Neighborhood: " + neighborhoods.get(i));
                    neighText.setFont(Font.font("System", FontWeight.BOLD, 16));
                    Text infoText = new Text();
                    infoText.setText("First name: " + r.get("first_name")+'\n'
                            +"Last name: " + r.get("last_name") + "\n"
                            +"Number of accommodations: " + r.get("num_accommodations_neighborhood"));
                    label.setGraphic(new VBox(neighText, infoText));
                    listView.getItems().add(label);
                }
                return listView;
            }
        });
        resultsVbox.getChildren().clear();
        resultsVbox.getChildren().add(p);
    }

    private void mostReservingCountryForEachNeighborhoodAnalytic() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        List<Document> results = admin.mostReservingCountryForNeighborhood();
        ListView listView = new ListView();
        for(Document doc : results){
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(resultsVbox.getPrefWidth());
            Text neighText = new Text();
            neighText.setText("Neighborhood: " + doc.get("_id"));
            neighText.setFont(Font.font("System", FontWeight.BOLD, 16));
            Text infoText = new Text();
            infoText.setText("Country: " + doc.get("most_res_country")+'\n'
                    +"Num Reservation: " + doc.get("num_reservation"));
            label.setGraphic(new VBox(neighText, infoText));
            listView.getItems().add(label);
        }
        resultsVbox.getChildren().add(listView);

    }

    private void neighborhoodRentedByMostCountriesAnalytic() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        Document doc = admin.neighborhoodRentedByMostNumberOfCountries();
        Document nested = (Document) doc.get("_id");
        ListView listView = new ListView();
        Label label = new Label();
        label.setWrapText(true);
        label.setMaxWidth(resultsVbox.getPrefWidth());
        Text titleText = new Text("Neighborhood rented by most countries");
        titleText.setFont(Font.font("System", FontWeight.BOLD, 16));
        Text infoText = new Text("Neighborhood: " + nested.get("neighborhood") + "\n"
        + "Number of countries: " + doc.get("num_countries"));
        label.setGraphic(new VBox(titleText, infoText));
        listView.getItems().add(label);
        resultsVbox.getChildren().add(listView);
    }

    private void mostReservedAccommodationForEachNeighborhoodAnalytic() throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        List<Document> results = admin.mostReservedAccommodationForEachNeighborhood();
        ListView listView = new ListView();
        for(Document doc : results){
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(resultsVbox.getPrefWidth());
            Text neighText = new Text();
            neighText.setText("Neighborhood: " + doc.get("_id"));
            neighText.setFont(Font.font("System", FontWeight.BOLD, 16));
            Text infoText = new Text();
            infoText.setText("Accommodation: " + doc.get("most_res_acc")+'\n'
                    +"Num Reservation: " + doc.get("num_reservation"));
            label.setGraphic(new VBox(neighText, infoText));
            listView.getItems().add(label);
        }
        resultsVbox.getChildren().add(listView);
    }

    private void customerWhoSpentTheMostAnalytic() throws NotBoundException, RemoteException, BusinessException{
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        Document result = admin.customerWhoHasSpentTheMost();
        Document customer =(Document) result.get("_id");
        ListView listView = new ListView();
        Label label = new Label();
        label.setWrapText(true);
        label.setMaxWidth(resultsVbox.getPrefWidth());
        Text custText = new Text();
        custText.setText("Customer: "+customer.get("first_name")+" "+customer.get("last_name"));
        custText.setFont(Font.font("System", FontWeight.BOLD, 16));
        Text infoText = new Text();
        infoText.setText("Spent: "+result.get("total_spent"));
        label.setGraphic(new VBox(custText, infoText));
        listView.getItems().add(label);
        resultsVbox.getChildren().add(listView);

    }

    private void mostReservedForSeasonAnalytic() throws NotBoundException, RemoteException, BusinessException {
        Registry registry = getRegistry();
        AdminServicesRMI admin = (AdminServicesRMI) registry.lookup("adminServices");
        resultsVbox.getChildren().clear();
        List<Document> results = admin.mostReservedAccommodationForSeason();
        ListView listView = new ListView();
        for (int i = 0; i<results.size(); i++){
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(resultsVbox.getPrefWidth());
            Document doc = results.get(i);
            Document nested = (Document) ((ArrayList) doc.get("accommodation")).get(0);

            Text infoText = new Text();
            Text seasonText = new Text();
            infoText.setText("Name: " + nested.get("name") + "\n" +
                    "Neighborhood: " + nested.get("neighborhood") + "\n" +
                    "Number of reservations: " + doc.get("num_res") + "\n");
            switch (i){
                case 0:
                    seasonText.setText("Winter most reserved accommodation");
                    seasonText.setFont(Font.font("System", FontWeight.BOLD, 16));
                    break;

                case 1:
                    seasonText.setText("Spring most reserved accommodation");
                    seasonText.setFont(Font.font("System", FontWeight.BOLD, 16));
                    break;

                case 2:
                    seasonText.setText("Summer most reserved accommodation");
                    seasonText.setFont(Font.font("System", FontWeight.BOLD, 16));
                    break;

                case 3:
                    seasonText.setText("Fall most reserved accommodation");
                    seasonText.setFont(Font.font("System", FontWeight.BOLD, 16));
                    break;
            }
            label.setGraphic(new VBox(seasonText, infoText));
            listView.getItems().add(label);

        }

        resultsVbox.getChildren().add(listView);
    }

}