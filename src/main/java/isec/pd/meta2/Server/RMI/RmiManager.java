package isec.pd.meta2.Server.RMI;

import java.io.File;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.util.concurrent.atomic.AtomicBoolean;

public class RmiManager {
    private final RmiServerService RmiService;
    private final String registry;
    private final String rmiServiceName;
    private final int registryPort;
    private Thread rmiHeartBeatThread;

    public RmiManager(String newServiceName, File databaseLocation, int newRegistryPort, AtomicBoolean newServerVariable) throws java.rmi.RemoteException, SocketException {
        registryPort = newRegistryPort;
        rmiServiceName = newServiceName;
        registry = "rmi://localhost/" + rmiServiceName;
        RmiService = new RmiServerService(rmiServiceName, registryPort, databaseLocation, newServerVariable);
        LocateRegistry.createRegistry(registryPort);
    }

    public Thread getRmiHeartBeatThread() {return rmiHeartBeatThread;}

    public boolean registerService(){
        try{
            LocateRegistry.getRegistry(registryPort).rebind(registry, RmiService);
            Naming.rebind(registry, RmiService);
            rmiHeartBeatThread = new Thread(RmiService);
            rmiHeartBeatThread.start();
        }
        catch (MalformedURLException | java.rmi.RemoteException e) {

            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
