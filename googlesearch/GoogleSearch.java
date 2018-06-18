/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlesearch;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author NDBDEV
 */
public class GoogleSearch {
    public static Set<String> names=new HashSet<>();
    static int count=0;
    public static void main(String[] args) {
        
         String searchQ="colombo colts cricket club administration";      
        // String searchQ="IT Professors in sri lanka";
        //args=new String[]{"IT ","Professionals","Sri","Lanka"};
        String[] searchWords=args=searchQ.split(" ");
       // args=new String[]{"Sri","Lanka","Telephone","Numbers"};     

       
        try {
            
            URL url=null;
            //maximum google results is 100
            for (int i = 0; i <= 9; i=i+10) {
                // Encode the command-line arguments as a Google search query.
                url = encodeGoogleQuery(searchWords,i);
                
            // Download the content from Google.
            System.out.println("Downloading [" + url + "]...\n");
            final String html = downloadString(url);
               

            // Parse and display the links.
            final List<URL> links = parseGoogleLinks(html);
            for (final URL link : links){
                Spider spider = new Spider();
                System.out.println("  " + link);
                try{
                    // spider.search(convertSpecialCharsToNormal(link.toString()), ".*\\d{10}.*");
                     spider.search(convertSpecialCharsToNormal(link.toString()), "[A-Z]([a-z]*) [A-Z]([a-z]*)");
                  //   spider.search(convertSpecialCharsToNormal(link.toString()), "Malcolm Perera");
                     //spider.search(convertSpecialCharsToNormal(link.toString()), "94\\d{9}.*");
                    // spider.search(convertSpecialCharsToNormal(link.toString()), "94716332197");
                }catch(Exception e){
                    e.printStackTrace();
                }
               
            }
            
            for(String successLinks:Spider.successPages){
                System.out.println(successLinks);
            }
            
                            
            }
            String model="classifiers\\english.conll.4class.distsim.crf.ser.gz";
            String serializedClassifier = model;
        System.out.println(serializedClassifier);
        CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
            
            
            for(String successWord:SpiderLeg.matchedPatterns){
                //System.out.println(successWord);
                identifyNER(successWord,classifier);
            }
            for(String name:names){
                System.out.println(name);
            }
            

            
        } catch (final IOException e) {
            // Display an error if anything fails.
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Reads all contents from an input stream and returns a string from the
     * data.
     * 
     * @param stream
     *            The input stream to read.
     * 
     * @return A string built from the contents of the input stream.
     * 
     * @throws IOException
     *             Thrown if there is an error reading the stream.
     */
    private static String downloadString(final InputStream stream)
            throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while (-1 != (ch = stream.read()))
            out.write(ch);
        return out.toString();
    }

    /**
     * Downloads the contents of a URL as a String. This method alters the
     * User-Agent of the HTTP request header so that Google does not return
     * Error 403 Forbidden.
     * 
     * @param url
     *            The URL to download.
     * 
     * @return The content downloaded from the URL as a string.
     * 
     * @throws IOException
     *             Thrown if there is an error downloading the content.
     */
    private static String downloadString(final URL url) throws IOException {
        final String agent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US)";
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        return downloadString(stream);
    }

    /**
     * Encodes a string of arguments as a URL for a Google search query.
     * 
     * @param args
     *            The array of arguments to pass to Google's search engine.
     * 
     * @return A URL for a Google search query based on the arguments.
     */
    private static URL encodeGoogleQuery(final String[] args,int pageStart) {
        
        try {
            final StringBuilder localAddress = new StringBuilder();
            localAddress.append("/search?q=");

            for (int i = 0; i < args.length; i++) {
                final String encoding = URLEncoder.encode(args[i], "UTF-8");
                localAddress.append(encoding);
                if (i + 1 < args.length)
                    localAddress.append("+");
            }
            localAddress.append("&start="+pageStart);
        pageStart+=10;
            return new URL("http", "www.google.com", localAddress.toString());
            

        } catch (final IOException e) {
            // Errors should not occur under normal circumstances.
            throw new RuntimeException(
                    "An error occurred while encoding the query arguments.");
        }
    }

