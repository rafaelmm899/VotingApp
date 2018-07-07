package com.softmedialtda.votingapp.util;

import android.graphics.Color;

/**
 * Created by Agustin on 18/6/2018.
 */

public class Constants {

    //public static final String SOFTMEDIAAPPLICATIONDOMAIN = "http://169.254.170.188:80/softmediapplication/public/api/";
    public static final String SOFTMEDIAAPPLICATIONDOMAIN = "http://192.168.43.228:80/softmediapplication/public/api/";
    //public static final String DOMAIN  = "http://192.168.43.228:80/SIIWEB/public/api/";
    //public static final String DOMAIN  = "http://10.0.2.2:80/SIIWEB/public/api/";
    //public static final String DOMAIN  = "http://169.254.170.188:80/SIIWEB/public/api/";
    public static final String DOMAIN  = "http://192.168.43.228:80/SIIWEB/public/api/";
    //public static final String DOMAIN  = "http://sii.softmedialtda.com/api/";
    public static final String SERVER  = "http://192.168.43.228:80/SIIWEB/public/";
    public static final String YOUTUBEKEY = "AIzaSyAdBxj0HutQ0cddMjYPbCKnAVTbckD944s";
    public static final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    public static final String[] videoIdRegex = { "\\?vi?=([^&]*)","watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};
    public static final int[] COLORS = {Color.BLUE,Color.CYAN,Color.GREEN,Color.YELLOW,Color.RED};
}
