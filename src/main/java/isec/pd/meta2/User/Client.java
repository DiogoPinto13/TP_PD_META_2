package isec.pd.meta2.User;

import com.google.gson.Gson;
import  isec.pd.meta2.Shared.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    public static String token;
    private static final int timeoutTime = 10;
    private static int port;

    public static void setPort(int newPort) {
        port = newPort;
    }

    private static String address;

    public static void setAddress(String newAddress) {
        address = newAddress;
    }

    /**
     * THIS IS EITHER LOGIN OR REGISTER OBJECT
     */
    private static Login login;
    private static Register register;

    public static String setObjectLogin(Login Login) {
        login = Login;
        return handleLogin();
    }

    public static boolean setObjectRegister(Register Register) {
        register = Register;
        return handleRegister();
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Client.username = username;
    }

    private static String username;

    //HTTP REQUEST FUNCTIONS
    public static Pair<String, Integer> sendRequestAndShowResponse(String uri, String verb, String authorizationValue, String body) throws MalformedURLException, IOException {

        String responseBody = null;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        connection.setRequestMethod(verb);
        connection.setRequestProperty("Accept", "application/xml, */*");

        if (authorizationValue != null) {
            connection.setRequestProperty("Authorization", authorizationValue);
        }

        if (body != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "Application/Json");
            connection.getOutputStream().write(body.getBytes());
        }

        connection.connect();

        int responseCode = connection.getResponseCode();
        //System.out.println("Response code: " +  responseCode + " (" + connection.getResponseMessage() + ")");

        Scanner s;

        if (connection.getErrorStream() != null) {
            s = new Scanner(connection.getErrorStream()).useDelimiter("\\A");
            responseBody = s.hasNext() ? s.next() : null;
        }

        try {
            s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
            responseBody = s.hasNext() ? s.next() : null;
        } catch (IOException e) {
        }

        connection.disconnect();

        //System.out.println(verb + " " + uri + (body==null?"":" with body: "+body) + " ==> " + responseBody);
        //System.out.println();

        Pair<String, Integer> pair = new Pair<>(responseBody, responseCode);
        return pair;
    }

    private static EventResult convertJsonToEventResult(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, EventResult.class);
    }


    public static String handleLogin() {
        String response;
        Pair<String, Integer> responsePair;
        String input;
        boolean flag;
        String loginUri = "http://localhost:8080/login";
        String credentialsToEncode = login.getUsername() + ":" + login.getPassword();
        String credentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());
        try {
            responsePair = sendRequestAndShowResponse(loginUri, "POST", "basic " + credentials, null);
            token = responsePair.first;
            response = (responsePair.second) == 401 ? ErrorMessages.INVALID_PASSWORD.toString() : Messages.OK.toString();
            if (response.equals(Messages.OK.toString())) {
                responsePair = sendRequestAndShowResponse("http://localhost:8080/isAdmin", "GET", "bearer " + token, null);
                return responsePair.first;
            } else {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ErrorMessages.INVALID_PASSWORD.toString();
    }

    public static boolean handleRegister() {
        String response;
        Pair<String, Integer> responsePair;
        try {
            Map<String, String> requestData = new HashMap<>();
            requestData.put("name", register.getName());
            requestData.put("id", register.getId());
            requestData.put("username", register.getUsername());
            requestData.put("password", register.getPassword());
            Gson gson = new Gson();
            String requestBody = gson.toJson(requestData);

            responsePair = sendRequestAndShowResponse("http://localhost:8080/register", "POST", null, requestBody);
            return responsePair.first.equals(Messages.OK.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /*
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
    }*/
    /*
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
    }*/

    public static boolean sendCode(String code) {
        //envia o codigo, username
        //Pair<String, Integer> responsePair;
        Map<String, String> requestData = new HashMap<>();
        requestData.put("presenceCode", code);
        requestData.put("username", username);
        Gson gson = new Gson();
        String requestBody = gson.toJson(requestData);

        try {
            return sendRequestAndShowResponse("http://localhost:8080/codEvent", "POST", "basic " + "bearer " + token, requestBody).first.equals(Messages.REGISTER_PRESENCE_CODE.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static EventResult getPresences(String input) {

        Pair<String, Integer> responsePair;
        EventResult eventResult;
        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", input);
        Gson gson = new Gson();
        String requestBody = gson.toJson(requestData);

        try {
            responsePair = sendRequestAndShowResponse("http://localhost:8080/presences", "GET", "basic " + "bearer " + token, requestBody);
            eventResult = gson.fromJson(responsePair.first, EventResult.class);
            return eventResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EventResult queryEvents(String column, String text, String username) {
        Pair<String, Integer> responsePair;
        EventResult eventResult;

        Map<String, String> requestData = new HashMap<>();
        requestData.put(column, text);
        requestData.put("username", username);
        Gson gson = new Gson();
        String requestBody = gson.toJson(requestData);

        try {
            responsePair = sendRequestAndShowResponse("http://localhost:8080/presences", "GET", "basic " + "bearer " + token, requestBody);
            eventResult = gson.fromJson(responsePair.first, EventResult.class);
            return eventResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}