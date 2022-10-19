package ar.edu.itba.pod.server;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
//    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        System.out.println("tpe2-g10 Server Starting ...");
        try {
            final Registry registry = LocateRegistry.getRegistry(1099);
            System.out.println("server online");
        } catch (Exception ex ) {
            System.out.println("An exception happened");
            ex.printStackTrace();
        }
    }
}
