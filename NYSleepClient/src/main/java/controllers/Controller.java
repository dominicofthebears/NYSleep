package controllers;

import it.unipi.lsmsd.nysleep.DTO.RegisteredUserDTO;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public abstract class Controller {

    private static Registry registry;

    private Scene scene;

    private Stage stage;

    private Parent root;

    private RegisteredUserDTO loggedUser;



    static void printAlert(Alert.AlertType alertType, String message, ButtonType button){
        Alert alert = new Alert(alertType, message, button);
        alert.showAndWait();
    }

    public static void initializeRegistry(){
        try {
            registry = LocateRegistry.getRegistry(1099);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static Registry getRegistry() {
        return registry;
    }

    public static void setRegistry(Registry registry) {
        Controller.registry = registry;
    }

    public Stage getStage(){
        return this.stage;
    }


    public Scene getScene(){
        return this.scene;
    }

    public Parent getParent(){
        return this.root;
    }

    public  RegisteredUserDTO getLoggedUser(){
        return loggedUser;
    }


    public void setStage(Stage stage){
        this.stage = stage;
    }


    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void setParent(Parent root){
        this.root=root;
    }

    public void setLoggedUser(RegisteredUserDTO loggedIn){
        loggedUser = loggedIn;
    }
}