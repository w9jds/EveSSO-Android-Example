package com.w9jds.evesso.Classes;

/**
 * Created by Jeremy Shore on 10/19/2014.
 */
public class Utility {

    public static String buildAuthUrL() {
            return String.format(
                    "%s/oauth/authorize?response_type=code&redirect_uri=%s&client_id=%s&scope=",
                    Constants.BASE_URI,
                    Constants.CALLBACK,
                    Constants.CLIENT_ID);
    }

}
