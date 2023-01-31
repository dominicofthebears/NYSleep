package it.unipi.lsmsd.nysleep.business.RMI;

import it.unipi.lsmsd.nysleep.business.*;
import it.unipi.lsmsd.nysleep.business.RMI.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ServerRegistry {
    public static Registry findRegistry(int port) throws RemoteException{
    Registry rmiRegistry;
    try {
        rmiRegistry = LocateRegistry.createRegistry(port);
        System.out.println("RMIregistry created - port "+Integer.toString(port));
        return rmiRegistry;
    }
    catch (RemoteException ex) {
        System.out.println(ex);
        // Retrieve existing registry.
        rmiRegistry = LocateRegistry.getRegistry(port);
        System.out.println("RMIregistry found - port "+Integer.toString(port));
        System.out.println(rmiRegistry.toString());
        return rmiRegistry;
    }

}

    public static void main(String [] args) {
        try {

            System.out.println("Server is booting....");

            AdminServices b1 = new AdminServices();
            CustomerServices b2 = new CustomerServices();
            RenterServices b3 = new RenterServices();
            UnregisteredUserServices b4 = new UnregisteredUserServices();
            UserServices b5 = new UserServices();



            Registry serverRegistry = findRegistry(1099);


            AdminServicesRMI stub1 = (AdminServicesRMI) UnicastRemoteObject.exportObject(b1, 0);
            CustomerServicesRMI stub2 = (CustomerServicesRMI) UnicastRemoteObject.exportObject(b2, 0);
            RenterServicesRMI stub3 = (RenterServicesRMI) UnicastRemoteObject.exportObject(b3, 0);
            UnregisteredUserServicesRMI stub4 = (UnregisteredUserServicesRMI) UnicastRemoteObject.exportObject(b4, 0);
            UserServicesRMI stub5 = (UserServicesRMI) UnicastRemoteObject.exportObject(b5, 0);

            serverRegistry.rebind("adminServices", stub1);
            serverRegistry.rebind("customerServices", stub2);
            serverRegistry.rebind("renterServices", stub3);
            serverRegistry.rebind("unregisteredUserServices", stub4);
            serverRegistry.rebind("userServices", stub5);



        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
