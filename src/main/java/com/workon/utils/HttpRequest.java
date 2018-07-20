package com.workon.utils;

import com.sun.javafx.tools.packager.Log;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class HttpRequest {

    /**
     * Envoie la requete HTTP avec OkHttp.
     *
     * @param url
     *        url vers l'api
     * @param formBody
     *        Le body de la requete HTTP
     * @param connection
     *        Indique s'il s'agit d'une requete qui servira a se connecter a un compte utilisateur
     * @param type
     *        Type de requete
     * @return String
     *         le retour de la requete HTTP
     */
    public static String setOkHttpRequest(@NoNull String url, RequestBody formBody, @NoNull Boolean connection, @NoNull String type) {
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
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Objects.requireNonNull(response).code() != 200){
            return null;
        }else{
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }finally {
                response.body().close();
            }
        }
    }

    /**
     * Recupere les collaborateurs d'un projet
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getCollaborators() {
        String getProjectCollaborators = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts");
        return setOkHttpRequest(getProjectCollaborators, null, false, "GET");
    }

    /**
     * Recupere le bug courant via son ID
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getCurrentBugs() {
        String getBug = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs/")
                .concat(CreateProjectController.getProject().getCurrentBugId());
        return setOkHttpRequest(getBug, null, false, "GET");

    }

    /**
     * Recupere un account grace a son ID
     *
     * @param id
     *        Id du compte
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getAccount(@NoNull String id) {
        AnnotationParser.parse(id);
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(id);
        return setOkHttpRequest(getCreator, null, false, "GET");
    }

    /**
     * Recupere un account d'une conversation
     *
     * @param conversationId
     *        ID de la conversation
     * @param accountId
     *        ID du compte a recuperer
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getAccountFromConversation(@NoNull String conversationId, @NoNull String accountId) {
        AnnotationParser.parse(conversationId, accountId);
        String getAccount = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId)
                .concat("/accounts/").concat(accountId);
        return setOkHttpRequest(getAccount, null, false, "GET");
    }

    /**
     * Recupere les accounts d'une conversation
     *
     * @param conversationId
     *        ID de la conversation
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getAccountsFromConversation(@NoNull String conversationId) {
        AnnotationParser.parse(conversationId);
        String getAccount = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId)
                .concat("/accounts");
        return setOkHttpRequest(getAccount, null, false, "GET");
    }

    /**
     * Recupere un account d'un projet
     *
     * @param id
     *        ID de l'account
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getCollaboratorAccount(@NoNull String id) {
        AnnotationParser.parse(id);
        String getCreator = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts/").concat(id);
        return setOkHttpRequest(getCreator, null, false, "GET");
    }

    /**
     * Recupere les bugs d'un projet
     *
     * @param state
     *        Etat des projets a recuperer
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getBugs(@NoNull String state) {
        AnnotationParser.parse(state);
        String getBugs = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/bugs")
                .concat("?filter={\"where\":{\"state\":" + state + "}}");

        return setOkHttpRequest(getBugs, null, false, "GET");
    }

    /**
     * Ajouter un bug a un projet
     *
     * @param name
     *        Nom du bug
     * @param description
     *        Description du bug
     * @param projectId
     *        L'id du projet
     * @return String
     *         le retour de la requete HTTP
     */
    public static String addBug(@NoNull String name, @NoNull String description, @NoNull String projectId) {
        AnnotationParser.parse(name, description, projectId);
        String getBugRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId()).concat("/bugs");

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("description", description)
                .add("projectId", projectId)
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "POST");
    }

    /**
     * Creer un projet
     *
     * @param name
     *        Nom du projet
     * @return String
     *         le retour de la requete HTTP
     */
    public static String addProject(@NoNull String name) {
        AnnotationParser.parse(name);
        String createProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId()).concat("/projects");
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();

        return setOkHttpRequest(createProjectRequest, formBody, false, "POST");

    }

    /**
     * Ajout d'un collaborateur a un projet
     *
     * @param collaboratorIdOrEmail
     *        Identifiant de l'utilisateur (email ou ID)
     * @param projectId
     *        ID du projet
     * @return String
     *         le retour de la requete HTTP
     */
    public static String addCollaboratorsToProject(@NoNull String collaboratorIdOrEmail, @NoNull String projectId) {
        AnnotationParser.parse(collaboratorIdOrEmail, projectId);
        String addDirectorRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(projectId).concat("/accounts/rel/").concat(collaboratorIdOrEmail);
        RequestBody formBody = RequestBody.create(null, new byte[]{});
        return setOkHttpRequest(addDirectorRequest, formBody, false, "PUT");
    }

    /**
     * Connexion d'un utilisateur
     *
     * @param email
     *        Email de l'utilisateur
     * @param password
     *        Password de l'utilisateur
     * @param loginErrorConnectionLabel
     *        Le lobel a renseigner en cas d'echec de connexion
     * @return String
     *         le retour de la requete HTTP
     */
    public static String connection(@NoNull String email, @NoNull String password, @NoNull Label loginErrorConnectionLabel) {
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

    /**
     * Creation d'un compte a un utilisateur
     *
     * @param lastname
     *        Nom de l'utilisateur
     * @param firstname
     *        Prenom de l'utilisateur
     * @param email
     *        Email de l'utilisateur
     * @param password
     *        Mot de passe de l'utilisateur
     * @param registerErrorLabel
     *        Label a renseigner en cas d'echec de creation d'un compte
     * @return String
     *         le retour de la requete HTTP
     */
    public static String createAccount(@NoNull String lastname, @NoNull String firstname, @NoNull String email, @NoNull String password, @NoNull Label registerErrorLabel) {
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

    /**
     * Recupere les jalons d'un projet
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getProjectSteps() {
        String getProjectSteps = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/steps")
                .concat("?filter[order]=date ASC");
        return setOkHttpRequest(getProjectSteps, null, false, "GET");
    }

    /**
     * Recupere les projets fini ou non
     *
     * @param finished
     *        Etat du projet
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getProjects(@NoNull String finished) {
        AnnotationParser.parse(finished);
        String getProjectRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId()).concat("/projects")
                .concat("?filter={\"where\":{\"finished\":" + finished + "}}");
        return setOkHttpRequest(getProjectRequest, null, false, "GET");
    }

    /**
     * Ajout d'un message dans un bug
     *
     * @param content
     *        Contenu du message
     * @return String
     *         le retour de la requete HTTP
     */
    public static String addMessage(@NoNull String content) {
        AnnotationParser.parse(content);
        String addMessageRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId())
                .concat("/messages");
        RequestBody formBody = new FormBody.Builder()
                .add("content", content)
                .add("accountId", LoginConnectionController.getUserId())
                .build();
        return setOkHttpRequest(addMessageRequest, formBody, false, "POST");
    }

    /**
     * Ajout d'un message dans une conversation
     *
     * @param content
     *        Contenu du message
     * @param conversationId
     *        ID de la conversation
     * @return String
     *         le retour de la requete HTTP
     */
    public static String addMessageToConversation(@NoNull String content, @NoNull String conversationId) {
        AnnotationParser.parse(content, conversationId);
        String addMessageRequest = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId).concat("/messages");
        RequestBody formBody = new FormBody.Builder()
                .add("content", content)
                .add("accountId", LoginConnectionController.getUserId().toString())
                .add("conversationId", conversationId)
                .build();
        return setOkHttpRequest(addMessageRequest, formBody, false, "POST");
    }

    /**
     * Supprime une conversation
     *
     * @param conversationId
     *        ID de la conversation
     * @return String
     *         le retour de la requete HTTP
     */
    public static String deleteConversation(@NoNull String conversationId) {
        AnnotationParser.parse(conversationId);
        String deleteConversation = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/conversations/rel/").concat(conversationId);
        return setOkHttpRequest(deleteConversation, null, false, "DELETE");
    }

    /**
     * Recupere les messages d'une conversation
     *
     * @param conversationId
     *        ID de la conversation
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getMessagesFromConversation(@NoNull String conversationId) {
        AnnotationParser.parse(conversationId);
        String addMessageRequest = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId).concat("/messages");
        return setOkHttpRequest(addMessageRequest, null, false, "GET");
    }

    /**
     * Creation d'une conversation
     *
     * @param name
     *        Nom de la conversation
     * @return String
     *         le retour de la requete HTTP
     */
    public static String createConversation(@NoNull String name) {
        AnnotationParser.parse(name);
        String createConversationRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/conversations");
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();
        return setOkHttpRequest(createConversationRequest, formBody, false, "POST");
    }

    /**
     * Ajout d'un collaborateur a une conversation
     *
     * @param conversationId
     *        ID de la conversation
     * @param accountIdOrEmail
     *        Identifiant du compte a ajouter (email ou ID)
     * @return String
     *         le retour de la requete HTTP
     */
    public static String addCollaboratorToConversation(@NoNull String conversationId, String accountIdOrEmail) {
        AnnotationParser.parse(conversationId, accountIdOrEmail);
        String addCollaborator = LoginConnectionController.getPath().concat("Conversations/").concat(conversationId).concat("/accounts/rel/")
                .concat(accountIdOrEmail);
        RequestBody formBody = RequestBody.create(null, new byte[]{});
        return setOkHttpRequest(addCollaborator, formBody, false, "PUT");
    }

    /**
     * Recupere les conversations d'un account
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getConversations() {
        String accountConversationsRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/conversations");
        return setOkHttpRequest(accountConversationsRequest, null, false, "GET");
    }

    /**
     * Recupere les messages d'un bug
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getBugMessages() {
        String getMessageRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId())
                .concat("/messages");
        return setOkHttpRequest(getMessageRequest, null, false, "GET");
    }

    /**
     * Change l'etat d'un bug (cloture)
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String updateBug() {
        String getBugRequest = LoginConnectionController.getPath().concat("Bugs/").concat(CreateProjectController.getProject().getCurrentBugId());

        RequestBody formBody = new FormBody.Builder()
                .add("state", "1")
                .add("closerId", LoginConnectionController.getUserId())
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "PATCH");
    }

    /**
     * Supprime un bug
     *
     * @param bugId
     *        ID du bug a supprimer
     * @return String
     *         le retour de la requete HTTP
     */
    public static String deleteBug(@NoNull String bugId) {
        AnnotationParser.parse(bugId);
        String getBugRequest = LoginConnectionController.getPath().concat("Bugs/").concat(bugId);

        RequestBody formBody = new FormBody.Builder()
                .add("state", "1")
                .build();

        return setOkHttpRequest(getBugRequest, formBody, false, "PATCH");
    }

    /**
     * Change l'etat d'un projet (fini)
     *
     * @param projectId
     *        ID du projet
     * @return String
     *         le retour de la requete HTTP
     */
    public static String updateProject(@NoNull String projectId) {
        AnnotationParser.parse(projectId);
        String getProjectRequest = LoginConnectionController.getPath().concat("Projects/").concat(projectId);

        RequestBody formBody = new FormBody.Builder()
                .add("finished", "true")
                .build();

        return setOkHttpRequest(getProjectRequest, formBody, false, "PATCH");
    }

    /**
     * Deconnexion d'un utilisateur
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String logout() {
        String logout = LoginConnectionController.getPath().concat("accounts/logout");
        RequestBody formBody = RequestBody.create(null, new byte[]{});
        return setOkHttpRequest(logout, formBody, false, "POST");
    }

    /**
     * Recupere les fichiers d'un projet
     *
     * @param projectId
     *        ID du projet
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getFiles(@NoNull String projectId) {
        AnnotationParser.parse(projectId);
        String getFiles = LoginConnectionController.getPath().concat("Containers/").concat(projectId).concat("/files");
        return setOkHttpRequest(getFiles, null, false, "GET");
    }

    /**
     * Telechargement d'un fichier
     *
     * @param container
     *        ID du projet dans lequel se trouve le document
     * @param fileName
     *        Nom du fichier a telecharger
     */
    public static void downloadFile(@NoNull String container, @NoNull String fileName) {
        AnnotationParser.parse(container, fileName);
        Application application = new Application() {
            @Override
            public void start(Stage stage) {}
        };
        application.getHostServices().showDocument(LoginConnectionController.getPath().concat("Containers/").concat(container).concat("/download/").concat(fileName));
    }

    /**
     * Ajout d'un jalon a un projet
     *
     * @param projectId
     *        ID du projet
     * @param name
     *        Nom du jalon
     * @param date
     *        Date du jalon
     * @return String
     *         le retour de la requete HTTP
     */
    public static String addStep(@NoNull String projectId, @NoNull String name, @NoNull String date) {
        AnnotationParser.parse(projectId, name, date);
        String addStepsRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(projectId).concat("/steps");

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("date", date)
                .add("state", "En cours")
                .build();

        return setOkHttpRequest(addStepsRequest, formBody, false, "POST");
    }

    /**
     * Creer une reunion
     *
     * @param subject
     *        Sujet de la reunion
     * @param place
     *        Emplacement de la reunion
     * @param date
     *        Date de la reunion
     * @return String
     *         le retour de la requete HTTP
     */
    public static String createMeeting(@NoNull String subject, @NoNull String place, @NoNull String date) {
        AnnotationParser.parse(subject, place, date);
        String createMeetingRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings");

        RequestBody formBody = new FormBody.Builder()
                .add("subject", subject)
                .add("place", place)
                .add("date", date)
                .build();

        return setOkHttpRequest(createMeetingRequest, formBody, false, "POST");
    }

    /**
     * Ajoute un resume a une reunion
     *
     * @param summary
     *        Resume de la reunion
     * @param id
     *        ID de la reunion
     * @return String
     *         le retour de la requete HTTP
     */
    public static String setSummaryMeeting(@NoNull String summary, @NoNull String id) {
        AnnotationParser.parse(summary, id);
        String updateMeetingRequest = LoginConnectionController.getPath().concat("meetings/").concat(id);

        RequestBody formBody = new FormBody.Builder()
                .add("summary", summary)
                .build();

        return setOkHttpRequest(updateMeetingRequest, formBody, false, "PATCH");
    }

    /**
     * Recupere toutes les reunions au dela de la date d'aujourd'hui
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getMeetings() {
        String getMeetingRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings")
                .concat("?filter[where][date][gt]=" + LocalDate.now() + "&filter[order]=date ASC");

        return setOkHttpRequest(getMeetingRequest, null, false, "GET");
    }

    /**
     * Recupere toutes les reunions avant la date d'aujourd'hui
     *
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getOldMeetings() {
        String getOldMeetingsRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings")
                .concat("?filter[where][date][lt]=" + LocalDate.now() + "&filter[order]=date ASC");

        return setOkHttpRequest(getOldMeetingsRequest, null, false, "GET");
    }

    /**
     * Recupere une reunion par son id
     *
     * @param id
     *        ID de la reunion
     * @return String
     *         le retour de la requete HTTP
     */
    public static String getMeetingById(@NoNull String id) {
        AnnotationParser.parse(id);
        String getMeetingRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/meetings/")
                .concat(id);
        return setOkHttpRequest(getMeetingRequest, null, false, "GET");
    }

    /**
     * Supprime un collaborateur d'un projet par son id
     *
     * @param collaboratorId
     *        ID du collaborateur
     * @return String
     *         le retour de la requete HTTP
     */
    public static String removeCollaboratorFromProject(@NoNull String collaboratorId){
        AnnotationParser.parse(collaboratorId);
        String removeCollaboratorRequest = LoginConnectionController.getPath().concat("accounts/").concat(LoginConnectionController.getUserId())
                .concat("/projects/").concat(CreateProjectController.getProject().getId()).concat("/accounts/rel/")
                .concat(collaboratorId);
        return setOkHttpRequest(removeCollaboratorRequest, null, false, "DELETE");
    }

    public static String createTask(String name){
        AnnotationParser.parse(name);
        String createTaskRequest = LoginConnectionController.getPath().concat("Steps/").concat(CreateProjectController.getProject().getCurrentStepId())
                .concat("/tasks");
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();

        return setOkHttpRequest(createTaskRequest, formBody, false, "POST");
    }

    public static String getTasksFromStep(){
        String getTaskRequest = LoginConnectionController.getPath().concat("Steps/").concat(CreateProjectController.getProject().getCurrentStepId())
                .concat("/tasks");
        return setOkHttpRequest(getTaskRequest, null, false, "GET");
    }

    public static String deleteTasksFromStep(String taskId){
        String removeTaskRequest = LoginConnectionController.getPath().concat("Steps/").concat(CreateProjectController.getProject().getCurrentStepId())
                .concat("/tasks/").concat(taskId);
        return setOkHttpRequest(removeTaskRequest, null, false, "DELETE");
    }

    public static String updateTasksFromStep(String taskId){
        String updateTaskRequest = LoginConnectionController.getPath().concat("Tasks/").concat(taskId);
        RequestBody formBody = new FormBody.Builder()
                .add("state", "1")
                .build();
        return setOkHttpRequest(updateTaskRequest, formBody, false, "PATCH");
    }

    public static String updateStateStep(String state){
        String updateStepState = LoginConnectionController.getPath().concat("Steps/").concat(CreateProjectController.getProject().getCurrentStepId());
        RequestBody formBody = new FormBody.Builder()
                .add("state", state)
                .build();
        return setOkHttpRequest(updateStepState, formBody, false, "PATCH");
    }
}