package com.robosoft.archana.instagramapplication.Modal;

/**
 * Created by archana on 23/2/16.
 */
public class Constants {

    public static final String CLIENT_ID = "289b08f3184e49c99992b8e46899abd4";
    public static final String CLIENT_SECRET = "4335b56123ed456e99cf0850437f7021";
    public static final String CALLBACK_URL = "https://www.google.co.in";
    //Authentication
    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
 //   Used for Authentication.
    private static final String TOKENURL ="https://api.instagram.com/oauth/access_token";
 //   Used for getting token and User details.
    public static final String APIURL = "https://api.instagram.com/v1";
    public static final String ACCESSTOKEN = "2972956137.289b08f.4518b8e436fd444195fdf1d47745a3c5";

   // Used to specify the API version which we are going to use.
    public static final String aurthUrlString = AUTHURL+"?client_id=" + CLIENT_ID+ "&redirect_uri="+ CALLBACK_URL + "&response_type=code&display=touch&scope=likes+comments+captions+relationships"+"&scope=public_content"+"&scope=follower_list";
    public static final String tokenURLString = TOKENURL +"?client_id="+CLIENT_ID +"&client_secret="+CLIENT_SECRET+"&redirect_uri="+CALLBACK_URL+"&grant_type=authorization_code";
    public static final String API_USERNAME = "mcasantosh";

}


