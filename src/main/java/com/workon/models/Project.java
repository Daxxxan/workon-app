package com.workon.models;

import java.util.ArrayList;

public class Project {
    private String id;
    private String name;
    private int director;
    private String currentBugId;
    private String currentBugName;
    private ArrayList<Step> steps = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Bug> bugs = new ArrayList<>();

    public Project(){}

    public Project(String name, int director) {
        this.name = name;
        this.director = director;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDirector() {
        return director;
    }

    public void setDirector(int director) {
        this.director = director;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void addUserToArrayList(User user){
        this.users.add(user);
    }

    public ArrayList<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(ArrayList<Bug> bugs) {
        this.bugs = bugs;
    }

    public void addBugToArrayList(Bug bug){
        this.bugs.add(bug);
    }

    public String getCurrentBugId() {
        return currentBugId;
    }

    public void setCurrentBugId(String currentBugId) {
        this.currentBugId = currentBugId;
    }

    public String getCurrentBugName() {
        return currentBugName;
    }

    public void setCurrentBugName(String currentBugName) {
        this.currentBugName = currentBugName;
    }
}
