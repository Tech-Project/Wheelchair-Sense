package com.neux.proj.insurance.utility;

import com.neux.proj.insurance.MainWebViewActivity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import com.neux.proj.insurance.MainWebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: titan
 * Date: 2014/1/14
 * Time: ¤U¤È 3:16
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtils {
    public static String getHtml(String url) throws Exception {

        HttpClient httpClient =  new  DefaultHttpClient();
        HttpGet postMethod =  new HttpGet(url);
//        HttpPost postMethod =  new HttpPost(url);
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        postMethod.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

        HttpResponse response = httpClient.execute(postMethod);


        return null;
//        return getHtml(url, HTTP.UTF_8, null, true);
    }

    public static String getHtml(String url, String encoder) throws Exception {
        return getHtml(url, encoder, null, true);
    }

    public static String getHtml(String url, Map<String, String> paramMap) throws Exception {
        return getHtml(url, HTTP.UTF_8, paramMap, true);
    }

    public static String getHtml(String url, Map<String, String> paramMap, String encoder) throws Exception {
        return getHtml(url, encoder, paramMap, true);
    }

    public static String getHtml(String url, String encoder, Map<String, String> paramMap, boolean isSleep) throws Exception {

        DialogUtils.openDialog(MainWebViewActivity.m_WV.getContext(),"1");

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        DialogUtils.openDialog(MainWebViewActivity.m_WV.getContext(),"2");

        String ret = null;
        try {
            if (paramMap != null) {
                for (String key : paramMap.keySet()) {
                    params.add(new BasicNameValuePair(key, paramMap.get(key)));
                }
                post.setEntity(new UrlEncodedFormEntity(params, encoder));
            }

            DialogUtils.openDialog(MainWebViewActivity.m_WV.getContext(),"3");

            HttpResponse response = client.execute(post);

            DialogUtils.openDialog(MainWebViewActivity.m_WV.getContext(),"4");

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                DialogUtils.openDialog(MainWebViewActivity.m_WV.getContext(),"5");
                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw e;
        }

        return ret;
    }
}
