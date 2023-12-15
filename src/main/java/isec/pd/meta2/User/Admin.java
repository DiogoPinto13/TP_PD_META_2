package isec.pd.meta2.User;

import isec.pd.meta2.Shared.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Admin {

    public static String getUsername(){
        return username;
    }
    public static void setUsername(String username){
        Admin.username = username;
    }
    private static String username;


    public static String createEvent(String designation, String place, Time timeBeggining, Time timeEnding){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(designation).append(",").append(place).append(",").append(timeBeggining.toString()).append(",").append(timeEnding.toString());
        Request request = new Request(Messages.CREATE_EVENT, stringBuilder.toString());
        try {
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ErrorMessages.SQL_ERROR.toString();
    }/*
    public static String editEvent(String designacao, Time hInicio, Time hFim){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(designacao).append(",").append(hInicio.toString()).append(",").append(hFim.toString());
        Request request = new Request(Messages.EDIT_EVENT, stringBuilder.toString());
        try {
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ErrorMessages.SQL_ERROR.toString();
    }*/
    public static String deleteEvent(String designacao){
        Request request = new Request(Messages.DELETE_EVENT, designacao);
        try{
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ErrorMessages.SQL_ERROR.toString();
    }
    public static EventResult getEvents(String username){
        Request request = new Request(Messages.GET_EVENTS, username);
        try{
            out.writeObject(request);
            return (EventResult) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String generatePresenceCode(String code, int duracao){
        Request request = new Request(Messages.GENERATE_PRESENCE_CODE, code + "," + duracao);
        try{
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static EventResult queryEvents(String column, String text ){
        Request request = new Request(Messages.QUERY_EVENTS, column+","+ text);
        try{
            out.writeObject(request);
            return (EventResult) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }/*
    public static String registerPresence(String eventDesignation, String clientName){
        Request request = new Request(Messages.INSERT_PRESENCES, eventDesignation+","+ clientName);
        try{
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public static String CheckPresences(String designacao) {
        //caso tenha presenças registadas ou não seja possivel encontrar o evento return true
        Request request = new Request(Messages.CHECK_PRESENCES, designacao);
        try{
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }/*
    public static String GetInfoAboutEvent(String designacao) {

        Request request = new Request(Messages.GET_INFO_EVENT, designacao);
        try{
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public static EventResult getPresencesEvent(String designacao) {
        Request request = new Request(Messages.GET_PRESENCES_EVENT, designacao);
        try{
            out.writeObject(request);
            return (EventResult) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
    /*
    public static String EliminatePresenceinEvent(String designacao, String username) {
        Request request = new Request(Messages.DELETE_PRESENCES, designacao+","+ username);
        try{
            out.writeObject(request);
            return (String) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static EventResult getPresencesByUsername(String username){
        Request request = new Request(Messages.GET_PRESENCES, username);
        try{
            out.writeObject(request);
            return (EventResult) in.readObject();
        } catch (SocketTimeoutException e){
            Main.fatalErrorNotification(Main.requestTimeoutErrorTitle, Main.requestTimeoutErrorDescription);
        } catch (SocketException e){
            Main.fatalErrorNotification(Main.noServerErrorTitle, Main.noServerErrorDescription);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
