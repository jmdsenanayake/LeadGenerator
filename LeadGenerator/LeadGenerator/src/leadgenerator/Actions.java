/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import DBOperations.DBActions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
    static int criterialId = 0;
    public static void generateLeads() {
        final Calendar start = Calendar.getInstance();
         OutputDisplayer.setTextInloadingOutputTextArea("Process Started @ "+start.getTime()+"\n");
        OutputDisplayer.setTextInloadingOutputTextArea("##############################################\n");
        DBActions dBActions = new DBActions();
        
        try {
            GoogleSearch googleSearch = new GoogleSearch();
            WebCrawl webCrawl = new WebCrawl();
            List<URL> links = googleSearch.getGoogleSearchLinks(Configurations.searchKeyPhrase, Configurations.googleURLSearchResultEnd);
            criterialId = dBActions.insertCriteriaDetails(Configurations.searchKeyPhrase);
            OutputDisplayer.setTextInloadingOutputTextArea("##############################################");
            OutputDisplayer.setTextInloadingOutputTextArea("Generating Leads for :"+Configurations.searchKeyPhrase);
            Set<String> matchedPatterns = webCrawl.crawlWeb(links, Configurations.REGEX_NAME, Configurations.linkDepthForNames, true);

            NameRecongnizer nameRecongnizer = new NameRecongnizer();
            Set<String> names = nameRecongnizer.recongnizeNames(matchedPatterns);

////////Set<String> names=new HashSet<>();
////////names.add("Janaka Senanayake");
////////names.add("Rajeev Munasinghe");

            List<URL> calllinks = new LinkedList<>();
            List<URL> emaillinks = new LinkedList<>();
            for (String name : names) {
                
                OutputDisplayer.setTextInloadingOutputTextArea("##############################################\n");
                
                OutputDisplayer.setTextInloadingOutputTextArea("Name : "+name);
                
                OutputDisplayer.setTextInloadingOutputTextArea("____________________________\n");
                
                OutputDisplayer.setTextInloadingOutputTextArea("********************************************\n");
                 
                 OutputDisplayer.setTextInloadingOutputTextArea("Possible Designations :\n");
                
                Set<String> designations = new HashSet<>();
                Set<String> phoneNumbers = new HashSet<>();
                Set<String> emailAddresses = new HashSet<>();

                int leadID=dBActions.insertLeadDetails(name, criterialId);
                String searchKey = name + " LinkedIn";
                //xxxxxx<b>name</b> - title | LinkedInxxxxxxxx<b>name</b> - title | LinkedIn xxxxxxxx<b>name</b> - title | LinkedInxx
                String result = getSearchResultsString(searchKey);

                if (result.contains(";ved=")) {
                    String[] resultsSplitting = result.split(";ved=");
                    for (int i = 1; i < resultsSplitting.length; i += 1) {
                        String firstName = name.split(" ")[0];
                        String firstNameLower = firstName.toLowerCase();
                        String secondName = name.split(" ")[1];
                        String secondNameLower = secondName.toLowerCase();
                        String phraseForFindingName = resultsSplitting[i].replaceAll(firstName, firstNameLower).replaceAll(secondName, secondNameLower);

                        if (phraseForFindingName.contains("><b>" + name.toLowerCase() + "</b>")) {
                            String subSplitingResults = phraseForFindingName.split("><b>" + name.toLowerCase() + "</b>")[1];
                            String subSubSplittingResults = subSplitingResults.split("<b>LinkedIn</b>")[0];
                            if ((!subSubSplittingResults.trim().startsWith("|")) && (subSubSplittingResults.endsWith(" | ") || (subSubSplittingResults.endsWith(" ... - ")))) {
                                String designation = subSubSplittingResults.replaceFirst(" - ", "");
                                designations.add(designation);
                            }
                        }
                    }
                }
                
                for(String designation : designations){
                    OutputDisplayer.setTextInloadingOutputTextArea("\t"+designation+"\n");
                    
                    dBActions.insertDesignationDetails(designation, leadID);
                }
                OutputDisplayer.setTextInloadingOutputTextArea("********************************************\n");

                OutputDisplayer.setTextInloadingOutputTextArea("Possible Phone Numbers :\n");

                String searchKeyForPhone = name + " Contact Phone Call";
                calllinks = googleSearch.getGoogleSearchLinks(searchKeyForPhone, Configurations.googleURLPhoneResultEnd);
                phoneNumbers.addAll(webCrawl.crawlWeb(calllinks, Configurations.REGEX_PHONE, Configurations.linkDepthForPhone, false));
               
                for (String phoneNumber : phoneNumbers) {
                   OutputDisplayer.setTextInloadingOutputTextArea("\t"+phoneNumber+"\n");
                    
                   dBActions.insertContactNoDetails(phoneNumber, leadID);
                }
                
                OutputDisplayer.setTextInloadingOutputTextArea("********************************************\n");
                
                OutputDisplayer.setTextInloadingOutputTextArea("Possible Email Address :\n");

                String searchKeyForEmail = name + " Contact Email";
                emaillinks = googleSearch.getGoogleSearchLinks(searchKeyForEmail, Configurations.googleURLEmailResultEnd);
                emailAddresses = webCrawl.crawlWeb(emaillinks, Configurations.REGEX_EMAIL, Configurations.linkDepthForEmails, false);            
            
                String firstName = name.split(" ")[0].toLowerCase();
                String secondName = name.split(" ")[1].toLowerCase();
                for (String emailAddress : emailAddresses) {

                    if (emailAddress.contains(firstName.toLowerCase()) || emailAddress.contains(secondName.toLowerCase()) || emailAddress.contains(firstName) || emailAddress.contains(secondName)) {
                        dBActions.insertEmailAddressDetails(emailAddress, leadID);
                        
                        OutputDisplayer.setTextInloadingOutputTextArea("\t"+emailAddress+"\n");
                    }
                }
            }

            Calendar end = Calendar.getInstance();
            OutputDisplayer.setTextInloadingOutputTextArea("##############################################\n");
            
            OutputDisplayer.setTextInloadingOutputTextArea("Process End @ "+end.getTime()+"\n");
            
            long duration = end.getTimeInMillis() - start.getTimeInMillis();
            String durationString = String.format("%02dhh:%02dmm:%02dss",
                    TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration)
                    - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                    TimeUnit.MILLISECONDS.toSeconds(duration)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
            OutputDisplayer.setTextInloadingOutputTextArea("Duration : "+durationString+"\n");
            OutputDisplayer.setTextInloadingOutputTextArea("##############################################\n");

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static String getSearchResultsString(String searchKeyPhrase) throws IOException {
        final StringBuilder localAddress = new StringBuilder();
        localAddress.append("/search?q=");
        String[] searchKeyWords = searchKeyPhrase.split(" ");
        for (int i = 0; i < searchKeyWords.length; i++) {
            final String encoding = URLEncoder.encode(searchKeyWords[i], "iso-8859-1");
            localAddress.append(encoding);
            if (i + 1 < searchKeyWords.length) {
                localAddress.append("+");
            }
        }
        URL url = new URL("http", "www.google.lk", localAddress.toString());

        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", Configurations.USER_AGENT);
        final InputStream stream = connection.getInputStream();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while (-1 != (ch = stream.read())) {
            out.write(ch);
        }
        return out.toString();
    }
    
}
