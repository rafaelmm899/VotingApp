package com.softmedialtda.votingapp.dashboard.domain;

import java.io.Serializable;

/**
 * Created by Agustin on 26/6/2018.
 */

public class Voting implements Serializable {
    int id;
    String name;
    int idInstitution;
    int userVoted;

    public Voting() {

    }

    public Voting(int id, String name, int idInstitution, int userVoted) {
        this.id = id;
        this.name = name;
        this.idInstitution = idInstitution;
        this.userVoted = userVoted;
    }

    public int getUserVoted() {
        return userVoted;
    }

    public void setUserVoted(int userVoted) {
        this.userVoted = userVoted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(int idInstitution) {
        this.idInstitution = idInstitution;
    }
}
