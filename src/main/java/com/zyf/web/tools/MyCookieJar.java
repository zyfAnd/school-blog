package com.zyf.web.tools;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zfy on 2017/3/6.
 */

public class MyCookieJar implements CookieJar{
    private static List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies = cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (cookies!=null)
        {
            return cookies;
        }
        else
        {
            return new ArrayList<Cookie>();
        }
    }
    public static void resetCookies()
    {
        cookies=null;
    }
}
