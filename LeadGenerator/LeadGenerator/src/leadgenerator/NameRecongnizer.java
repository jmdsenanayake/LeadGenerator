/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leadgenerator;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
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

    static String[] skippedWords;

    static {
        HashSet<String> skippedWordsSet = DBOperations.DBActions.getSkippedWords();
        skippedWords = skippedWordsSet.toArray(new String[skippedWordsSet.size()]);
        Arrays.sort(skippedWords);
        System.out.println(skippedWordsSet.toString());
    }
    private Set<String> names = new HashSet<>();

    public Set<String> recongnizeNames(Set<String> matchedPatterns) {
//        String model="classifiers\\english.muc.7class.distsim.crf.ser.gz";
      //  String model="classifiers\\english.all.3class.distsim.crf.ser.gz";
        //String model = "classifiers\\english.conll.4class.distsim.crf.ser.gz";
        String model = "classifiers\\english.nowiki.3class.distsim.crf.ser.gz";
        String serializedClassifier = model;
        CRFClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

        for (String successWord : matchedPatterns) {
            identifyNER(successWord, classifier);
        }
        return names;

    }

    private void identifyNER(String text, CRFClassifier<CoreLabel> classifier) {

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
                    if (category.equalsIgnoreCase("PERSON")) {
                        String[] twoNames=text.split(" ");
                        int searchResultName1=0;
                      //  int searchResultName2=0;
                        
                        searchResultName1=Arrays.binarySearch(skippedWords, twoNames[0].toLowerCase());
                       // searchResultName2=Arrays.binarySearch(skippedWords, twoNames[1].toLowerCase());
                        
//                        if (searchResultName1 < 0 && searchResultName2<0 ) {
                        if (searchResultName1 < 0) {
                            
                            names.add(text);
                        }
                    }
                }

                OutputDisplayer.setTextInloadingProgressTextArea(word + ":" + category);
            }
        }
    }

}
