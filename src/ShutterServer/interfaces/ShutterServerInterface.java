package ShutterServer.interfaces;

import ShutterServer.observer.AObservable;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Tim on 07.04.2017.
 */
public interface ShutterServerInterface extends Remote {
    public void moveUp(ShutterClientInterface c) throws RemoteException;
    public void moveDown(ShutterClientInterface c) throws RemoteException;
    public boolean isUp(ShutterClientInterface c) throws RemoteException;
    public boolean isDown(ShutterClientInterface c) throws RemoteException;
    public String getName(ShutterClientInterface c) throws RemoteException;
    //public void update(AObservable o, Object change, ShutterClientInterface c);
}
