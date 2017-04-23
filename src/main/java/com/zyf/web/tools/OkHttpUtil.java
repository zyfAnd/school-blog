package com.zyf.web.tools;


import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author : Zhangyanfu
 * Date ：2017.3.1
 */

public class OkHttpUtil {
    private final String TAG = "OkHttpUtil";
    private static OkHttpUtil mHttpUtil;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mOkHttpBuilder;

    private OkHttpUtil() {
        mOkHttpBuilder = new OkHttpClient.Builder();
        mOkHttpBuilder.cookieJar(new MyCookieJar());
        mOkHttpClient = mOkHttpBuilder.build();
    }

    private static OkHttpUtil getInstance() {
        if (mHttpUtil == null) {
            synchronized (OkHttpUtil.class) {
                if (mHttpUtil == null) {
                    mHttpUtil = new OkHttpUtil();
                }
            }
        }
        return mHttpUtil;
    }

    /**
     * 同步的get请求 返回response对象
     *
     * @param url
     * @return
     * @throws IOException
     */
    private Response _getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    /**
     * 同步的get请求 返回一个字符串
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String _getSyncString(String url) throws IOException {
        return _getSync(url).body().string();
    }


    /**
     * 同步的post请求 返回Response对象
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private Response _postSync(String url, RequestData[] params, RequestData... headers) throws IOException {
        Request request = buildPostReqeust(url, params, headers);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的 post请求 返回String字符串
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws IOException
     */
    private String _postSyncString(String url, RequestData[] params, RequestData... headers) throws IOException {
        Response response = _postSync(url, params, headers);
        return response.body().string();
    }



    private RequestData[] mapToRequestData(Map<String, String> params) {
        int index = 0;
        if (params == null) {
            return new RequestData[0];
        }
        int size = params.size();
        RequestData[] res = new RequestData[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            res[index++] = new RequestData(entry.getKey(), entry.getValue());
        }
        return res;
    }

    /**
     * 构建post请求参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private Request buildPostReqeust(String url, RequestData[] params, RequestData... headers) {
        if (headers == null) {
            headers = new RequestData[0];
        }
        Headers.Builder headersBuilder = new Headers.Builder();
        for (RequestData header : headers) {
            headersBuilder.add(header.key, header.value);
        }
        Headers requestHeaders = headersBuilder.build();
        if (params == null) {
            params = new RequestData[0];
        }
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (RequestData param : params) {
//            Log.i(TAG, "buildPostReqeust: "+" key:"+param.key +" value:"+param.value);
            formBodyBuilder.add(param.key, param.value);

        }
        FormBody requestFormBody = formBodyBuilder.build();
        return new Request.Builder().url(url).headers(requestHeaders).post(requestFormBody).build();

    }



    /**************************************供外部调用的方法***********************************************/
    /**
     * 请求的接口
     */
    public interface ResultCallback {
        void onError(Call call, Exception e);
        void onSuccess(byte[] response);
    }

    /**
     * post请求参封装
     */
    public static class RequestData {
        String key;
        String value;

        public RequestData() {
        }

        public RequestData(String key, String value) {
            this.value = value;
            this.key = key;
        }
    }
    public static Response getSync(String url) throws IOException {
        return getInstance()._getSync(url);
    }
    public static String getSyncString(String url) throws IOException {
        return getInstance()._getSyncString(url);
    }
    public static Response postSync(String url,RequestData[] params,RequestData[] headers) throws IOException {
       return getInstance()._postSync(url,params,headers);
    }
    public static String postSyncString(String url,RequestData[] params,RequestData[] headers) throws IOException {
        return getInstance()._postSyncString(url,params,headers);
    }
}
