/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Janaka_5977
 */
public class NameRecongnizer {
    private  Set<String> names=new HashSet<>();
    
    public Set<String> recongnizeNames(Set<String> matchedPatterns){
        //String model="classifiers\\english.muc.7class.distsim.crf.ser.gz";
        String model="classifiers\\english.all.3class.distsim.crf.ser.gz";
//        String model="classifiers\\english.conll.4class.distsim.crf.ser.gz";
        String serializedClassifier = model;
        CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        
        for(String successWord:matchedPatterns){
            identifyNER(successWord,classifier);
        }
        removeNotNames();
        return names;
        
    }
    
    private void identifyNER(String text,CRFClassifier<CoreLabel> classifier) {      
        
        LinkedHashMap<String, LinkedHashSet<String>> map = new <String, LinkedHashSet<String>>LinkedHashMap();
        
        List<List<CoreLabel>> classify = classifier.classify(text);
        for (List<CoreLabel> coreLabels : classify) {
            for (CoreLabel coreLabel : coreLabels) {

                String word = coreLabel.word();
                String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
                if (!"O".equals(category)) {
                    if (map.containsKey(category)) {
                        // key is already their just insert in arraylist
                        map.get(category).add(word);
                    } else {
                        LinkedHashSet<String> temp = new LinkedHashSet<String>();
                        temp.add(word);
                        map.put(category, temp);
                    }
                    if(category.equalsIgnoreCase("PERSON")){
                        names.add(text);
                    }
                    
                    OutputDisplayer.setTextinTextArea(word+":"+category);
                    //System.out.println(word + ":" + category);
                }
            }
        }
    }
    
    private void removeNotNames(){       
       
        String[] pronouns=new String[]{"I","me","we","us","you","she","her","he","him","it","they","them",
            "that","which","who","whom","whose","whichever","whoever","whomever","this","these","that",
            "those","anybody","anyone","anything","each","either","everybody","everyone","everything",
            "neither","nobody","none","nothing","one","somebody","something","someone","both","few","many",
            "several","all","any","most","some","myself","ourselves","yourself","yourselves","himself",
            "herself","itself","themselves","what","my","your","his","her","its","our","your","their","mine",
            "yours","his","hers","ours","yours","theirs"};
        
        Arrays.sort(pronouns);
        Set<String> notNames=new HashSet<>();
        for(String name:names){
            String[] twoNames=name.split(" ");
            for (String aName:twoNames) {
                System.out.println(aName+" "+Arrays.binarySearch(pronouns,aName));
//                if(Arrays.binarySearch(pronouns,aName)<0){
//                    notNames.add(name);
//                }   
            }                     
        }
//        for(String notName:notNames){
//            System.out.println(notName);
//            names.remove(notName);
//        }
    }
    
}
