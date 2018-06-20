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
import java.util.Locale;
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
//            Set<String> matchedPatterns=webCrawl.crawlWeb(links, searchPatternForTwoWordswithCapitalFirstLetter,2,true);
//            
//            for(String matchedPattern:matchedPatterns){
//                System.out.println(matchedPattern);
//            }
//            
//            NameRecongnizer nameRecongnizer=new NameRecongnizer();
//            Set<String> names=nameRecongnizer.recongnizeNames(matchedPatterns);
            Set<String> names=new HashSet<>();
            names.add("Indika Lakmal");
            
//            List<URL> calllinks=new LinkedList<>();
//            Map<String,String> namePhone=new HashMap<>();
//            for(String name:names){
//                searchKeyPhrase = "Call "+name;
//                calllinks=googleSearch.getGoogleSearchLinks(searchKeyPhrase,0,10);
//                String searchPatternForTP = "(0|94)\\d{9}";    
//                Set<String> phoneNumbers=webCrawl.crawlWeb(calllinks, searchPatternForTP,1,true);
//                String multiplePhoneNumbers="";
//                for(String phoneNumber:phoneNumbers){
//                    multiplePhoneNumbers+=phoneNumber+",";
//                }
//                if(multiplePhoneNumbers.endsWith(",")){
//                    multiplePhoneNumbers=multiplePhoneNumbers.substring(0,multiplePhoneNumbers.length()-1);
//                }
//                namePhone.put(name, multiplePhoneNumbers);                
//            }
//            System.out.println(namePhone);
            
            List<URL> emaillinks=new LinkedList<>();
            Map<String,String> nameEmail=new HashMap<>();
            for(String name:names){
                searchKeyPhrase = "Email "+name;
                emaillinks=googleSearch.getGoogleSearchLinks(searchKeyPhrase,0,30);
                String searchPatternForTP = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";    
                Set<String> emailAddresses=webCrawl.crawlWeb(emaillinks, searchPatternForTP,1,false);
                String multipleEmails="";
                String firstName=name.split(" ")[0].toLowerCase();
                String secondName=name.split(" ")[1].toLowerCase();
                for(String emailAddress:emailAddresses){
                    //System.out.println(emailAddress);
                    
                    if(emailAddress.contains(firstName.toLowerCase())||emailAddress.contains(secondName.toLowerCase())){
                        multipleEmails+=emailAddress+",";
                        //System.out.println(emailAddress);
                    }      
                   
                }
                if(multipleEmails.endsWith(",")){
                    multipleEmails=multipleEmails.substring(0,multipleEmails.length()-1);
                }
                nameEmail.put(name, multipleEmails);                
            }
            System.out.println(nameEmail);
            
            
        
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
