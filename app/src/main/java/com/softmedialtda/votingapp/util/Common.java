package com.softmedialtda.votingapp.util;

import android.content.Context;
import android.widget.Toast;

import com.softmedialtda.votingapp.login.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

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

}
