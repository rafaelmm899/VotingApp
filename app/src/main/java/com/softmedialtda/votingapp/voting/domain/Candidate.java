package com.softmedialtda.votingapp.voting.domain;

/**
 * Created by Agustin on 25/6/2018.
 */

public class Candidate {
    String name;
    String grade;
    String group;
    String image;
    int id;
    String tVoto;

    public Candidate() {
    }

    public Candidate(String name, String grade, String group, String image, int id) {
        this.name = name;
        this.grade = grade;
        this.group = group;
        this.image = image;
        this.id = id;
    }

    public Candidate(String name, String grade, String group, String image, int id, String tVoto) {
        this.name = name;
        this.grade = grade;
        this.group = group;
        this.image = image;
        this.id = id;
        this.tVoto = tVoto;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String gettVoto() {
        return tVoto;
    }

    public void settVoto(String tVoto) {
        this.tVoto = tVoto;
    }
}
