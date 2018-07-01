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

    public static StringBuffer getCollaborators() throws IOException {
        String getProjectCollaborators = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts");
        return HttpRequest.setRequest(getProjectCollaborators, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getCurrentBugs() throws IOException {
        String getBug = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs/")
                .concat(CreateProjectController.getProject().getCurrentBugId());
        return HttpRequest.setRequest(getBug, null, null, "GET", null, LoginConnectionController.getUserToken());

    }

    public static StringBuffer getAccount(String id) throws IOException {
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(id);
        return HttpRequest.setRequest(getCreator, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getBugs() throws IOException {
        String getBugs = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs");
        return HttpRequest.setRequest(getBugs, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer addBug(String name, String description, String projectId) throws IOException {
        String getBugRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/bugs");

        Map<String, String> bugParameters = new HashMap<>();
        bugParameters.put("name", name);
        bugParameters.put("description", description);
        bugParameters.put("projectId", projectId);

        return HttpRequest.setRequest(getBugRequest, bugParameters, null, "POST", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer addProject(String name) throws IOException {
        String createProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
        Map<String, String> createProjectParameters = new HashMap<>();
        createProjectParameters.put("name", name);

        return HttpRequest.setRequest(createProjectRequest, createProjectParameters, null, "POST", null, LoginConnectionController.getUserToken());

    }

    public static StringBuffer addCollaboratorsToProject(String collaboratorIdOrEmail, String projectId) throws IOException {
        String addDirectorRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId()))
                .concat("/projects/").concat(projectId).concat("/accounts/rel/").concat(collaboratorIdOrEmail);
        //Lancement de l'ajout du directeur aux collaborateurs
        return HttpRequest.setRequest(addDirectorRequest, null, null, "PUT", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer connection(String email, String password, Label loginErrorConnectionLabel) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);

        //Lancement de la requete
        return HttpRequest.setRequest(LoginConnectionController.getPath().concat("accounts/login"), parameters, loginErrorConnectionLabel, "POST", "Le compte n'existe pas", null);
    }

    public static StringBuffer createAccount(String lastname, String firstname, String email, String password, Label registerErrorLabel) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lastname", lastname);
        parameters.put("firstname", firstname);
        parameters.put("email", email);
        parameters.put("password", password);

        return HttpRequest.setRequest(LoginConnectionController.getPath().concat("accounts"), parameters, registerErrorLabel, "POST", "Le compte existe déjà !", null);
    }

    public static StringBuffer getProjectSteps() throws IOException{
        String getProjectSteps = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/steps");
        return HttpRequest.setRequest(getProjectSteps, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getProjects() throws IOException {
        String getProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
        return HttpRequest.setRequest(getProjectRequest, null, null, "GET", null, LoginConnectionController.getUserToken());
    }
}
