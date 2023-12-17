package isec.pd.meta2.BackupServer;

import isec.pd.meta2.Shared.RMI.RmiServerInterface;
import isec.pd.meta2.Shared.RMIMulticastMessage;

import java.io.*;
import java.net.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.InvalidParameterException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

class BackupServerThread extends Thread{
    private File localDirectory;
    private final int timeout = 30;
    private final int port = 4444;
    private final String ip = "230.44.44.44";
    private Object obj;
    private DatagramPacket pkt;
    private RMIMulticastMessage msg;
    private RmiClientService clientService;
    private RmiServerInterface serverInterface = null;
    private NetworkInterface nif;
    private MulticastSocket s;
    public static int currentDatabaseVersion = -1;
    private static final String databaseName = "backup.db";
    private final AtomicBoolean serverVariable;
    public BackupServerThread(File newLocalDirectory, AtomicBoolean newServerVariable) throws RemoteException, IOException {
        localDirectory = newLocalDirectory;
        serverVariable = newServerVariable;
        try {
            nif = NetworkInterface.getByInetAddress(InetAddress.getByName(ip)); //e.g., 127.0.0.1, 192.168.10.1, ...
        } catch (SocketException | NullPointerException | UnknownHostException | SecurityException ex) {
            nif = NetworkInterface.getByName("eth0"); //e.g., lo, eth0, wlan0, en0, ...
        }
        s = new MulticastSocket(port);
        s.joinGroup(new InetSocketAddress(ip, port), nif);
        s.setSoTimeout(timeout * 1000);
        String localFilePath = new File(localDirectory.getPath()+File.separator+databaseName).getCanonicalPath();
        clientService = new RmiClientService(localFilePath);
    }
    @Override
    public void run(){
        try {
            System.out.println("Socket successfully started.");
            while (serverVariable.get()) {
                pkt = new DatagramPacket(new byte[1000], 1000);
                s.receive(pkt);
                try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(pkt.getData(), 0, pkt.getLength()))) {
                    obj = in.readObject();
                    if (obj instanceof RMIMulticastMessage) {
                        msg = (RMIMulticastMessage) obj;
                        if(msg.getDatabaseVersion() != currentDatabaseVersion){
                            if(serverInterface == null){
                                serverInterface = (RmiServerInterface) LocateRegistry.getRegistry(msg.getRegistryPort()).lookup(msg.getServiceName());
                                try{
                                    clientService.setFout();
                                    serverInterface.getFile(clientService);
                                    if(!serverInterface.registerToServer(clientService))
                                        throw new RemoteException();
                                    clientService.closeFout();
                                }
                                catch(RemoteException e){
                                    System.out.println("Error while connecting to RMI Service.");
                                    break;
                                }
                                catch(IOException e){
                                    System.out.println("Error while downloading file.");
                                    break;
                                }
                                currentDatabaseVersion = msg.getDatabaseVersion();
                                System.out.println("First time Setup Complete.\nCommand:> ");
                            }
                            else{
                                System.out.println("Database versions don't match, closing app.");
                                break;
                            }
                        }
                    }
                }
                catch(NotBoundException ignored){
                    System.out.println("Heartbeat received. Registry name invalid.");
                }
                catch (ClassNotFoundException e) {
                    System.out.println("Mensagem recebida de tipo inesperado! " + e);
                }
                catch (IOException e) {
                    System.out.println("Impossibilidade de aceder ao conteudo da mensagem recebida! " + e);
                }
                catch (Exception e) {
                    System.out.println("Excepcao: " + e);
                    break;
                }
            }
        }
        catch (SocketTimeoutException e){
            System.out.println("Socket Timed out, Server isn't online.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if(serverInterface != null) {
            try {
                serverInterface.unregisterFromServer(clientService);
            } catch (RemoteException ignored) {

            }
        }
        s.close();
    }
}

public class BackupServer {
    public static void main(String args[]) throws IOException {
        if(args.length != 1){
            System.out.println("Sintaxe: java BackupServer localRootDirectory");
            return;
        }

        File localDirectory = new File(args[0].trim());

        if(!localDirectory.exists()){
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return;
        }

        if(!localDirectory.isDirectory()){
            System.out.println("O caminho " + localDirectory + " nao se refere a uma diretoria!");
            return;
        }

        if(!localDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na diretoria " + localDirectory + "!");
            return;
        }

        try (DirectoryStream<Path> directory = Files.newDirectoryStream(localDirectory.toPath())) {
            if(directory.iterator().hasNext()){
                System.out.println("A diretoria nao esta vazia.");
                return;
            }
        }

        AtomicBoolean serverVariable = new AtomicBoolean(true);
        BackupServerThread thread;
        try{
            thread = new BackupServerThread(localDirectory, serverVariable);
        } catch (RemoteException e){
            System.out.println("Error while setting up RMI Service.");
            return;
        } catch (IOException e){
            System.out.println("Error while setting up Multicast Socket.");
            return;
        }
        thread.start();
        Callable<String> scanner = () -> new Scanner(System.in).next();
        long currentAppTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<String> input;
        input = executor.submit(scanner);
        System.out.println("Command:> ");
        do{
            if(!thread.isAlive())
                break;
            if(input.isDone()){
                try{
                    if(input.get().equalsIgnoreCase("exit"))
                        break;
                    else
                        throw new IllegalArgumentException();
                } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
                    System.out.println("To exit type \"exit\".\nCommand:> ");
                    input = executor.submit(scanner);
                }
            }
        }while(true);
        serverVariable.set(false);
        System.out.println("\nWaiting for Thread to finish.");
        try{
            thread.join();
        } catch (InterruptedException ignored) {

        }
        System.out.println("Backup Server closing.");
        System.exit(0);
    }
}
