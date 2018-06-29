package com.softmedialtda.votingapp.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.softmedialtda.votingapp.campaign.domain.Publication;
import com.softmedialtda.votingapp.dashboard.domain.Voting;
import com.softmedialtda.votingapp.login.domain.User;
import com.softmedialtda.votingapp.voting.domain.Candidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Agustin on 18/6/2018.
 */

public class Common {
    public static void showMessage(String msg, Context context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static User convertJsonObjectToUserObject(JSONObject json){
        User user = new User();
        try{
            switch (json.getString("TYPE").trim()){
                case "SUPERADMIN" :
                    user = new User(json.getString("USERNAME").trim(),Integer.parseInt(json.getString("ID").trim()),json.getString("NAME").trim(),json.getString("LASTNAME").trim(),json.getString("TYPE").trim(),json.getString("IMAGE"));
                    break;
                case "Student":
                case "Functionary":
                    user = new User(json.getString("USERNAME").trim(),Integer.parseInt(json.getString("ID").trim()),json.getString("NOMBRE1").trim(),json.getString("NOMBRE2").trim(),json.getString("APELLIDO1").trim(),json.getString("APELLIDO2").trim(),json.getString("NO_DOCUMENTO").trim(),json.getString("TYPE").trim(),Integer.parseInt(json.getString("ID_INSTITUCION").trim()),Integer.parseInt(json.getString("ID_PER").trim()),json.getString("IMAGE"));
                    break;
                case "Attendant":
                    user = new User(json.getString("USERNAME").trim(),Integer.parseInt(json.getString("ID").trim()),json.getString("NOMBRE1").trim(),json.getString("NOMBRE2").trim(),json.getString("APELLIDO1").trim(),json.getString("APELLIDO2").trim(),json.getString("NO_DOCUMENTO").trim(),Integer.parseInt(json.getString("ID_ALUMNO").trim()),json.getString("NOMBRE1_ALUMNO").trim(),json.getString("NOMBRE2_ALUMNO").trim(),json.getString("APELLIDO1_ALUMNO").trim(),json.getString("APELLIDO2_ALUMNO").trim(),json.getString("NO_DOCUMENTO_ALUMNO").trim(),json.getString("TYPE").trim(),Integer.parseInt(json.getString("ID_INSTITUCION").trim()),Integer.parseInt(json.getString("ID_PER").trim()),json.getString("IMAGE"));
                    break;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<Candidate> getListCandidate(JSONArray json){
        ArrayList<Candidate> list = new ArrayList<Candidate>();
        try {
            for (int i = 0; i < json.length(); i++){
                JSONObject data = json.getJSONObject(i);
                list.add(new Candidate(data.getString("NOMBRE"),data.getString("GRADO"),data.getString("GRUPO"),data.getString("IMAGE"),Integer.parseInt(data.getString("ID"))));
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Publication> getListPublication(JSONArray json){
        ArrayList<Publication> list = new ArrayList<Publication>();
        try {
            for (int i = 0; i < json.length(); i++){
                JSONObject data = json.getJSONObject(i);
                list.add(new Publication(Integer.parseInt(data.getString("ID")),Integer.parseInt(data.getString("IDALUMNO")),data.getString("TEXT"),data.getString("LINK"),Integer.parseInt(data.getString("IDVOTING")),data.getString("DATE"),Integer.parseInt(data.getString("IDINSTITUCION")),data.getString("NOMBRE"),data.getString("IMAGE")));
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return list;
    }

    public static Voting getCurrentVotes(JSONObject json){
        Voting object = new Voting();
        try {
            object.setId(Integer.parseInt(json.getString("ID")));
            object.setName(json.getString("NOMBRE"));
            object.setIdInstitution(Integer.parseInt(json.getString("IDINSTITUCION")));
            object.setUserVoted(Integer.parseInt(json.getString("VOTO")));
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return object;
    }

}
