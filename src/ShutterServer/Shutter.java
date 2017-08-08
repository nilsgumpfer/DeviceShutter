package ShutterServer;

import ShutterServer.interfaces.ShutterServerInterface;

import de.thm.smarthome.global.beans.*;
import de.thm.smarthome.global.enumeration.EPosition;
import de.thm.smarthome.global.observer.AObservable;

import de.thm.smarthome.global.observer.IObserver;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

   /*Attribute/Beans*/

    private PositionBean currentPosition = new PositionBean(EPosition.P0_DOWN);
    private PositionBean desiredPosition = new PositionBean(EPosition.P0_DOWN);
    private ModelVariantBean modelVariant;
    private ManufacturerBean manufacturer;
    private ActionModeBean actionModeBean;
    private String serverIP;
    private ShutterServerInterface stub = null;


    /*Variable*/
    public String genericName = null;
    private String serialNumber = null;

    public String shuttername = "SmartHomeAPI";
    public String serverstatus = null;
    public int serverport = 1099;
    public Registry rmiRegistry;



    public StringProperty ShutterPosition = new SimpleStringProperty(String.valueOf(currentPosition.getPosition_Int()));
    public StringProperty desiredShutterPosition = new SimpleStringProperty(String.valueOf(desiredPosition.getPosition_Int()));


    public Shutter() {
    }


    @Override
    public PositionBean getCurrentPosition() throws RemoteException{

        return currentPosition;
    }

    @Override
    public PositionBean getDesiredPosition() throws RemoteException{

        return desiredPosition;
    }

    @Override
    public ModelVariantBean getModelVariant() throws RemoteException{

        return modelVariant;
    }

    @Override
    public ManufacturerBean getManufacturer() throws RemoteException{

        return manufacturer;
    }

    @Override
    public ActionModeBean getActionMode() throws RemoteException{
        return actionModeBean;
    }

    @Override
    public String getGenericName()  throws RemoteException{

        return this.genericName;
    }

    @Override
    public String getSerialNumber()  throws RemoteException{

        return this.serialNumber;
    }


    @Override
    public void setDesiredPosition(PositionBean new_desiredPosition)throws RemoteException{
        desiredPosition = new_desiredPosition;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                desiredShutterPosition.set(String.valueOf(desiredPosition.getPosition_Int()));
            }
        });


        if(desiredPosition.getPosition_Int() < currentPosition.getPosition_Int()){
            herunterfahren();
        }
        else if (desiredPosition.getPosition_Int() > currentPosition.getPosition_Int()){
            hochfahren();

    }}


    @Override
    public void setGenericName(String genericName)throws RemoteException{
        this.genericName = genericName;
    }

    private void hochfahren(){

        Thread hochfahren = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = currentPosition.getPosition_Int(); currentPosition.getPosition_Int() < desiredPosition.getPosition_Int(); i++) {

                    PositionBean new_currentPosition = new PositionBean(i);
                    //currentPosition = new_currentPosition;
                    setCurrentPosition(new_currentPosition);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.print(e.toString());
                    }
                }
            }

        });
        hochfahren.start();
    }

    private void setCurrentPosition(PositionBean new_currentPosition) {
        currentPosition = new_currentPosition;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ShutterPosition.set(String.valueOf(currentPosition.getPosition_Int()));
            }
        });

    }

    private void herunterfahren(){

        Thread herunterfahren = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = currentPosition.getPosition_Int(); currentPosition.getPosition_Int() > desiredPosition.getPosition_Int(); i--) {

                    PositionBean new_currentPosition = new PositionBean(i);
                    //currentPosition = new_currentPosition;
                    setCurrentPosition(new_currentPosition);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.print(e.toString());
                    }
                }
            }

        });
        herunterfahren.start();
    }

   /*Servermethoden*/

    public String getNameSrv() {
        return shuttername;
    }

    @Override
    public void update(Object o, Object change) {

    }

    public String getServerIP() {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();

        } catch (UnknownHostException e) {

            System.out.print(e.toString());
            return "0.0.0.0";
        }
    }

    public String stopServer(){
        try {
            rmiRegistry.unbind(shuttername);

            UnicastRemoteObject.unexportObject(rmiRegistry, true);
            this.serverstatus = "Gestoppt";
            return "Server ist gestoppt!";

        } catch (NoSuchObjectException e) {
            System.out.print(e.toString());
            return "Fehler beim Stoppen des Servers!";
        }
            catch (NotBoundException e)
        {
            System.out.print(e.toString());
            return "Fehler beim Stoppen des Servers!";
        }

        catch (RemoteException e) {
            System.out.print(e.toString());
            return "Fehler beim Stoppen des Servers!";
        }
    }


    public String startServer() throws RemoteException {
        serverIP = getServerIP();
        System.setProperty("java.rmi.server.hostname", serverIP);

        if(stub == null){
        stub = (ShutterServerInterface) UnicastRemoteObject.exportObject(this, 0);}

        rmiRegistry = LocateRegistry.createRegistry(serverport);

        try {
            /*Aktiviert und definiert das Logging des Servers*/
            RemoteServer.setLog(System.out);


            /*Bindet den Server an die folgende Adresse*/
            Naming.rebind("//127.0.0.1/"+shuttername, this);
            this.serverstatus = "Gestartet";
            return "Server ist gestartet!";


        } catch (MalformedURLException e) {
            System.out.print(e.toString());
            return "Fehler beim Starten des Servers!";
        }

    }
}
