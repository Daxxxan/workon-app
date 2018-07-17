package com.workon.controllers;

import com.jfoenix.controls.JFXButton;
import com.workon.Main;
import com.workon.plugin.PluginInterface;
import com.workon.plugin.PluginLoader;
import com.workon.utils.HttpRequest;
import com.workon.utils.LabelHelper;
import com.workon.utils.LoadFXML;
import com.workon.utils.ParseRequestContent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ProjectsController {
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private JFXButton createBugButton;
    @FXML
    private JFXButton createMeetingButton;
    @FXML
    private JFXButton createDocumentationButton;
    @FXML
    private ScrollPane scrollPaneProjectList;
    @FXML
    private Menu pluginMenu;

    private static Menu mainPluginMenu;
    private static ScrollPane mainPane;
    private static ScrollPane projectListPane;
    private static JFXButton bug;
    private static JFXButton documentation;
    private static JFXButton meeting;

    @FXML
    public void initialize() throws Exception {
        setMainPane(mainScrollPane);
        setProjectListPane(scrollPaneProjectList);

        LoadFXML.loadFXMLInScrollPane("/fxml/vboxProject.fxml", scrollPaneProjectList, true, true);

        createBugButton.setDisable(true);
        createMeetingButton.setDisable(true);
        createDocumentationButton.setDisable(true);
        setBug(createBugButton);
        setMeeting(createMeetingButton);
        setDocumentation(createDocumentationButton);

        String contentAccountInformations = HttpRequest.getAccount(LoginConnectionController.getUserId().toString());
        String firstname = ParseRequestContent.getValueOf(Objects.requireNonNull(contentAccountInformations), "firstname");
        firstname = firstname.substring(1, firstname.length() - 1);

        Label mainLabel = LabelHelper.createLabel("Bienvenue ".concat(firstname).concat(" !"), Double.MAX_VALUE, new Font("Book Antiqua", 40), Pos.CENTER);
        mainGridPane.add(mainLabel, 2, 0, 4, 1);

        setMainPluginMenu(pluginMenu);

        Files.list(Paths.get("src/main/resources/pluginsWorkon/"))
            .forEach(path -> {
                MenuItem plugin = new MenuItem(path.getFileName().toString());
                plugin.setOnAction(event -> {
                    PluginLoader pluginLoader = new PluginLoader();
                    PluginInterface pluginInterface = null;
                    try {
                        pluginInterface = (PluginInterface) pluginLoader.loadPlugin(path.getFileName().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(pluginInterface != null){
                        pluginInterface.LoadPane(mainScrollPane);
                    }
                });
                pluginMenu.getItems().add(plugin);
            });
    }

    @FXML
    protected void handleCreateProjectView() throws IOException {
        LoadFXML.loadFXMLInScrollPane("/fxml/createProject.fxml", mainScrollPane, true, true);
    }

    @FXML
    protected void handlePluginMenuItem() throws Exception {
        LoadFXML.loadFXMLInScrollPane("/fxml/pluginPanel.fxml", mainScrollPane, true, true);
    }

    @FXML
    protected void handleCreateBugButton() throws Exception{
        LoadFXML.loadFXMLInScrollPane("/fxml/bugList.fxml", mainScrollPane, true, true);
    }

    @FXML
    protected void handleCreateMeetingButton() throws Exception {
        LoadFXML.loadFXMLInScrollPane("/fxml/meetingList.fxml", mainScrollPane, true, true);
    }

    @FXML
    protected void handleDocumentationButton() throws Exception {
        LoadFXML.loadFXMLInScrollPane("/fxml/fileList.fxml", mainScrollPane, true, true);
    }

    @FXML
    protected void handleMessagesList() throws Exception {
        LoadFXML.loadFXMLInScrollPane("/fxml/conversationList.fxml", mainScrollPane, true, true);
    }

    @FXML
    protected void handleLogout() throws Exception{
        HttpRequest.logout();
        Main.getMainStage().close();
        Platform.runLater( () -> {
            try {
                new Main().start( new Stage() );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static ScrollPane getMainPane() {
        return mainPane;
    }

    public static void setMainPane(ScrollPane mainPane) {
        ProjectsController.mainPane = mainPane;
    }

    public static ScrollPane getProjectListPane() {
        return projectListPane;
    }

    public static void setProjectListPane(ScrollPane projectListPane) {
        ProjectsController.projectListPane = projectListPane;
    }

    public static JFXButton getBug() {
        return bug;
    }

    public static void setBug(JFXButton bug) {
        ProjectsController.bug = bug;
    }

    public static JFXButton getDocumentation() {
        return documentation;
    }

    public static void setDocumentation(JFXButton documentation) {
        ProjectsController.documentation = documentation;
    }

    public static JFXButton getMeeting() {
        return meeting;
    }

    public static void setMeeting(JFXButton meeting) {
        ProjectsController.meeting = meeting;
    }

    public static Menu getMainPluginMenu() {
        return mainPluginMenu;
    }

    public static void setMainPluginMenu(Menu mainPluginMenu) {
        ProjectsController.mainPluginMenu = mainPluginMenu;
    }
}
