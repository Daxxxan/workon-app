package com.workon.utils;

import com.workon.controllers.CreateProjectController;
import com.workon.controllers.LoginConnectionController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
            if(requestType.equals("POST") || requestType.equals("PUT")){
                if(parameters != null){
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    String urlParameters = ParameterStringBuilder.getParamsString(parameters);
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();
                }
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
            System.out.println(e);
            return null;
        }
    }

    public static StringBuffer getProject() throws IOException {
        //Get du projet
        String getProject = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId());
        return setRequest(getProject, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getCollaborators() throws IOException {
        String getProjectCollaborators = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts");
        return setRequest(getProjectCollaborators, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getCurrentBugs() throws IOException {
        String getBug = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs/")
                .concat(CreateProjectController.getProject().getCurrentBugId());
        return setRequest(getBug, null, null, "GET", null, LoginConnectionController.getUserToken());

    }

    public static StringBuffer getAccount(String id) throws IOException {
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(id);
        return setRequest(getCreator, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getCollaboratorAccount(String id) throws IOException {
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts/").concat(id);
        return setRequest(getCreator, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getBugs() throws IOException {
        String getBugs = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs");
        return setRequest(getBugs, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer addBug(String name, String description, String projectId) throws IOException {
        String getBugRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/bugs");

        Map<String, String> bugParameters = new HashMap<>();
        bugParameters.put("name", name);
        bugParameters.put("description", description);
        bugParameters.put("projectId", projectId);

        return setRequest(getBugRequest, bugParameters, null, "POST", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer addProject(String name) throws IOException {
        String createProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
        Map<String, String> createProjectParameters = new HashMap<>();
        createProjectParameters.put("name", name);

        return setRequest(createProjectRequest, createProjectParameters, null, "POST", null, LoginConnectionController.getUserToken());

    }

    public static StringBuffer addCollaboratorsToProject(String collaboratorIdOrEmail, String projectId) throws IOException {
        String addDirectorRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId()))
                .concat("/projects/").concat(projectId).concat("/accounts/rel/").concat(collaboratorIdOrEmail);
        //Lancement de l'ajout du directeur aux collaborateurs
        return setRequest(addDirectorRequest, null, null, "PUT", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer connection(String email, String password, Label loginErrorConnectionLabel) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);

        //Lancement de la requete
        return setRequest(LoginConnectionController.getPath().concat("accounts/login"), parameters, loginErrorConnectionLabel, "POST", "Le compte n'existe pas", null);
    }

    public static StringBuffer createAccount(String lastname, String firstname, String email, String password, Label registerErrorLabel) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lastname", lastname);
        parameters.put("firstname", firstname);
        parameters.put("email", email);
        parameters.put("password", password);

        return setRequest(LoginConnectionController.getPath().concat("accounts"), parameters, registerErrorLabel, "POST", "Le compte existe déjà !", null);
    }

    public static StringBuffer getProjectSteps() throws IOException{
        String getProjectSteps = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/steps");
        return setRequest(getProjectSteps, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getProjects() throws IOException {
        String getProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
        return setRequest(getProjectRequest, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer addMessage(String content) throws IOException{
        Map<String, String> parameters = new HashMap<>();
        parameters.put("content", content);
        parameters.put("accountId", LoginConnectionController.getUserId().toString());
        String addMessageRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId())
                .concat("/messages");
        return setRequest(addMessageRequest, parameters, null, "POST", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getBugMessages() throws Exception{
        String getMessageRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId())
                .concat("/messages");
        return setRequest(getMessageRequest, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer updateBug() throws IOException {
        String getBugRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId());

        Map<String, String> bugParameters = new HashMap<>();
        bugParameters.put("name", "nom");
        bugParameters.put("state", "Fermer");
        bugParameters.put("description", "desc");

        return setRequest(getBugRequest, bugParameters, null, "PUT", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer logout() throws Exception {
        String logout = LoginConnectionController.getPath().concat("accounts/logout");
        return setRequest(logout, null, null, "POST", null, LoginConnectionController.getUserToken());
    }

    public static StringBuffer getFiles() throws Exception {
        String getFiles = LoginConnectionController.getPath().concat("Containers/").concat(CreateProjectController.getProject().getId()).concat("/files");
        return setRequest(getFiles, null, null, "GET", null, LoginConnectionController.getUserToken());
    }

    public static void downloadFile(String container, String fileName) throws Exception{
        Application application = new Application() {
            @Override
            public void start(Stage stage) {}
        };
        application.getHostServices().showDocument(LoginConnectionController.getPath().concat("Containers/").concat(container).concat("/download/").concat(fileName));
    }

    public static StringBuffer addStep(String projectId, String name, String date) throws Exception{
        //Request pour l'ajout des steps au projet
        String addStepsRequest = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnectionController.getUserId()))
                .concat("/projects/").concat(projectId).concat("/steps");

        //Set des parameters de la requete en POST
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("date", date);
        parameters.put("state", "En cours");

        //Lancement de l'ajout des steps
        return HttpRequest.setRequest(addStepsRequest, parameters, null, "POST", null, LoginConnectionController.getUserToken());

    }
}