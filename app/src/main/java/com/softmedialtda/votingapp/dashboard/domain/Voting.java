package com.softmedialtda.votingapp.dashboard.domain;

/**
 * Created by Agustin on 26/6/2018.
 */

public class Voting {
    int id;
    String name;
    int idInstitution;

    public Voting() {

    }

    public Voting(int id, String name, int idInstitution) {
        this.id = id;
        this.name = name;
        this.idInstitution = idInstitution;
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
