/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlesearch;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author NDBDEV
 */
public class Spider {

    private final int MAX_PAGES_TO_SEARCH = 2;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    public static Set<String> successPages=new HashSet<String>();

    /**
     * Our main launching point for the Spider's functionality. Internally it
     * creates spider legs that make an HTTP request and parse the response (the
     * web page).
     *
     * @param url - The starting point of the spider
     * @param searchWord - The word or string that you are searching for
     */
    public void search(String url, String searchWord) {
        while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
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
                boolean success = leg.searchForWord(searchWord);
                if (success) {
                    System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                    successPages.add(currentUrl);
                    break;
                }
                this.pagesToVisit.addAll(leg.getLinks());
            }
            else{
                this.pagesVisited.add(currentUrl);
                this.pagesToVisit.remove(currentUrl);
                break;
            }

        }
        System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
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
}
