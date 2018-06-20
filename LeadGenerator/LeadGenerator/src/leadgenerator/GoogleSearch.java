/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Janaka_5977
 */
public class GoogleSearch {

    public List<URL> getGoogleSearchLinks(String searchKeyPhrase,int startPage,int endPage) throws IOException {
        List<URL> links=new LinkedList<>();
        URL url = null;

        //maximum google results is 100
        for (int i = startPage; i < endPage; i +=10) {
            // Encode the command-line arguments as a Google search query.
            url = encodeGoogleQuery(searchKeyPhrase, i);

            // Download the content from Google.
            System.out.println("Downloading [" + url + "]...\n");
            String html = downloadString(url);

            // Parse and display the links.
            links.addAll(parseGoogleLinks(html));
        }
        return links;
    }

    /**
     * Encodes a string of arguments as a URL for a Google search query.
     *
     * @param searchKeyPhrase : The phase to be passed to Google search engine.
     * @param pageStart : Page number to start the google search results
     *
     * @return A URL for a Google search query based on the arguments.
     *
     * @throws IOException if any error occur during encoding
     */
    private static URL encodeGoogleQuery(String searchKeyPhrase, int pageStart) throws IOException {
        final StringBuilder localAddress = new StringBuilder();
        localAddress.append("/search?q=");
        String[] searchKeyWords = searchKeyPhrase.split(" ");
        for (int i = 0; i < searchKeyWords.length; i++) {
            final String encoding = URLEncoder.encode(searchKeyWords[i], "UTF-8");
            localAddress.append(encoding);
            if (i + 1 < searchKeyWords.length) {
                localAddress.append("+");
            }
        }
        localAddress.append("&start=" + pageStart);
        pageStart += 10;
        return new URL("http", "www.google.lk", localAddress.toString());
    }

    /**
     * Downloads the contents of a URL as a String. This method alters the
     * User-Agent of the HTTP request header. Therefore Google does not return
     * Error 403 Forbidden.
     *
     * @param url The URL to download.
     *
     * @return The content downloaded from the URL as a string.
     *
     * @throws IOException if there is an error downloading the content.
     */
    private static String downloadString(final URL url) throws IOException {
        final String agent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US)";
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        
        
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while (-1 != (ch = stream.read())) {
            out.write(ch);
        }
       return out.toString();      
    }

    /**
     * Parses HTML output from a Google search and returns a list of
     * corresponding links for the query.
     *
     * @param html : The HTML output from Google search results.
     *
     * @return A list of links for the query.
     *
     * @throws IOException if there is an error parsing the results from Google
     * or if one of the links returned by Google is not a valid URL.
     */
    private static List<URL> parseGoogleLinks(final String html) throws IOException {

        // Tokens are adequate for parsing the HTML from Google.        
        //First find a heading-3 element with an "r" class. 
        final String token1 = "<h3 class=\"r\">";

        //Then find the next anchor with the desired link. 
        final String token2 = "<a href=\"/url?q=";

        //The last token indicates the end of the URL for the link.
        final String token3 = "\"";

        final List<URL> links = new ArrayList<URL>();

        try {
            // Loop until all links are found and parsed. 
            //Find each link by finding the beginning and ending index of the tokens defined above.
            int index = 0;
            while (-1 != (index = html.indexOf(token1, index))) {
                final int result = html.indexOf(token2, index); // indexNo of token2 after the the value of the index
                final int urlStart = result + token2.length();
                final int urlEnd = html.indexOf(token3, urlStart);

                String urlText = html.substring(urlStart, urlEnd);
                urlText = urlText.substring(0, urlText.indexOf("&amp;sa=U"));
                urlText = urlText.replaceAll("&amp;", "&");
                urlText = urlText.replaceAll("&lt;", "<");
                urlText = urlText.replaceAll("&gt;", ">");
                urlText = urlText.replaceAll("&quot;", "\"");
                final URL url = new URL(urlText);
                links.add(url);
                System.out.println(urlText);
                index = urlEnd + token3.length();
            }

            

        } catch (IndexOutOfBoundsException e) {
            //throw new IOException("Failed to parse Google links.");
        }
        return links;
    }

    

}
