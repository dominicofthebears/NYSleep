package it.unipi.lsmsd.nysleep.server;

import it.unipi.lsmsd.nysleep.business.AdminServices;
import it.unipi.lsmsd.nysleep.business.CustomerServices;
import it.unipi.lsmsd.nysleep.business.RenterServices;
import it.unipi.lsmsd.nysleep.business.UnregisteredUserServices;
import it.unipi.lsmsd.nysleep.business.UserServices;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ServerRegistry {

    public static void main(String [] args) {
        try {

            System.out.println("Server is booting....");
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            AdminServices b1 = new AdminServices();
            CustomerServices b2 = new CustomerServices();
            RenterServices b3 = new RenterServices();
            UnregisteredUserServices b4 = new UnregisteredUserServices();
            UserServices b5 = new UserServices();

            AdminServicesRMI stub1 = (AdminServicesRMI) UnicastRemoteObject.exportObject(b1, 0);
            CustomerServicesRMI stub2 = (CustomerServicesRMI) UnicastRemoteObject.exportObject(b2, 0);
            RenterServicesRMI stub3 = (RenterServicesRMI) UnicastRemoteObject.exportObject(b3, 0);
            UnregisteredUserServicesRMI stub4 = (UnregisteredUserServicesRMI) UnicastRemoteObject.exportObject(b4, 0);
            UserServicesRMI stub5 = (UserServicesRMI) UnicastRemoteObject.exportObject(b5, 0);

            Registry serverRegistry = LocateRegistry.getRegistry("127.0.0.1", 1090);

            serverRegistry.bind("adminServices", stub1);
            serverRegistry.bind("customerServices", stub2);
            serverRegistry.bind("renterServices", stub3);
            serverRegistry.bind("unregisteredUserServices", stub4);
            serverRegistry.bind("userServices", stub5);

        } catch (Exception e) {
            System.out.println("Server error" + e);

        }

    }
}
