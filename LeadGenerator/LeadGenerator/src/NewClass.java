
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author NDB
 */
public class NewClass {
     public static void main(String[] args) throws Exception {

    String model="models\\left3words-wsj-0-18.tagger";
    MaxentTagger tagger = new MaxentTagger(model);
//    @SuppressWarnings("unchecked")
//    List<List<HasWord>> sentences = tagger.tokenizeText(new BufferedReader(new FileReader("sample-input.txt")));
//    for (List<HasWord> sentence : sentences) {
//      ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
//      System.out.println(Sentence.listToString(tSentence, false));
//    }

String sample = "This is a sample text";
 
        // The tagged string
        String tagged = tagger.tagString(sample);
 
        // Output the result
        System.out.println(tagged);
  }
}
