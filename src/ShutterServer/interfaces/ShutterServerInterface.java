package ShutterServer.interfaces;

import de.thm.smarthome.global.beans.ActionModeBean;
import de.thm.smarthome.global.beans.ManufacturerBean;
import de.thm.smarthome.global.beans.ModelVariantBean;
import de.thm.smarthome.global.beans.PositionBean;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tim on 07.04.2017.
 */
public interface ShutterServerInterface extends Remote {

    void setDesiredPosition(PositionBean new_desiredPosition) throws RemoteException;
    void setGenericName(String new_genericName) throws RemoteException;
    PositionBean getCurrentPosition() throws RemoteException;
    PositionBean getDesiredPosition() throws RemoteException;
    ModelVariantBean getModelVariant() throws RemoteException;
    ManufacturerBean getManufacturer() throws RemoteException;
    ActionModeBean getActionMode() throws RemoteException;
    String getGenericName() throws RemoteException;
    String getSerialNumber() throws RemoteException;


    /*public void moveUp(ShutterClientInterface c) throws RemoteException;
    public void moveDown(ShutterClientInterface c) throws RemoteException;
    public boolean isUp(ShutterClientInterface c) throws RemoteException;
    public boolean isDown(ShutterClientInterface c) throws RemoteException;
    public String getName(ShutterClientInterface c) throws RemoteException;
    //public void update(AObservable o, Object change, ShutterClientInterface c);*/
}
