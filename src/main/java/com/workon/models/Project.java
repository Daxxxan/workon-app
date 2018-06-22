package com.workon.models;

import java.util.ArrayList;

public class Project {
    private String id;
    private String name;
    private int director;
    private ArrayList<Step> steps = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

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

    public void addUserInUsers(User user){
        this.users.add(user);
    }
}
