package isec.pd.meta2.BackupServer;

import isec.pd.meta2.Shared.RMI.RmiClientInterface;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClientService extends UnicastRemoteObject implements RmiClientInterface {
    FileOutputStream fout = null;
    String localFilePath;
    public RmiClientService(String newLocalfilepath) throws java.rmi.RemoteException{
        localFilePath = newLocalfilepath;
    }
    @Override
    public synchronized void setFout() throws FileNotFoundException {
        this.fout = new FileOutputStream(localFilePath);
    }

    @Override
    public void closeFout() throws RemoteException, IOException {
        if(fout != null){
            fout.close();
            System.out.println("File Downloaded.\nCommand:> ");
        }
    }

    @Override
    public void writeFileChunk(byte[] fileChunk, int nbytes) throws RemoteException, IOException {
        fout.write(fileChunk, 0, nbytes);
    }
    @Override
    public boolean checkDatabaseVersion(int databaseVersion) throws RemoteException, IOException {
        if(databaseVersion - 1 == BackupServerThread.currentDatabaseVersion){
            BackupServerThread.currentDatabaseVersion = databaseVersion;
            return true;
        }
        return false;
    }
}
