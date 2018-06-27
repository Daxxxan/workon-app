package com.workon.utils;

import com.workon.controllers.CreateProjectController;
import com.workon.controllers.LoginConnectionController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public static StringBuffer setRequest(String stringUrl, Map<String, String> parameters, Label errorLabel, String requestType, String errorMessage, String token) throws IOException {
        try{
            URL url = new URL(stringUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            if(token != null){
                String accessToken = "Bearer ";
                accessToken = accessToken.concat(token);
                con.setRequestProperty("Authorization", accessToken);
            }

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
            return content;
        }catch (IOException e){
            if(errorLabel != null){
                LabelHelper.setLabel(errorLabel, errorMessage, Pos.CENTER, "#FF0000");
            }
            return null;
        }
    }

    public static StringBuffer getProject() throws IOException {
        //Get du projet
        String getProject = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId());
        return HttpRequest.setRequest(getProject, null, null, "GET", null, LoginConnectionController.getUserToken());

    }
}
