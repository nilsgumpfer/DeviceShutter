package ShutterServer;

import ShutterServer.interfaces.ShutterServerInterface;
import ShutterServer.observer.AObservable;
import ShutterServer.observer.IObserver;
import de.thm.smarthome.global.beans.*;
import de.thm.smarthome.global.enumeration.EDeviceManufacturer;
import de.thm.smarthome.global.enumeration.EModelVariant;
import de.thm.smarthome.global.enumeration.EPosition;
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

    private PositionBean currentPosition = new PositionBean(EPosition.P0);
    private PositionBean desiredPosition = new PositionBean(EPosition.P0);
    private ModelVariantBean modelVariant;
    private ManufacturerBean manufacturer;
    private ActionModeBean actionModeBean;


    /*Variable*/
    public String genericName = null;
    private String serialNumber = null;

    public String shuttername = null;
    public String serverstatus = null;
    public int serverport = 1099;
    public Registry rmiRegistry;
    /*private int currentPosition = 0;*/


    public StringProperty ShutterPosition = new SimpleStringProperty(String.valueOf(currentPosition.getPosition_Int()));
    public StringProperty desiredShutterPosition = new SimpleStringProperty(String.valueOf(desiredPosition.getPosition_Int()));


    public Shutter() {
    }

    //TO DO:Tim fragen wegen move up; currentPosition ist eine PositionBean
   /* public void moveUp(ShutterClientInterface c) {
        if (currentPosition < 5){
            currentPosition++;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ShutterPosition.set(String.valueOf(currentPosition));
                }
            });

            notifyObservers(this.currentPosition);
        }
    }


    public void moveDown(ShutterClientInterface c) {
        if (currentPosition > 0){
            currentPosition--;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ShutterPosition.set(String.valueOf(currentPosition));
                }
            });

            notifyObservers(this.currentPosition);
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
    }*/


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

   /* @Override
    public String getName(ShutterClientInterface c) throws RemoteException {
        return shuttername;
    }

    @Override
    public void setGenericName(String genericName)throws RemoteException{
        this.genericName = genericName;
    }*/

    //TO DO: sicher so fertig?! und was muss desiredPosition für ein Datentyp sein

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

    /*private void setCurrentPositionPosition(PositionBean new_currentPosition)throws RemoteException{
        desiredPosition = new_currentPosition;
    }*/

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
                    }
                }
            }

        });
        herunterfahren.start();
    }

   /*Servermethoden*/
   /*
   public int getPosSrv(){
        return currentPosition;
    }

    public void moveUpSrv() {
        if (currentPosition < 5){
            currentPosition++;
            ShutterPosition.set(String.valueOf(currentPosition));
        }
    }
    public void moveDownSrv() {
        if (currentPosition > 0){
            currentPosition--;

            ShutterPosition.set(String.valueOf(currentPosition));
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
    }*/

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
