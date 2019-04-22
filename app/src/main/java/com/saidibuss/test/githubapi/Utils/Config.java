package com.saidibuss.test.githubapi.Utils;

/*
* Class designed for store any static information
* */

public class Config {

    public static final String URL_USER_QUERY = "https://api.github.com/search/users?q=in:login+";
    // One minute to refresh remaining
    public static final String URL_LIMIT_QUERY = "https://api.github.com/rate_limit";


    public static final String KEY_USERNAME = "login";
    public static final String KEY_IMAGE = "avatar_url";

    public static final String KEY_LIMIT = "remaining";


}
