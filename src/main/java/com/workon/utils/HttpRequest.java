package com.workon.utils;

import com.workon.controllers.CreateProjectController;
import com.workon.controllers.LoginConnectionController;
import com.workon.controllers.ProjectsController;
import com.workon.utils.parser.AnnotationParser;
import com.workon.utils.parser.NoNull;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import okhttp3.*;

import java.time.LocalDate;

public class HttpRequest {
    public static String setOkHttpRequest(@NoNull String url, RequestBody formBody, @NoNull Boolean connection, @NoNull String type) throws Exception{
        AnnotationParser.parse(url, formBody, connection, type);
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
                        .header("Authorization", "Bearer " + LoginConnectionController.getUserToken())
                        .post(formBody)
                        .build();
            }else{
                request = new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " + LoginConnectionController.getUserToken())
                        .build();
            }
        }else if(type.equals("GET")){
            request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + LoginConnectionController.getUserToken())
                    .build();
        }else if(type.equals("PATCH")){
            if(formBody != null){
                request = new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " +LoginConnectionController.getUserToken())
                        .patch(formBody)
                        .build();
            }else{
                request = new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " + LoginConnectionController.getUserToken())
                        .method("PATCH", null)
                        .build();
            }
        }else if(type.equals("PUT")){
            request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + LoginConnectionController.getUserToken())
                    .put(formBody)
                    .build();
        }else if(type.equals("DELETE")){
            request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + LoginConnectionController.getUserToken())
                    .delete()
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

    public static String getAccount(@NoNull String id) throws Exception {
        AnnotationParser.parse(id);
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(id);
        System.out.println(getCreator);
        return setOkHttpRequest(getCreator, null, false, "GET");
    }

    public static String getAccountFromConversation(@NoNull String conversationId, @NoNull String accountId) throws Exception{
        AnnotationParser.parse(conversationId, accountId);
        String getAccount = LoginConnectionController.getPath().concat("/Conversations/").concat(conversationId)
                .concat("/accounts/").concat(accountId);
        return setOkHttpRequest(getAccount, null, false, "GET");
    }

    public static String getAccountsFromConversation(@NoNull String conversationId) throws Exception {
        AnnotationParser.parse(conversationId);
        String getAccount = LoginConnectionController.getPath().concat("/Conversations/").concat(conversationId)
                .concat("/accounts");
        return setOkHttpRequest(getAccount, null, false, "GET");
    }

    public static String getCollaboratorAccount(@NoNull String id) throws Exception {
        AnnotationParser.parse(id);
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts/").concat(id);
        return setOkHttpRequest(getCreator, null, false, "GET");
    }

    public static String getBugs(@NoNull String state) throws Exception {
        AnnotationParser.parse(state);
        String getBugs = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs")
                .concat("?filter={\"where\":{\"state\":" + state + "}}");

        return setOkHttpRequest(getBugs, null, false, "GET");
    }

    public static String addBug(@NoNull String name, @NoNull String description, @NoNull String projectId) throws Exception {
        AnnotationParser.parse(name, description, projectId);
        String getBugRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId()).concat("/bugs");

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("description", description)
                .add("projectId", projectId)
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "POST");
    }

    public static String addProject(@NoNull String name) throws Exception {
        AnnotationParser.parse(name);
        String createProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId()).concat("/projects");
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();

        return setOkHttpRequest(createProjectRequest, formBody, false, "POST");

    }

    public static String addCollaboratorsToProject(@NoNull String collaboratorIdOrEmail, @NoNull String projectId) throws Exception {
        AnnotationParser.parse(collaboratorIdOrEmail, projectId);
        String addDirectorRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(projectId).concat("/accounts/rel/").concat(collaboratorIdOrEmail);
        RequestBody formBody = RequestBody.create(null, new byte[]{});
        return setOkHttpRequest(addDirectorRequest, formBody, false, "PUT");
    }

    public static String connection(@NoNull String email, @NoNull String password, @NoNull Label loginErrorConnectionLabel) throws Exception {
        AnnotationParser.parse(email, password, loginErrorConnectionLabel);
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();
        String response = setOkHttpRequest(LoginConnectionController.getPath().concat("accounts/login"), formBody, true, "POST");
        if(response == null){
            LabelHelper.setLabel(loginErrorConnectionLabel, "Les identifiants de connexion ne sont pas valide.", Pos.CENTER, "#FF0000", new Font("Book Antiqua", 16));
            return null;
        }else{
            return response;
        }
    }

    public static String createAccount(@NoNull String lastname, @NoNull String firstname, @NoNull String email, @NoNull String password, @NoNull Label registerErrorLabel) throws Exception {
        AnnotationParser.parse(lastname, firstname, email, password, registerErrorLabel);
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

    public static String getProjects(@NoNull String finished) throws Exception {
        AnnotationParser.parse(finished);
        String getProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId()).concat("/projects")
                .concat("?filter={\"where\":{\"finished\":" + finished + "}}");
        return setOkHttpRequest(getProjectRequest, null, false, "GET");
    }

    public static String addMessage(@NoNull String content) throws Exception {
        AnnotationParser.parse(content);
        String addMessageRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId())
                .concat("/messages");
        RequestBody formBody = new FormBody.Builder()
                .add("content", content)
                .add("accountId", LoginConnectionController.getUserId().toString())
                .build();
        return setOkHttpRequest(addMessageRequest, formBody, false, "POST");
    }

    public static String addMessageToConversation(@NoNull String content, @NoNull String conversationId) throws Exception {
        AnnotationParser.parse(content, conversationId);
        String addMessageRequest = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId).concat("/messages");
        RequestBody formBody = new FormBody.Builder()
                .add("content", content)
                .add("accountId", LoginConnectionController.getUserId().toString())
                .add("conversationId", conversationId)
                .build();
        return setOkHttpRequest(addMessageRequest, formBody, false, "POST");
    }

    public static String deleteConversation(@NoNull String conversationId) throws Exception {
        AnnotationParser.parse(conversationId);
        String deleteConversation = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/conversations/rel/").concat(conversationId);
        return setOkHttpRequest(deleteConversation, null, false, "DELETE");
    }

    public static String getMessagesFromConversation(@NoNull String conversationId) throws Exception{
        AnnotationParser.parse(conversationId);
        String addMessageRequest = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId).concat("/messages");
        return setOkHttpRequest(addMessageRequest, null, false, "GET");
    }

    public static String createConversation(@NoNull String name) throws Exception{
        AnnotationParser.parse(name);
        String createConversationRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/conversations");
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();
        return setOkHttpRequest(createConversationRequest, formBody, false, "POST");
    }

    public static String addCollaboratorToConversation(@NoNull String conversationId, String accountIdOrEmail) throws Exception{
        AnnotationParser.parse(conversationId, accountIdOrEmail);
        String addCollaborator = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId).concat("/accounts/rel/")
                .concat(accountIdOrEmail);
        RequestBody formBody = RequestBody.create(null, new byte[]{});
        return setOkHttpRequest(addCollaborator, formBody, false, "PUT");
    }

    public static String getConversations() throws Exception{
        String accountConversationsRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/conversations");
        return setOkHttpRequest(accountConversationsRequest, null, false, "GET");
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
                .add("closerId", LoginConnectionController.getUserId().toString())
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "PATCH");
    }

    public static String deleteBug(@NoNull String bugId) throws Exception {
        AnnotationParser.parse(bugId);
        String getBugRequest = LoginConnectionController.getPath().concat("Bugs/").concat(bugId);

        RequestBody formBody = new FormBody.Builder()
                .add("state", "1")
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "PATCH");
    }

    public static String updateProject(@NoNull String projectId) throws Exception {
        AnnotationParser.parse(projectId);
        String getProjectRequest = LoginConnectionController.getPath().concat("Projects/").concat(projectId);

        RequestBody formBody = new FormBody.Builder()
                .add("finished", "true")
                .build();

        return setOkHttpRequest(getProjectRequest, formBody, false, "PATCH");
    }

    public static String logout() throws Exception {
        String logout = LoginConnectionController.getPath().concat("accounts/logout");
        RequestBody formBody = RequestBody.create(null, new byte[]{});
        return setOkHttpRequest(logout, formBody, false, "POST");
    }

    public static String getFiles(@NoNull String projectId) throws Exception {
        AnnotationParser.parse(projectId);
        String getFiles = LoginConnectionController.getPath().concat("Containers/").concat(projectId).concat("/files");
        return setOkHttpRequest(getFiles, null, false, "GET");
    }

    public static void downloadFile(@NoNull String container, @NoNull String fileName) throws Exception{
        AnnotationParser.parse(container, fileName);
        Application application = new Application() {
            @Override
            public void start(Stage stage) {}
        };
        application.getHostServices().showDocument(LoginConnectionController.getPath().concat("Containers/").concat(container).concat("/download/").concat(fileName));
    }

    public static String addStep(@NoNull String projectId, @NoNull String name, @NoNull String date) throws Exception{
        AnnotationParser.parse(projectId, name, date);
        String addStepsRequest = "http://localhost:3000/api/accounts/".concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(projectId).concat("/steps");

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("date", date)
                .add("state", "En cours")
                .build();

        return setOkHttpRequest(addStepsRequest, formBody, false, "POST");
    }

    public static String createMeeting(@NoNull String subject, @NoNull String place, @NoNull String date) throws Exception {
        AnnotationParser.parse(subject, place, date);
        String createMeetingRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings");

        RequestBody formBody = new FormBody.Builder()
                .add("subject", subject)
                .add("place", place)
                .add("date", date)
                .build();

        return setOkHttpRequest(createMeetingRequest, formBody, false, "POST");
    }

    public static String setSummaryMeeting(@NoNull String summary, @NoNull String id) throws Exception{
        AnnotationParser.parse(summary, id);
        String updateMeetingRequest = LoginConnectionController.getPath().concat("/meetings/").concat(id);

        RequestBody formBody = new FormBody.Builder()
                .add("summary", summary)
                .build();

        return setOkHttpRequest(updateMeetingRequest, formBody, false, "PATCH");
    }

    public static String getMeetings() throws Exception {
        String getMeetingRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings")
                .concat("?filter[where][date][gt]=" + LocalDate.now() + "&filter[order]=date ASC");

        return setOkHttpRequest(getMeetingRequest, null, false, "GET");
    }

    public static String getOldMeetings() throws Exception {
        String getOldMeetingsRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings")
                .concat("?filter[where][date][lt]=" + LocalDate.now() + "&filter[order]=date ASC");

        return setOkHttpRequest(getOldMeetingsRequest, null, false, "GET");
    }

    public static String getMeetingById(String id) throws Exception{
        String getMeetingRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId().toString())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings/")
                .concat(id);
        return setOkHttpRequest(getMeetingRequest, null, false, "GET");
    }
}