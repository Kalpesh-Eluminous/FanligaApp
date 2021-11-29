package com.fanliga.utils;

public class AppConstant {


    // Volley Methods Used below
    public static int GET = 0;
    public static int POST = 1;
    public static int PUT = 2;
    public static int DELETE = 3;
    public static int HEAD = 4;
    public static int OPTIONS = 5;
    public static int TRACE = 6;
    public static int PATCH = 7;

    // web service app token
    public static String APP_TOKEN = "APP-TOKEN";
    public static String TOKEN_VALUE = "AK5SnkI8lDyPqBTLVxji";

    public static String AUTHORIZATION = "Authorization";  //key
    public static String API_ACCESS_TOKEN = "API_ACCESS_TOKEN"; //value

    // for session data
    public static String USER_ID = "USER_ID";
    public static String F_NAME = "F_NAME";
    public static String L_NAME = "L_NAME";
    public static String EMAIL = "EMAIL";
    public static String MOBILE_NUM = "MOBILE_NUM";
    public static String USER_ROLE = "USER_ROLE";

    // for bundle data and keys
    public static String ADS_URL = "ADS_URL";
    public static String SPORT_ID = "SPORT_ID";
    public static String LEAGUE_ID = "LEAGUE_ID";
    public static String PROFILE_DATA = "PROFILE_DATA";


    // APIs base URL - TEST SERVER
    //public static String API_BASE_URL = "http://49.248.144.235/lv/fanliga/api/v1/";
    public static String API_BASE_URL = "https://backend.fanliga.app/api/v1/";

    // APIs base URL - LIVE SERVER
    // public static String API_BASE_URL = "http://49.248.144.235/lv/fanliga/api/v1/";

    // video file base path URL
    public static String VIDEO_BASE_URL = "http://49.248.144.235/lv/fanliga/storage/app/";

    // web API URL's below
    public static String LOGIN = API_BASE_URL + "login";
    public static String REGISTER = API_BASE_URL + "signup";
    public static String GET_HOME_DATA = API_BASE_URL + "home-screen";
    public static String GET_VIDEO_DETAILS = API_BASE_URL + "video-details";
    public static String ADD_COMMENT = API_BASE_URL + "user/add-comment";
    public static String GET_PROFILE_DATA = API_BASE_URL + "get-profile";
    public static String DELETE_COMMENT = API_BASE_URL + "user/delete-comment";
    public static String GET_CATEGORIES = API_BASE_URL + "get-categories";
    public static String GET_SPORTS = API_BASE_URL + "get-sports";
    public static String GET_LEAGUES = API_BASE_URL + "get-leagues";
    public static String GET_CLUBS = API_BASE_URL + "get-clubs";
    public static String GET_FAVORITES = API_BASE_URL + "get-clubs";
    public static String UPDATE_PROFILE = API_BASE_URL + "update-profile";
    public static String ADD_FAVOURITE_SPORT = API_BASE_URL + "user/add-favorite-sport";
    public static String ADD_FAVOURITE_LEAGUE = API_BASE_URL + "user/add-favorite-league";
    public static String ADD_FAVOURITE_CLUB = API_BASE_URL + "user/add-favorite-club";
    public static String ADD_FAVOURITE_CATEGORY = API_BASE_URL + "user/add-favorite-category";
    public static String ADD_FAVOURITE_MODERATOR = API_BASE_URL + "user/add-favorite-moderator";
    public static String ADD_FAVOURITE_TEAM = API_BASE_URL + "user/add-favorite-team";
    public static String GET_MODERATORS = API_BASE_URL + "get-moderators";
    public static String LIKE_VIDEO = API_BASE_URL + "user/add-favorite";
    public static String GET_FAVORITES_LIST = API_BASE_URL + "user/get-favorites-list";

    public static String GET_VIDEOLIST = API_BASE_URL + "user/get-videos";

    public static String MenuSeleted="none";
}
