package com.softmedialtda.votingapp.campaign.domain;

/**
 * Created by Agustin on 28/6/2018.
 */

public class Publication {
    int id;
    int idStudent;
    String text;
    String link;
    int idVote;
    String date;
    int idInstitution;
    String nameStudent;

    public Publication() {
    }

    public Publication(int id, int idStudent, String text, String link, int idVote, String date, int idInstitution, String nameStudent) {
        this.id = id;
        this.idStudent = idStudent;
        this.text = text;
        this.link = link;
        this.idVote = idVote;
        this.date = date;
        this.idInstitution = idInstitution;
        this.nameStudent = nameStudent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIdVote() {
        return idVote;
    }

    public void setIdVote(int idVote) {
        this.idVote = idVote;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(int idInstitution) {
        this.idInstitution = idInstitution;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }
}
