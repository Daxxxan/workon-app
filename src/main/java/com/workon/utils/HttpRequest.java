package com.workon.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequest {
    public static boolean setRequest(String stringUrl, Map<String, String> parameters, Label errorLabel, String requestType, String errorMessage) throws IOException {
        try{
            URL url = new URL(stringUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestType);

            con.setDoOutput(true);
            if(requestType.equals("POST")){
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                String urlParameters = ParameterStringBuilder.getParamsString(parameters);
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(content);
            in.close();
            return true;
        }catch (IOException e){
            if(errorLabel != null){
                LabelHelper.setLabel(errorLabel, errorMessage, Pos.CENTER, "#FF0000");
            }
            return false;
        }
    }
}
