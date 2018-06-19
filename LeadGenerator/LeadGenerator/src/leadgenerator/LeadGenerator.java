/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
//            List<URL> links=googleSearch.getGoogleSearchLinks(searchKeyPhrase,0,10);
//            
//            String searchPatternForTwoWordswithCapitalFirstLetter = "[A-Z]([a-z]*) [A-Z]([a-z]*)";
//            //String searchPattern = ".*\\d{10}.*";
//            //String searchPattern = "94\\d{9}.*";
//            //String searchPattern = "94716332197";            
            WebCrawl webCrawl=new WebCrawl();    
//            Set<String> matchedPatterns=webCrawl.crawlWeb(links, searchPatternForTwoWordswithCapitalFirstLetter,2);
//            
//            for(String matchedPattern:matchedPatterns){
//                System.out.println(matchedPattern);
//            }
//            
//            NameRecongnizer nameRecongnizer=new NameRecongnizer();
//            Set<String> names=nameRecongnizer.recongnizeNames(matchedPatterns);
            Set<String> names=new HashSet<>();
            names.add("Rajeev Munasinghe");
            
            List<URL> calllinks=new LinkedList<>();
            Map<String,String> namePhone=new HashMap<>();
            for(String name:names){
                searchKeyPhrase = "Call "+name;
                calllinks=googleSearch.getGoogleSearchLinks(searchKeyPhrase,1,10);
                String searchPatternForTP = "(0|94)\\d{9}";    
                Set<String> phoneNumbers=webCrawl.crawlWeb(calllinks, searchPatternForTP,1);
                String multiplePhoneNumbers="";
                for(String phoneNumber:phoneNumbers){
                    multiplePhoneNumbers+=phoneNumber+",";
                }
                if(multiplePhoneNumbers.endsWith(",")){
                    multiplePhoneNumbers=multiplePhoneNumbers.substring(0,multiplePhoneNumbers.length()-1);
                }
                namePhone.put(name, multiplePhoneNumbers);                
            }
            System.out.println(namePhone);
            
            
        
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
