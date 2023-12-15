package isec.pd.meta2.User;

import com.google.gson.Gson;
import isec.pd.meta2.Shared.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import static isec.pd.meta2.User.Client.sendRequestAndShowResponse;
import static isec.pd.meta2.User.Client.token;

public class Admin {

    public static String getUsername(){
        return username;
    }
    public static void setUsername(String username){
        Admin.username = username;
    }
    private static String username;


    public static String createEvent(String designation, String place, Time timeBeggining, Time timeEnding) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(designation).append(",").append(place).append(",").append(timeBeggining.toString()).append(",").append(timeEnding.toString());

        Map<String, String> requestData = new HashMap<>();
        requestData.put("args", stringBuilder.toString());
        Gson gson = new Gson();
        String requestBody = gson.toJson(requestData);

        try {
            return sendRequestAndShowResponse("http://localhost:8080/events/newEvent", "POST", "basic " + "bearer " + token, requestBody).first;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ErrorMessages.SQL_ERROR.toString();
    }

/*
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
        String uri = "http://localhost:8080/events/delete/" + designacao;
        try {
            return sendRequestAndShowResponse(uri, "DELETE", "basic " + "bearer " + token, null).first;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ErrorMessages.SQL_ERROR.toString();
    }
    public static EventResult getEvents(String username){
        EventResult eventResult;
        Gson gson = new Gson();
        try{
            String body = sendRequestAndShowResponse("http://localhost:8080/events", "GET", "bearer " + token, null).first;
            eventResult = gson.fromJson(body, EventResult.class);
            return eventResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String generatePresenceCode(String code, int duracao){

        Map<String, String> requestData = new HashMap<>();
        requestData.put("args", code + "," + duracao);
        Gson gson = new Gson();
        String requestBody = gson.toJson(requestData);

        try {
            return sendRequestAndShowResponse("http://localhost:8080/events/genCode", "POST", "basic " + "bearer " + token, requestBody).first;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ErrorMessages.SQL_ERROR.toString();
    }

    public static EventResult queryEvents(String column, String text ){
        EventResult eventResult;

        Map<String, String> requestData = new HashMap<>();
        requestData.put(column, text);
        Gson gson = new Gson();
        String requestBody = gson.toJson(requestData);

        try{
            String body = sendRequestAndShowResponse("http://localhost:8080/events", "GET", "bearer " + token, requestBody).first;
            eventResult = gson.fromJson(body, EventResult.class);
            return eventResult;
        } catch (IOException e) {
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
    /* NÃO É PRECISO ATÉ PORQUE NEM TEMOS ESSE ENDPOINT E PRA ALÉM DISSO A VERIFICAÇÃO ESTÁ A SER FEITA DO LADO DO UTILIZADOR
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
    }
    /*
     /*
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
        String uri = "http://localhost:8080/events/presences/" + designacao;
        EventResult eventResult;
        Gson gson = new Gson();
        try {
            String body = sendRequestAndShowResponse(uri, "GET", "basic " + "bearer " + token, null).first;
            eventResult = gson.fromJson(body, EventResult.class);
            return eventResult;
        } catch (IOException e) {
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

    /*
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
    }*/

}
