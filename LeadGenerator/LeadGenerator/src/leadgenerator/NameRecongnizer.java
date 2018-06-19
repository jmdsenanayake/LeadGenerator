/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
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
        String model="classifiers\\english.conll.4class.distsim.crf.ser.gz";
        String serializedClassifier = model;
        CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        
        for(String successWord:matchedPatterns){
            identifyNER(successWord,classifier);
        }
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
                    System.out.println(word + ":" + category);
                }
            }
        }
    }
    
}
