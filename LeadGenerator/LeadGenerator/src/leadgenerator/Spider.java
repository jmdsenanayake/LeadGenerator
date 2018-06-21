/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Janaka_5977
 */
public class Spider {
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    

    /**
     * Our main launching point for the Spider's functionality. Internally it
     * creates spider legs that make an HTTP request and parse the response (the
     * web page).
     *
     * @param url - The starting point of the spider
     * @param searchWord - The word or string that you are searching for
     * @param maxPagesToSearch
     * @param isBreakWhenSuccess
     * @return 
     */
    public  Set<String> search(String url, String searchWord,int maxPagesToSearch,boolean isBreakWhenSuccess) {
     Set<String> matchedPatterns=new HashSet<>();
     
        url=convertSpecialCharsToNormal(url);
        while (this.pagesVisited.size() < maxPagesToSearch) {
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            } else {
                currentUrl = this.nextUrl();
            }
            boolean isHTML = leg.crawl(currentUrl); 
            
            if (isHTML) {
               Set<String> matchedPatternsPart = leg.searchForWord(searchWord);
                if (matchedPatternsPart.size()>0) {
                    OutputDisplayer.setTextinTextArea("**Success** Word "+searchWord+" found at "+currentUrl);
                    //System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                   // successPages.add(currentUrl);
                   matchedPatterns.addAll(matchedPatternsPart);
                   if(isBreakWhenSuccess){
                       break;
                   }
                   else{
                       continue;
                   }
                    
                }
                
                this.pagesToVisit.addAll(leg.getLinks());
            }
            else{
                this.pagesVisited.add(currentUrl);
                this.pagesToVisit.remove(currentUrl);
                break;
            }

        }
        OutputDisplayer.setTextinTextArea("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
        //System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
        return matchedPatterns;
    }

    /**
     * Returns the next URL to visit (in the order that they were found). We
     * also do a check to make sure this method doesn't return a URL that has
     * already been visited.
     *
     * @return
     */
    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
    
    private static String convertSpecialCharsToNormal(String link) {
        link = link.replace("%21", "!");
        link = link.replace("%23", "#");
        link = link.replace("%24", "$");
        link = link.replace("%26", "&");
        link = link.replace("%27", "'");
        link = link.replace("%28", "(");
        link = link.replace("%29", ")");
        link = link.replace("%2A", "*");
        link = link.replace("%2B", "+");
        link = link.replace("%2C", ",");
        link = link.replace("%2F", "/");
        link = link.replace("%3A", ":");
        link = link.replace("%3B", ";");
        link = link.replace("%3D", "=");
        link = link.replace("%3F", "?");
        link = link.replace("%40", "@");
        link = link.replace("%5B", "[");
        link = link.replace("%5D", "]");

        return link;
    }
    
}
