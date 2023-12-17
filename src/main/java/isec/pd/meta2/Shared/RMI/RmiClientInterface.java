package isec.pd.meta2.Shared.RMI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

public interface RmiClientInterface extends java.rmi.Remote {
    void writeFileChunk(byte [] fileChunk, int nbytes) throws FileNotFoundException, java.rmi.RemoteException, java.io.IOException;
    void setFout() throws RemoteException, IOException;
    void closeFout() throws RemoteException, IOException;
    boolean checkDatabaseVersion(int databaseVersion) throws java.rmi.RemoteException, java.io.IOException;
}
