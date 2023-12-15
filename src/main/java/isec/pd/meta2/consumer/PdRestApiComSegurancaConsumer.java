package isec.pd.meta2.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import com.google.gson.*;
import isec.pd.meta2.Shared.EventResult;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class PdRestApiComSegurancaConsumer {

    public static String sendRequestAndShowResponse(String uri, String verb, String authorizationValue, String body) throws MalformedURLException, IOException {

        String responseBody = null;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(verb);
        connection.setRequestProperty("Accept", "application/xml, */*");

        if(authorizationValue!=null) {
            connection.setRequestProperty("Authorization", authorizationValue);
        }

        if(body!=null){
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "Application/Json");
            connection.getOutputStream().write(body.getBytes());
        }

        connection.connect();

        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " +  responseCode + " (" + connection.getResponseMessage() + ")");

        Scanner s;

        if(connection.getErrorStream()!=null) {
            s = new Scanner(connection.getErrorStream()).useDelimiter("\\A");
            responseBody = s.hasNext() ? s.next() : null;
        }

        try {
            s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
            responseBody = s.hasNext() ? s.next() : null;
        } catch (IOException e){}

        connection.disconnect();

        System.out.println(verb + " " + uri + (body==null?"":" with body: "+body) + " ==> " + responseBody);
        System.out.println();

        return responseBody;
    }

    private static void getAllEventsWithFilter(String authorizationValue, String filterType, String filterValue) throws IOException {
        String url = "http://localhost:8080" + "/events";

        // Create the query parameter based on the filter type
        String queryParams = String.format("?%s=%s", filterType, filterValue);

        // Build the complete URI
        String fullUrl = url + queryParams;

        // Set up your HTTP request parameters
        String verb = "GET";
        String body = null; // No request body for GET requests

        // Send the request and show the response
        String response = sendRequestAndShowResponse(fullUrl, verb, authorizationValue, body);
        EventResult eventResult = convertJsonToEventResult(response);
        System.out.println(eventResult.getColumns());
        // Handle the response as needed
        //System.out.println("Get All Events Response with " + filterType + ": " + response);
    }
    private static EventResult convertJsonToEventResult(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, EventResult.class);
    }
    /*
    public static void getData() throws IOException {
        String eventDesignation;
        String optionalFilter;
        String optionalFilterValue;

        String uri = "http://localhost:8080/events";

        URL url = new URL(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter the event designation: ");
        //eventDesignation = "/" + scanner.nextLine();

        System.out.println("Choose an optional filter: ");
        System.out.println("1 - eventDesignation");
        System.out.println("2 - place");
        System.out.println("3 - timeBegin");
        System.out.println("4 - timeEnd");
        int opt = scanner.nextInt();
        switch (opt){
            case 1:
                System.out.println("Plz type the value:");
                String response = scanner.nextLine();
                String queryParams = String.format("?%s=%s", "eventDesignation", response);
                HttpURLConnection connection;
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                uri += "/" +
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }



    }
*/
    public static void main(String args[]) throws MalformedURLException, IOException {

        String helloUri = "http://localhost:8080/events/delete/Aula%20ED%20T1";
        String helloUri2 = "http://localhost:8080/events/presences/Aula%20PD%20T1";
        String loginUri = "http://localhost:8080/login";
        String loremUri = "http://localhost:8080/lorem?type=paragraph";

        System.out.println();

        String token;

        //login
        String credentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());
        token = sendRequestAndShowResponse(loginUri, "POST","basic "+ credentials, null); //Base64(admin:admin) YWRtaW46YWRtaW4=
        //presenças das aulas
        sendRequestAndShowResponse(helloUri2, "GET", "bearer " + token, null);
        //apagar uma aula
        //sendRequestAndShowResponse(helloUri, "DELETE", "bearer " + token, null);
        getAllEventsWithFilter("bearer " + token, "place", "DEIS");


    }
}
