/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Janaka_5977
 */
public class LeadGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            GoogleSearch googleSearch=new GoogleSearch();
            String searchKeyPhrase = "colombo colts cricket club administration";
            List<URL> links=googleSearch.getGoogleSearchLinks(searchKeyPhrase);
            
            String searchPatternForTwoWordswithCapitalFirstLetter = "[A-Z]([a-z]*) [A-Z]([a-z]*)";
            //String searchPattern = ".*\\d{10}.*";
            //String searchPattern = "94\\d{9}.*";
            //String searchPattern = "94716332197";            
            WebCrawl webCrawl=new WebCrawl();    
            Set<String> successPages=webCrawl.crawlWeb(links, searchPatternForTwoWordswithCapitalFirstLetter);
            
            NameRecongnizer nameRecongnizer=new NameRecongnizer();
            Set<String> names=nameRecongnizer.recongnizeNames(SpiderLeg.matchedPatterns);
            
            for(String name:names){
                System.out.println(name);
            }
        
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
