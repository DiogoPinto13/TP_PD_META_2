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

public class PdRestApiComSegurancatConsumer {

    public static String sendRequestAndShowResponse(String uri, String verb, String authorizationValue) throws MalformedURLException, IOException {

        String responseBody = null;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(verb);
        connection.setRequestProperty("Accept", "application/xml, */*");

        if(authorizationValue!=null) {
            connection.setRequestProperty("Authorization", authorizationValue);
        }

        connection.connect();

        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " +  responseCode + " (" + connection.getResponseMessage() + ")");

        if(connection.getErrorStream()!=null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                responseBody = br.readLine();
            } catch (IOException e) {}
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            responseBody = br.readLine();
        }catch (IOException e) {}

        connection.disconnect();

        System.out.println(uri + " -> " + responseBody);
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
        token = sendRequestAndShowResponse(loginUri, "POST","basic YWRtaW46YWRtaW4="); //Base64(admin:admin)
        //System.out.println(Base64.getEncoder().encode("admin:admin".getBytes())); -- THIS NO WORK

        //Língua "gr" não suportada
        sendRequestAndShowResponse(helloUri2, "GET", "bearer " + token);

        //OK
        sendRequestAndShowResponse(helloUri, "DELETE", "bearer " + token);

    }
}
