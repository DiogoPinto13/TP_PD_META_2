package isec.pd.meta2.Shared;

import java.io.Serializable;

public class RMIMulticastMessage implements Serializable {
    private final String serviceName;
    private final int registryPort;
    private final int databaseVersion;

    public RMIMulticastMessage(String newServiceName, int newRegistryPort, int newDatabaseVersion){
        serviceName = newServiceName;
        registryPort = newRegistryPort;
        databaseVersion = newDatabaseVersion;
    }
    public String getServiceName(){return serviceName;}
    public int getRegistryPort(){return registryPort;}
    public int getDatabaseVersion(){return databaseVersion;}
}
