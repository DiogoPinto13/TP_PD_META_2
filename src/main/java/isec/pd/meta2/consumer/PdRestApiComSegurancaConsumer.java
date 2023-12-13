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

    public static void main(String args[]) throws MalformedURLException, IOException {

        String helloUri = "http://localhost:8080/events/delete/Aula%20ED%20T1";
        String helloUri2 = "http://localhost:8080/events/presences/Aula%20PD%20T1";
        String loginUri = "http://localhost:8080/login";
        String loremUri = "http://localhost:8080/lorem?type=paragraph";

        System.out.println();

        //Falta um campo "Authorization: basic ..." válido no cabeçalho do pedido para autenticação básica
        String token;

        //OK
        //token = sendRequestAndShowResponse(loginUri, "POST","basic YWRtaW46YWRtaW4="); //Base64(admin:admin)
        String credentials = Base64.getEncoder().encodeToString("admin:admin".getBytes());
        token = sendRequestAndShowResponse(loginUri, "POST","basic "+ credentials, null); //Base64(admin:admin) YWRtaW46YWRtaW4=

        //Língua "gr" não suportada
        sendRequestAndShowResponse(helloUri2, "GET", "bearer " + token, null);

        //OK
        sendRequestAndShowResponse(helloUri, "DELETE", "bearer " + token, null);

    }
}
