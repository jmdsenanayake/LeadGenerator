/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NDBDEV
 */
public class Actions1 {

    public static void generateLeads() {
        StringBuilder sb = new StringBuilder();
        Calendar start = Calendar.getInstance();
        sb.append("Process Started @ ").append(start.getTime()).append("\n");
        sb.append("##############################################").append("\n");

        try {
            final GoogleSearch googleSearch = new GoogleSearch();
            List<URL> links = googleSearch.getGoogleSearchLinks(Configurations.searchKeyPhrase, Configurations.googleURLSearchResultEnd);
            final WebCrawl webCrawl = new WebCrawl();
            Set<String> matchedPatterns = webCrawl.crawlWeb(links, Configurations.REGEX_NAME, Configurations.linkDepthForNames, true);
//            
////            for(String matchedPattern:matchedPatterns){
////                System.out.println(matchedPattern);
////            }

            NameRecongnizer nameRecongnizer = new NameRecongnizer();
            Set<String> names = nameRecongnizer.recongnizeNames(matchedPatterns);

            final Map<String, Set<String>> namePhones = new HashMap<>();
            final Map<String, Set<String>> nameEmails = new HashMap<>();

            

            for (final String name : names) {
                Thread phoneNoThread = new Thread() {
                    public void run() {
                        try {
                            String searchKeyPhone = name + " Contact Phone Call";
                            List<URL> calllinks = new LinkedList<>();
                            calllinks = googleSearch.getGoogleSearchLinks(searchKeyPhone, Configurations.googleURLPhoneResultEnd);
                            Set<String> phoneNumbers = webCrawl.crawlWeb(calllinks, Configurations.REGEX_PHONE, Configurations.linkDepthForPhone, false);
                            namePhones.put(name, phoneNumbers);
                        } catch (IOException ex) {
                            System.err.println(ex.getMessage());
                        }
                    }
                };

                Thread emailAddressThread = new Thread() {
                    public void run() {
                        try {
                            String searchKeyForEmail = name + " Contact Email";
                            List<URL> emaillinks = new LinkedList<>();
                            emaillinks = googleSearch.getGoogleSearchLinks(searchKeyForEmail, Configurations.googleURLEmailResultEnd);
                            Set<String> emailAddresses = webCrawl.crawlWeb(emaillinks, Configurations.REGEX_EMAIL, Configurations.linkDepthForPhone, false);

                            String firstName = name.split(" ")[0].toLowerCase();
                            String secondName = name.split(" ")[1].toLowerCase();
                            for (String emailAddress : emailAddresses) {
                                if (emailAddress.contains(firstName.toLowerCase()) || emailAddress.contains(secondName.toLowerCase()) || emailAddress.contains(firstName) || emailAddress.contains(secondName)) {
                                    //do Nothing if email address is ok                       
                                } else {
                                    emailAddresses.remove(emailAddress);
                                }
                            }
                            nameEmails.put(name, emailAddresses);
                        } catch (IOException ex) {
                            System.err.println(ex.getMessage());
                        }
                    }
                };
                
                phoneNoThread.start();
                emailAddressThread.start();

            }

            Calendar end = Calendar.getInstance();
            sb.append("##############################################").append("\n");
            sb.append("Process End @ ").append(end.getTime()).append("\n");
            long duration = end.getTimeInMillis() - start.getTimeInMillis();
            String durationString = String.format("%02dhh:%02dmm:%02dss",
                    TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration)
                    - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                    TimeUnit.MILLISECONDS.toSeconds(duration)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
            sb.append("Duration : ").append(durationString).append("\n");
            sb.append("##############################################").append("\n");
            new Results(null, true, sb).setVisible(true);
            System.out.println(sb.toString());

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

}
