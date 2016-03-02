package com.robosoft.archana.instagramapplication.Modal;

/**
 * Created by archana on 23/2/16.
 */
public class Constatns {

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
   // public static final String tokenURLString = APIURL + "?client_id=" +CLIENT_ID+ "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + CALLBACK_URL + "&grant_type=authorization_code";


    public static final String tokenURLString = TOKENURL +"?client_id="+CLIENT_ID +"&client_secret="+CLIENT_SECRET+"&redirect_uri="+CALLBACK_URL+"&grant_type=authorization_code";

  //  https://api.instagram.com/oauth/access_token?client_id=289b08f3184e49c99992b8e46899abd4&client_secret=4335b56123ed456e99cf0850437f7021&redirect_uri=https://www.google.co.in&grant_type=authorization_code
   // public static String urlString = APIURL + "/users/"+ {User's Instagram Id} +"/media/recent/?access_token=" + {Instagram Access Token};
}


//https://api.instagram.com/oauth/authorize/ + "/users/"+ 2972956137 +"/media/recent/?access_token="2972956137.289b08f.4518b8e436fd444195fdf1d47745a3c5

/*
7d2660543b2b455dafed885043cf5ded
 */
/* authUrlString
https://api.instagram.com/oauth/authorize/?client_id=289b08f3184e49c99992b8e46899abd4&redirect_uri=https://www.google.co.in&response_type=code

*/
/*
tokenURLsTRING
 */
/*
https://www.google.co.in/?code=0c36d8baf1304aa9a6f027851a35aa27
authURLString = AUTHURL + "?client_id=" + CLIENT_ID+ "&redirect_uri=" +CALLBACK_URL  + "&response_type=code&display=touch&scope=likes+comments+relationships";

tokenURLString = TOKENURL + "?client_id=" + client_id + "&client_secret=" + client_secret + "&redirect_uri=" + CALLBACKURL + "&grant_type=authorization_code";
 */