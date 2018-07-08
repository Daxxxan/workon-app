package com.workon.utils;

import com.workon.controllers.CreateProjectController;
import com.workon.controllers.LoginConnectionController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import okhttp3.*;

public class HttpRequest {
    public static String setOkHttpRequest(String url, RequestBody formBody, Boolean connection, String type) throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        
        if(type.equals("POST")){
            if(connection && formBody != null) {
                request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
            }else if(formBody != null){
                request = new Request.Builder()
                        .url(url)
                        .header("Authorization", LoginConnectionController.getUserToken())
                        .post(formBody)
                        .build();
            }else{
                request = new Request.Builder()
                        .url(url)
                        .header("Authorization", LoginConnectionController.getUserToken())
                        .build();
            }
        }else if(type.equals("GET")){
            request = new Request.Builder()
                    .url(url)
                    .header("Authorization", LoginConnectionController.getUserToken())
                    .build();
        }else if(type.equals("PATCH")){
            if(formBody != null){
                request = new Request.Builder()
                        .url(url)
                        .header("Authorization", LoginConnectionController.getUserToken())
                        .patch(formBody)
                        .build();
            }else{
                request = new Request.Builder()
                        .url(url)
                        .header("Authorization", LoginConnectionController.getUserToken())
                        .method("PATCH", null)
                        .build();
            }
        }else if(type.equals("PUT")){
            request = new Request.Builder()
                    .url(url)
                    .header("Authorization", LoginConnectionController.getUserToken())
                    .put(formBody)
                    .build();
        }
        
        Call call = client.newCall(request);
        Response response = call.execute();
        if(response.code() != 200){
            return null;
        }else{
            return response.body().string();
        }
    }

    public static String getCollaborators() throws Exception {
        String getProjectCollaborators = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts");
        return setOkHttpRequest(getProjectCollaborators, null, false, "GET");
    }

    public static String getCurrentBugs() throws Exception {
        String getBug = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs/")
                .concat(CreateProjectController.getProject().getCurrentBugId());
        return setOkHttpRequest(getBug, null, false, "GET");

    }

    public static String getAccount(String id) throws Exception {
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(id);
        return setOkHttpRequest(getCreator, null, false, "GET");
    }

    public static String getCollaboratorAccount(String id) throws Exception {
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts/").concat(id);
        return setOkHttpRequest(getCreator, null, false, "GET");
    }

    public static String getBugs(String state) throws Exception {
        String getBugs = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs")
                .concat("?filter={\"where\":{\"state\":" + state + "}}");

        return setOkHttpRequest(getBugs, null, false, "GET");
    }

    public static String addBug(String name, String description, String projectId) throws Exception {
        String getBugRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/bugs");

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("description", description)
                .add("projectId", projectId)
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "POST");
    }

    public static String addProject(String name) throws Exception {
        String createProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();

        return setOkHttpRequest(createProjectRequest, formBody, false, "POST");

    }

    public static String addCollaboratorsToProject(String collaboratorIdOrEmail, String projectId) throws Exception {
        String addDirectorRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId()))
                .concat("/projects/").concat(projectId).concat("/accounts/rel/").concat(collaboratorIdOrEmail);
        RequestBody formBody = RequestBody.create(null, new byte[]{});
        return setOkHttpRequest(addDirectorRequest, formBody, false, "PUT");
    }

    public static String connection(String email, String password, Label loginErrorConnectionLabel) throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();
        String response = setOkHttpRequest(LoginConnectionController.getPath().concat("accounts/login"), formBody, true, "POST");
        if(response == null){
            LabelHelper.setLabel(loginErrorConnectionLabel, "Les identifiants de connexion ne sont pas valide.", Pos.CENTER, "#FF0000");
            return null;
        }else{
            return response;
        }
    }

    public static String createAccount(String lastname, String firstname, String email, String password, Label registerErrorLabel) throws Exception {
        String request = LoginConnectionController.getPath().concat("accounts");
        RequestBody formBody = new FormBody.Builder()
                .add("lastname", lastname)
                .add("firstname", firstname)
                .add("email", email)
                .add("password", password)
                .build();

        return setOkHttpRequest(request, formBody, true, "POST");
    }

    public static String getProjectSteps() throws Exception {
        String getProjectSteps = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/steps");
        return setOkHttpRequest(getProjectSteps, null, false, "GET");
    }

    public static String getProjects() throws Exception {
        String getProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(Integer.toString(LoginConnectionController.getUserId())).concat("/projects");
        return setOkHttpRequest(getProjectRequest, null, false, "GET");
    }

    public static String addMessage(String content) throws Exception {
        String addMessageRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId())
                .concat("/messages");
        RequestBody formBody = new FormBody.Builder()
                .add("content", content)
                .add("accountId", LoginConnectionController.getUserId().toString())
                .build();
        return setOkHttpRequest(addMessageRequest, formBody, false, "POST");
    }

    public static String getBugMessages() throws Exception{
        String getMessageRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId())
                .concat("/messages");
        return setOkHttpRequest(getMessageRequest, null, false, "GET");
    }

    public static String updateBug() throws Exception {
        String getBugRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId());

        RequestBody formBody = new FormBody.Builder()
                .add("state", "1")
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "PATCH");
    }

    public static String logout() throws Exception {
        String logout = LoginConnectionController.getPath().concat("accounts/logout");
        return setOkHttpRequest(logout, null, false, "POST");
    }

    public static String getFiles() throws Exception {
        String getFiles = LoginConnectionController.getPath().concat("Containers/").concat(CreateProjectController.getProject().getId()).concat("/files");
        return setOkHttpRequest(getFiles, null, false, "GET");
    }

    public static void downloadFile(String container, String fileName) throws Exception{
        Application application = new Application() {
            @Override
            public void start(Stage stage) {}
        };
        application.getHostServices().showDocument(LoginConnectionController.getPath().concat("Containers/").concat(container).concat("/download/").concat(fileName));
    }

    public static String addStep(String projectId, String name, String date) throws Exception{
        String addStepsRequest = "http://localhost:3000/api/accounts/".concat(Integer.toString(LoginConnectionController.getUserId()))
                .concat("/projects/").concat(projectId).concat("/steps");

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("date", date)
                .add("state", "En cours")
                .build();

        return setOkHttpRequest(addStepsRequest, formBody, false, "POST");
    }
}