    /**
     * Parses HTML output from a Google search and returns a list of
     * corresponding links for the query. The parsing algorithm is crude and may
     * not work if Google changes the output of their results. This method works
     * adequately as of February 28, 2011.
     * 
     * @param html
     *            The HTML output from Google search results.
     * 
     * @return A list of links for the query.
     * 
     * @throws IOException
     *             Thrown if there is an error parsing the results from Google
     *             or if one of the links returned by Google is not a valid URL.
     */
    private static List<URL> parseGoogleLinks(final String html)
            throws IOException {

        // These tokens are adequate for parsing the HTML from Google. First,
        // find a heading-3 element with an "r" class. Then find the next anchor
        // with the desired link. The last token indicates the end of the URL
        // for the link.
        final String token1 = "<h3 class=\"r\">";
        //final String token2 = "<a href=\"";
        final String token2 = "<a href=\"/url?q=";
        final String token3 = "\"";

        final List<URL> links = new ArrayList<URL>();

        try {
            // Loop until all links are found and parsed. Find each link by
            // finding the beginning and ending index of the tokens defined
            // above.
            int index = 0;
            while (-1 != (index = html.indexOf(token1, index))) {
                final int result = html.indexOf(token2, index); // indexNo of token2 after the the value of the index
                final int urlStart = result + token2.length();
                final int urlEnd = html.indexOf(token3, urlStart);
                
                String urlText = html.substring(urlStart, urlEnd);
                urlText=urlText.substring(0,urlText.indexOf("&amp;sa=U"));
                       urlText = urlText.replaceAll("&amp;","&");
             urlText = urlText.replaceAll("&lt;","<");
             urlText = urlText.replaceAll("&gt;",">");
             urlText = urlText.replaceAll("&quot;","\"");
                final URL url = new URL(urlText);
                links.add(url);
                System.out.println(urlText);
                index = urlEnd + token3.length();
            }
            
//             final String urlText = html.substring(urlStart, urlEnd).substring(6);
//                URL url;
//                try{
//                    if(urlText.startsWith("=http")){
//                        url = new URL(urlText.substring(1));
//                    }
//                    else{
//                        continue;
//                    }
//                }catch(MalformedURLException e){
//                    e.printStackTrace();
//                    continue;
//                }
//                

            return links;

//        } catch (final MalformedURLException e) {
//            throw new IOException("Failed to parse Google links.");
        } catch (final IndexOutOfBoundsException e) {
            throw new IOException("Failed to parse Google links.");
        }
    }
    
    private static String convertSpecialCharsToNormal(String link){        
        link=link.replace("%21", "!");
        link=link.replace("%23", "#");
        link=link.replace("%24", "$");
        link=link.replace("%26", "&");
        link=link.replace("%27", "'");
        link=link.replace("%28", "(");
        link=link.replace("%29", ")");
        link=link.replace("%2A", "*");
        link=link.replace("%2B", "+");
        link=link.replace("%2C", ",");
        link=link.replace("%2F", "/");
        link=link.replace("%3A", ":");
        link=link.replace("%3B", ";");
        link=link.replace("%3D", "=");
        link=link.replace("%3F", "?");
        link=link.replace("%40", "@");
        link=link.replace("%5B", "[");
        link=link.replace("%5D", "]");
        
        
        return link;
    }
    
    private static void findFBProfiles(String name){
        
//        name=name.replace(" ", "%20");
//        String url="https://www.facebook.com/public?query="+name+"&init=dir&nomc=0";
//        try {
//            Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (compatible; Googlebot/2.1");
//            Document htmlDocument = connection.get();
//            String bodyText = htmlDocument.body().text();
//            boolean isNotAPerson=bodyText.contains("We couldn't find anything");
//            
//            if(!isNotAPerson){
////                
//                names.add(name);
//            }
//            else{
//                System.out.println(name+" is not a person");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally{
//            count++;
//            System.out.println(count);
//        }
        
    }
    
    public static void identifyNER(String text,CRFClassifier<CoreLabel> classifier) {
        
        
        LinkedHashMap<String, LinkedHashSet<String>> map = new <String, LinkedHashSet<String>>LinkedHashMap();
        
        List<List<CoreLabel>> classify = classifier.classify(text);
        for (List<CoreLabel> coreLabels : classify) {
            for (CoreLabel coreLabel : coreLabels) {

                String word = coreLabel.word();
                String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
                if (!"O".equals(category)) {
                    if (map.containsKey(category)) {
                        // key is already their just insert in arraylist
                        map.get(category).add(word);
                    } else {
                        LinkedHashSet<String> temp = new LinkedHashSet<String>();
                        temp.add(word);
                        map.put(category, temp);
                    }
                    if(category.equalsIgnoreCase("PERSON")){
                        names.add(text);
                    }
                    System.out.println(word + ":" + category);
                }

            }

        }
    }
}
