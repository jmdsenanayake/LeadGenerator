/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author NDBDEV
 */
public class Actions {    
    public static void generateLeads(){
         StringBuilder sb=new StringBuilder();
        final Calendar start=Calendar.getInstance();
        sb.append("Process Started @ ").append(start.getTime()).append("\n");
        sb.append("##############################################").append("\n");
        
        try {
            GoogleSearch googleSearch = new GoogleSearch();
            List<URL> links = googleSearch.getGoogleSearchLinks(Configurations.searchKeyPhrase, Configurations.googleURLSearchResultEnd);
            WebCrawl webCrawl = new WebCrawl();
            Set<String> matchedPatterns=webCrawl.crawlWeb(links, Configurations.REGEX_NAME,Configurations.linkDepthForNames,true);
//            
////            for(String matchedPattern:matchedPatterns){
////                System.out.println(matchedPattern);
////            }
            
            NameRecongnizer nameRecongnizer=new NameRecongnizer();
             Set<String> names=nameRecongnizer.recongnizeNames(matchedPatterns);
//Set<String> names=new HashSet<>();
//names.add("Dasun Chathuranga");
////names.add("Rajeev Munasinghe");
           
            List<URL> calllinks=new LinkedList<>();
            for(String name:names){
                String searchKey = name+" Contact Phone Call";
                calllinks=googleSearch.getGoogleSearchLinks(searchKey,Configurations.googleURLPhoneResultEnd);
                Set<String> phoneNumbers=webCrawl.crawlWeb(calllinks, Configurations.REGEX_PHONE,Configurations.linkDepthForPhone,false);
                sb.append(name).append(" matching Contact No(s):").append("\n");
                sb.append("--------------------------------------------------------------------------------------------------------------------------------------------\n");
                for(String phoneNumber:phoneNumbers){
                   sb.append("\t").append(phoneNumber).append("\n");
                }
                sb.append("\n");              
            }            
            List<URL> emaillinks = new LinkedList<>();
            for (String name : names) {
                String searchKey = name+" Contact Email";
                emaillinks = googleSearch.getGoogleSearchLinks(searchKey, Configurations.googleURLEmailResultEnd);
                Set<String> emailAddresses = webCrawl.crawlWeb(emaillinks, Configurations.REGEX_EMAIL, Configurations.linkDepthForPhone, false);

                String firstName = name.split(" ")[0].toLowerCase();
                String secondName = name.split(" ")[1].toLowerCase();
                sb.append(name).append(" matching email addresses:").append("\n");
                sb.append("--------------------------------------------------------------------------------------------------------------------------------------------\n");
                for (String emailAddress : emailAddresses) {

                    if (emailAddress.contains(firstName.toLowerCase()) || emailAddress.contains(secondName.toLowerCase()) || emailAddress.contains(firstName) || emailAddress.contains(secondName)) {
                        sb.append("\t").append(emailAddress).append("\n");
                    }
                }
                sb.append("\n");

            }            
                Calendar end=Calendar.getInstance();
                sb.append("##############################################").append("\n");
                sb.append("Process End @ ").append(end.getTime()).append("\n");
                long duration=end.getTimeInMillis()-start.getTimeInMillis();
                String durationString=String.format("%02dhh:%02dmm:%02dss", 
                        TimeUnit.MILLISECONDS.toHours(duration),
                        TimeUnit.MILLISECONDS.toMinutes(duration) - 
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)), 
                        TimeUnit.MILLISECONDS.toSeconds(duration) - 
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));   
                sb.append("Duration : ").append(durationString).append("\n");
                sb.append("##############################################").append("\n");
                new Results(null, true,sb).setVisible(true);
                System.out.println(sb.toString());

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }
    
}
