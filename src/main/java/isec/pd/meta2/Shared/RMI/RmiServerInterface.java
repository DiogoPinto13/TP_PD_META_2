package isec.pd.meta2.Shared.RMI;

import java.io.IOException;
import java.rmi.RemoteException;

public interface RmiServerInterface extends java.rmi.Remote {
    //functions
    void getFile(RmiClientInterface cliRef) throws IOException, RemoteException;
    boolean registerToServer(RmiClientInterface clientInterface) throws RemoteException;
    boolean unregisterFromServer(RmiClientInterface clientInterface) throws RemoteException;
}
