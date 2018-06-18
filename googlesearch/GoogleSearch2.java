/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlesearch;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author NDBDEV
 */
public class GoogleSearch2 {
    public static void main(String[] args) throws UnsupportedEncodingException, MalformedURLException, IOException {
         String google = "https://www.googleapis.com/customsearch/v1?key=AIzaSyByQwvWDcyFIAUsnmCnRymRsKyUZVlzPx8&cx=014942874880721995394:vzoyjtbvdqm&llr=ang_en&q=";
    String search = "stackoverflow";
    String charset = "UTF-8";

    URL url = new URL(google + URLEncoder.encode(search, charset));
    Reader reader = new InputStreamReader(url.openStream(), charset);
    GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

    // Show title and URL of 1st result.
    System.out.println(results.getResponseData().getResults().get(0).getTitle());
    System.out.println(results.getResponseData().getResults().get(0).getUrl());
    }
   
}
