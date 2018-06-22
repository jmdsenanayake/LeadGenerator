/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NDBDEV
 */
public class ActionsThread {

    public static boolean generateLeads() {
        boolean isLeadsGenerated = false;
        try {
            final GoogleSearch googleSearch = new GoogleSearch();
            List<URL> links = googleSearch.getGoogleSearchLinks(Configurations.searchKeyPhrase, Configurations.googleURLSearchResultEnd);
            final WebCrawl webCrawl = new WebCrawl();
            Set<String> matchedPatterns = webCrawl.crawlWeb(links, Configurations.REGEX_NAME, Configurations.linkDepthForNames, true);

//            for(String matchedPattern:matchedPatterns){
//                System.out.println(matchedPattern);
//            }
            NameRecongnizer nameRecongnizer = new NameRecongnizer();
            final Set<String> names = nameRecongnizer.recongnizeNames(matchedPatterns);
//Set<String> names=new HashSet<>();
//names.add("Indika Lakmal");

            final List<URL> calllinks = new LinkedList<>();
            final Map<String, String> namePhone = new HashMap<>();

            final List<URL> emaillinks = new LinkedList<>();
            final Map<String, String> nameEmail = new HashMap<>();

            Thread threadNamePhone = new Thread() {
                @Override
                public void run() {
                    for (String name : names) {
                        String searchKey = name + " Contact Phone Call";
                        try {
                            calllinks.addAll(googleSearch.getGoogleSearchLinks(searchKey, Configurations.googleURLPhoneResultEnd));
                        } catch (IOException ex) {
                            System.err.println(ex.getMessage());
                        }
                        Set<String> phoneNumbers = webCrawl.crawlWeb(calllinks, Configurations.REGEX_PHONE, Configurations.linkDepthForPhone, false);
                        String multiplePhoneNumbers = "";
                        for (String phoneNumber : phoneNumbers) {
                            multiplePhoneNumbers += phoneNumber + ",";
                        }
                        if (multiplePhoneNumbers.endsWith(",")) {
                            multiplePhoneNumbers = multiplePhoneNumbers.substring(0, multiplePhoneNumbers.length() - 1);
                        }
                        namePhone.put(name, multiplePhoneNumbers);
                    }
                }
            };
            
            threadNamePhone.start();

            Thread threadNameEmail = new Thread() {
                @Override
                public void run() {
                    for (String name : names) {
                        String searchKey = name + " Contact Email";
                        try {
                            emaillinks.addAll(googleSearch.getGoogleSearchLinks(searchKey, Configurations.googleURLEmailResultEnd));
                        } catch (IOException ex) {
                            System.err.println(ex.getMessage());
                        }
                        Set<String> emailAddresses = webCrawl.crawlWeb(emaillinks, Configurations.REGEX_EMAIL, Configurations.linkDepthForPhone, false);
                        String multipleEmails = "";
                        String firstName = name.split(" ")[0].toLowerCase();
                        String secondName = name.split(" ")[1].toLowerCase();
                        for (String emailAddress : emailAddresses) {

                            if (emailAddress.contains(firstName.toLowerCase()) || emailAddress.contains(secondName.toLowerCase()) || emailAddress.contains(firstName) || emailAddress.contains(secondName)) {
                                multipleEmails += emailAddress + ",";
                            }

                        }
                        if (multipleEmails.endsWith(",")) {
                            multipleEmails = multipleEmails.substring(0, multipleEmails.length() - 1);
                        }
                        nameEmail.put(name, multipleEmails);
                    }
                }
            };
            
            threadNameEmail.start();

//            System.out.println("#####################################################################");
//            System.out.println("matchedPatterns: "+matchedPatterns);
//            System.out.println("namePhone: "+namePhone);
//            System.out.println("nameEmail: "+nameEmail);
            OutputDisplayer.setTextinTextArea("#####################################################################");
//            OutputDisplayer.setTextinTextArea(matchedPatterns.toString());
            OutputDisplayer.setTextinTextArea(namePhone.toString());
            OutputDisplayer.setTextinTextArea(nameEmail.toString());
            isLeadsGenerated = true;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            isLeadsGenerated = false;
        }
        return isLeadsGenerated;
    }

}
