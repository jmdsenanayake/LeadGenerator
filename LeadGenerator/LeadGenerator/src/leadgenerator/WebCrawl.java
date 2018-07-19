/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Janaka_5977
 */
public class WebCrawl {  

    public Set<String> crawlWeb(List<URL> links,String searchPattern,int maxPageSize,boolean isBreakWhenSuccess) {
        Set<String> matchedPatterns = new HashSet<>();
        for (URL link : links) {
            OutputDisplayer2.setTextInloadingProgressTextArea(link.toString());
           // System.out.println(link);
            Spider spider = new Spider();
            matchedPatterns.addAll(spider.search(link.toString(), searchPattern,maxPageSize,isBreakWhenSuccess));         
        }
        return matchedPatterns;
    }
}
