package ShutterServer;

import ShutterServer.interfaces.ShutterClientInterface;
import ShutterServer.interfaces.ShutterServerInterface;
import ShutterServer.observer.AObservable;
import ShutterServer.observer.IObserver;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;


/**
 * Created by Nils on 27.01.2017.
 */
public class Shutter extends AObservable implements IObserver, ShutterServerInterface{

    public String shuttername = null;
    public String serverstatus = null;
    public int serverport = 1099;
    public Registry rmiRegistry;
    private int currentPosition = 0;


    public Shutter() {
    }

    public void moveUp(ShutterClientInterface c) {
        if (currentPosition < 5){
            currentPosition++;
        }
    }
    public void moveDown(ShutterClientInterface c) {
        if (currentPosition > 0){
            currentPosition--;
        }
    }
    public boolean isUp(ShutterClientInterface c){
        if (currentPosition == 5){
            return true;
        }
    return false;
    }
    public boolean isDown(ShutterClientInterface c){
        if (currentPosition == 0){
            return true;
        }
        return false;
    }

    @Override
    public String getName(ShutterClientInterface c) {
        return shuttername;
    }


    /*Servermethoden*/
    public int getPosSrv(){
        return currentPosition;
    }

    public void moveUpSrv() {
        if (currentPosition < 5){
            currentPosition++;
        }
    }
    public void moveDownSrv() {
        if (currentPosition > 0){
            currentPosition--;
        }
    }
    public boolean isUpSrv(){
        if (currentPosition == 5){
            return true;
        }
        return false;
    }
    public boolean isDownSrv(){
        if (currentPosition == 0){
            return true;
        }
        return false;
    }

    public String getNameSrv() {
        return shuttername;
    }

    @Override
    public void update(AObservable o, Object change) {

    }

    public String getServerIP() {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();

        } catch (UnknownHostException e) {

            e.printStackTrace();
            return null;
        }
    }

    public String stopServer(){
        try {

            //Registry rmiRegistry = LocateRegistry.getRegistry("127.0.0.1", serverport);
            //HeizungServerInterface myService = (HeizungServerInterface) rmiRegistry.lookup(heizungname);

            rmiRegistry.unbind(shuttername);

            //UnicastRemoteObject.unexportObject(myService, true);
            UnicastRemoteObject.unexportObject(rmiRegistry, true);
            this.serverstatus = "Gestoppt";
            return "Server ist gestoppt!";

        } catch (NoSuchObjectException e)
        {
            e.printStackTrace();
            return "Fehler beim Stoppen des Servers!";
        } catch (NotBoundException e)
        {
            e.printStackTrace();
            return "Fehler beim Stoppen des Servers!";
        } catch (RemoteException e) {
            e.printStackTrace();
            return "Fehler beim Stoppen des Servers!";
        }
    }

    public String startServer(String shuttername) throws RemoteException {
        ShutterServerInterface stub = (ShutterServerInterface) UnicastRemoteObject.exportObject(this, 0);
        rmiRegistry = LocateRegistry.createRegistry(serverport);
        try {
            /*if (System.getSecurityManager() == null) {
                System.setProperty("java.security.policy", "file:C:\\Users\\Tim\\IdeaProjects\\HeizungServer\\out\\production\\HeizungServer\\HeizungServer\\server.policy");
                System.setSecurityManager(new SecurityManager());

            }*/
            /*Aktiviert und definiert das Logging des Servers*/
            RemoteServer.setLog(System.out);
            //System.out.println(srvlog.toString());
            /*Bindet den Server an die folgende Adresse*/
            Naming.rebind("//127.0.0.1/"+shuttername, this);
            this.shuttername = shuttername;
            this.serverstatus = "Gestartet";
            return "Server ist gestartet!";


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Fehler beim Starten des Servers!";
        }

    }
}
