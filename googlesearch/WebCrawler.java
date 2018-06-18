/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlesearch;

/**
 *
 * @author NDBDEV
 */
public class WebCrawler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         Spider spider = new Spider();
         String searchKey="gem exporters";
       spider.search("https://www.google.com/search?q="+searchKey, searchKey);
        //spider.search("http://www.ndbbank.com/", searchKey);
    }
    
}
