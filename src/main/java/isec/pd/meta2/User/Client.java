package isec.pd.meta2.User;

import  isec.pd.meta2.Shared.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int timeoutTime = 10;
    private static int port;
    public static void setPort(int newPort){port = newPort;}
    private static String address;
    public static void setAddress(String newAddress){address = newAddress;}
    /**
     * THIS IS EITHER LOGIN OR REGISTER OBJECT
     */
    private static Object newObject;

    public static String setObjectLogin(Object object){
        Client.newObject = object;
        return handleLogin();
    }
    public static boolean setObjectRegister(Object object){
        Client.newObject = object;
        return handleRegister();
    }

    public static String getUsername(){
        return username;
    }
    public static void setUsername(String username){
        Client.username = username;
    }
    private static String username;
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static Socket getSocket(){
        return socket;
    }
    public static ObjectOutputStream getOut(){
        return out;
    }
    public static ObjectInputStream getIn(){
        return in;
    }

    public static void closeConnection(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException | NullPointerException ignored) { }
    }
    public static void prepareClient() {
        try {
            socket = new Socket(address, port);
            socket.setSoTimeout(timeoutTime*1000);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        }
        catch (Exception e) {
            System.out.println("exception");
            closeConnection();
            e.printStackTrace();
        }
    }

    public static String handleLogin(){
        String response;
        String input;
        boolean flag;
        try {
            out.writeObject(newObject);
            response = (String) in.readObject();
            return response;
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (Exception e){
            closeConnection();
            System.out.println("exception");
        }

        return ErrorMessages.INVALID_PASSWORD.toString();
    }
    public static boolean handleRegister(){
        String response;
        String input;
        boolean flag;
        try {
            out.writeObject(newObject);
            response = (String) in.readObject();
            if(response.equals(ErrorMessages.USERNAME_ALREADY_EXISTS.toString())){
                return false;
            }
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static String getProfileData(String input){
        Request request = new Request(Messages.REQUEST_EDIT_PROFILE, input);
        try {
            out.writeObject(request);
            return (String) in.readObject();
        }
        catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        }
        catch (IOException | ClassNotFoundException e) {
            closeConnection();
            e.printStackTrace();
        }
        return ErrorMessages.SQL_ERROR.toString();
    }
    public static boolean editProfile(String input){
        Request request = new Request(Messages.EDIT_PROFILE, input);
        try {
            out.writeObject(request);
            String response = (String) in.readObject();
            if(response.equals(Messages.EDIT_PROFILE_ERROR.toString())){
                return false;
            }
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static boolean sendCode(String code){

        Request request = new Request(Messages.REGISTER_PRESENCE_CODE, code + "," + Client.getUsername());
        try{
            out.writeObject(request);
            String response = (String) in.readObject();
            if(response.equals(Messages.INVALID_PRESENCE_CODE.toString())){
                return false;
            }
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static EventResult getPresences(String input){
        Request request = new Request(Messages.GET_PRESENCES, input);
        try{
            out.writeObject(request);
            return (EventResult) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static EventResult queryEvents(String column, String text, String username) {
        Request request = new Request(Messages.GET_PRESENCES_FILTER, column+","+text+","+username);
        try{
            out.writeObject(request);
            return (EventResult) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
            System.out.println(e.getMessage());
        }
        return null;
    }
}
