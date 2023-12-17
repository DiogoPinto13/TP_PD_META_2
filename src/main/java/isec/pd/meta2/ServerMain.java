package isec.pd.meta2;

import isec.pd.meta2.Server.DatabaseManager;
import isec.pd.meta2.Server.EventManager;
import isec.pd.meta2.Server.UserManager;
import isec.pd.meta2.Shared.*;
import isec.pd.meta2.Server.RMI.RmiManager;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


//wait for clients
class WaitClient extends Thread{
    private final int port;
    private final AtomicBoolean serverVariable;
    public WaitClient(int port, AtomicBoolean newServerVariable){
        this.port = port;
        serverVariable = newServerVariable;
    }
    public void run(){

        try(ServerSocket socket = new ServerSocket(port)){
            System.out.println("Main server online in port: "+ socket.getLocalPort());
            socket.setSoTimeout(5 * 1000);
            while(serverVariable.get()){
                try{
                    Socket toClientSocket = socket.accept();
                    ClientHandler clientHandler = new ClientHandler(toClientSocket, serverVariable);
                    clientHandler.start();
                }
                catch (SocketTimeoutException ignored){

                }
                catch (Exception e){
                    System.out.println("Error while getting the client socket..." + e.getMessage());
                }
            }

        }catch (Exception e){
            System.out.println("Error while creating the ServerSocket..." + e.getMessage());
        }
    }
}
class ClientHandler extends Thread{
    private final Socket clientSocket;
    private String Username;
    private Object receivedObject;
    private final AtomicBoolean serverVariable;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket, AtomicBoolean newServerVariable){
        clientSocket = socket;
        serverVariable = newServerVariable;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            //UpdateClients.prepareUpdate(in, out);
        } catch (IOException e) {
            try {
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("error while opening the object output and input stream...");
        }
    }
    public void run(){
        /*try {
            clientSocket.setSoTimeout(10*1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }*/
        try{
            String response = null;
            boolean flagProtection = false;
            System.out.println("Client has connected to the Server.");
            do{
                receivedObject = in.readObject();
                if(receivedObject == null)
                    return;
                else if(receivedObject instanceof Login login) {
                    response = UserManager.checkPassword(login).toString();
                    //System.out.println(response.toString());
                }
                else if(receivedObject instanceof Register register) {
                    UserManager.registerUser(register);
                    if(!UserManager.userExists(register.getUsername())){
                        response = ErrorMessages.USERNAME_ALREADY_EXISTS.toString();
                    }
                    else{
                        Username = register.getUsername();
                        response = "Welcome! " + Username;
                    }
                }
                else if(receivedObject instanceof Request request){
                    //Takes normal string as request, will turn this into enum
                    switch(request.getTypeMessage()){
                        case CLOSE:
                            break;
                        case REQUEST_EDIT_PROFILE:
                            response = UserManager.getProfileForEdition(request.getMessage());
                            break;
                        case EDIT_PROFILE:
                            String[] messages = request.getMessage().split(",");
                            response = (UserManager.editProfile(messages[0], messages[1], messages[2], messages[3])) ? Messages.EDIT_PROFILE_SUCCESS.toString() : Messages.EDIT_PROFILE_ERROR.toString();
                            break;
                        case REGISTER_PRESENCE_CODE:
                            String[] args = request.getMessage().split(",");
                            response = (EventManager.registerUserInEvent(args[1], Integer.parseInt(args[0])) ? Messages.PRESENCE_CODE_REGISTED.toString() : Messages.INVALID_PRESENCE_CODE.toString());
                            break;
                        case GET_PRESENCES:
                            //response = EventManager.queryEvents(Username, null);
                            flagProtection = true;
                            ArrayList<Integer> idsUser = EventManager.getIdsEventsByUsername(request.getMessage());
                            String filter = null;
                            EventResult eventResult = new EventResult(" ");
                            eventResult.setColumns(" ");
                            if(idsUser.size() == 0){
                                out.writeObject(eventResult);
                                out.flush();
                                break;
                            }
                            filter = EventManager.createFilterOr(idsUser);
                            out.writeObject(EventManager.queryEvents(request.getMessage(), filter));
                            out.flush();
                            //EventManager.queryEvents(username, null);
                            break;
                        case GET_PRESENCES_FILTER:
                            //response = EventManager.queryEvents(Username, null);
                            flagProtection = true;
                            String[] aarg = request.getMessage().split(",");


                            out.writeObject(EventManager.queryEventsFilterUser(aarg[0], aarg[1], aarg[2]));
                            out.flush();
                            //EventManager.queryEvents(username, null);
                            break;
                        //admin commands here:
                        case CREATE_EVENT:
                            String[] arguments = request.getMessage().split(",");
                            Time timeBegin = new Time(arguments[2]);
                            Time timeEnd = new Time(arguments[3]);

                            Event event = new Event(arguments[0], arguments[1], timeBegin, timeEnd);
                            response = (EventManager.createEvent(event)) ? Messages.OK.toString() : ErrorMessages.CREATE_EVENT_FAILED.toString();
                            break;
                        case EDIT_EVENT:
                            String[] arg = request.getMessage().split(",");
                            Time timeBeginEdit = new Time(arg[1]);
                            Time timeEndEdit = new Time(arg[2]);

                            event = new Event(arg[0], "", timeBeginEdit, timeEndEdit);
                            response = (EventManager.editEvent(event)) ? ErrorMessages.CREATE_EVENT_FAILED.toString() : Messages.OK.toString() ;
                            break;
                        case DELETE_EVENT:
                            response = (EventManager.deleteEvent(request.getMessage())) ? Messages.OK.toString() : ErrorMessages.INVALID_EVENT_NAME.toString();
                            break;
                        case GET_EVENTS:
                            flagProtection = true;
                            EventResult eventResult3 = new EventResult(" ");
                            eventResult3.setColumns(" ");

                            out.writeObject(EventManager.queryEvents(null, null));
                            out.flush();
                            break;
                        case GENERATE_PRESENCE_CODE:
                            String[] argsPresence = request.getMessage().split(",");
                            String[] times = EventManager.getTime(argsPresence[0]).split(",");
                            Time timeBeginEvent = new Time(times[0]);
                            Time timeEndEvent = new Time(times[1]);
                            Time timeAtual = new Time();
                            Event event1 = new Event(argsPresence[0],argsPresence[1],timeBeginEvent,timeEndEvent);

                            if(!EventManager.checkIfCodeAlreadyCreated(argsPresence[0]))
                                response = EventManager.registerPresenceCode(event1, Integer.parseInt(argsPresence[1]), timeAtual);
                            else{
                                int code = EventManager.generateCode();
                                response = (!EventManager.updatePresenceCode(code, Integer.parseInt(argsPresence[1]), argsPresence[0])) ? String.valueOf(code) : ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString() ;
                            }
                            break;
                        case CHECK_PRESENCES:
                            response = (EventManager.checkPresences(request.getMessage())) ? Messages.OK.toString() : ErrorMessages.INVALID_REQUEST.toString();
                            break;
                        case GET_INFO_EVENT:
                            response = (EventManager.getEventInfo(request.getMessage()));
                            break;
                        case QUERY_EVENTS:
                            flagProtection = true;
                            String[] argsQuery = request.getMessage().split(",");
                            EventResult eventResult2 = new EventResult(" ");
                            eventResult2.setColumns(" ");

                            out.writeObject(EventManager.queryEventsFilters(argsQuery[0], argsQuery[1]));
                            out.flush();
                            break;
                        case GET_PRESENCES_EVENT:
                            flagProtection = true;
                            out.writeObject(EventManager.getPresencesEvent(request.getMessage()));
                            out.flush();
                            break;
                        case DELETE_PRESENCES:
                            String[] arg1 = request.getMessage().split(",");
                            response = (EventManager.deleteManualPresences(arg1[0],arg1[1])) ? ErrorMessages.INVALID_REQUEST.toString() : Messages.OK.toString() ;
                        break;
                        case INSERT_PRESENCES:
                            String[] arg2 = request.getMessage().split(",");
                            response = (EventManager.insertPresence(arg2[0],arg2[1]));
                            break;
                        default:
                            response = Messages.UNKNOWN_COMMAND.toString();
                    }
                }
                else{
                    response = ErrorMessages.INVALID_REQUEST.toString();
                }
                /*if(!serverVariable.get()){
                    response = close request
                }*/
                if(!flagProtection){
                    out.writeObject(response);
                    out.flush();
                }
                flagProtection = false;
            }while(!clientSocket.isClosed() && serverVariable.get());
        } catch (ClassNotFoundException e) {
            System.out.println("Error while reading the object sent to the server.");
        } catch (ParseException e) {
            System.out.println("Error while parsing dates.");
        } catch (EOFException | SocketException e){
            System.out.println("Client closed the connection.");
        } catch (IOException e) {
            System.out.println("error: IO" + e);
        } finally {
            try {
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}


public class ServerMain {
    public static void main(String[] args) {
        if(args.length != 4){
            System.out.println("Wrong syntax! java Main port DatabaseLocation RMIServiceName RMIPort");
            return;
        }

        File databaseDirectory = new File(args[1].trim());

        if(!databaseDirectory.exists()){
            System.out.println("A directoria " + databaseDirectory + " nao existe!");
            return;
        }

        if(!databaseDirectory.isDirectory()){
            System.out.println("O caminho " + databaseDirectory + " nao se refere a uma diretoria!");
            return;
        }

        if(!databaseDirectory.canWrite()){
            System.out.println("Sem permissoes de escrever na diretoria " + databaseDirectory + "!");
            return;
        }
        if(!DatabaseManager.connect(args[1])){
            System.out.println("Error while connecting to the Database.");
            return;
        }
        System.out.println("Connection to SQLite has been established.");

        RmiManager rmiManager;
        AtomicBoolean serverVariable = new AtomicBoolean(true);
        try {
            rmiManager = new RmiManager(args[2], databaseDirectory, Integer.parseInt(args[3]), serverVariable);
            if(!rmiManager.registerService())
                throw new RemoteException();
            System.out.println("RMI Service is Online!");
        }catch (RemoteException e) {
            System.out.println("Error while creating the RMI manager: " + e);
            System.exit(1);
            return;
        } catch (SocketException e) {
            System.out.println("Error while connecting socket to Multicast Group." + e);
            System.exit(1);
            return;
        }

        WaitClient waitClient = new WaitClient(Integer.parseInt(args[0]), serverVariable);
        waitClient.start();

        ConfigurableApplicationContext restServer = SpringApplication.run(TpPdMeta2Application.class);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome!");
        while(!scanner.next().equalsIgnoreCase("exit") && restServer.isActive());
        System.out.println("Closing Server.");
        serverVariable.set(false);
        try {
            restServer.close();
            waitClient.join();
            rmiManager.getRmiHeartBeatThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}