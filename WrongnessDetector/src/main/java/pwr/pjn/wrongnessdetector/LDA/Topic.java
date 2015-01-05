package pwr.pjn.wrongnessdetector.LDA;

import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.SortedSet;

/**
 *
 * @author KonradOliwer
 */
public class Topic implements Serializable {

    private int id;
    private double topicDistribution;
    private int[] wordsIds;
    private int[] wordsWeight;
    private Alphabet alphabet;

    public Topic(int id, double topicDistribution, SortedSet<IDSorter> words, Alphabet alphabet) {
        this.id = id;
        this.alphabet = alphabet;
        this.topicDistribution = topicDistribution;
        
        wordsIds = new int[words.size()];
        wordsWeight = new int[words.size()];
        Iterator<IDSorter> iterator = words.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            IDSorter current = iterator.next();
            wordsIds[i] = current.getID();
            wordsWeight[i] = (int) current.getWeight();
            i++;
        }
    }

    public String getTopicDescription(int wordsToPrint) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\t%.3f\t", id, topicDistribution));
        for (int i = 0; i < wordsToPrint && i < wordsIds.length; i++) {
            sb.append(String.format("%s (%d) ", alphabet.lookupObject(wordsIds[i]), wordsWeight[i]));
        }
        return sb.toString();
    }

    public int getID() {
        return id;
    }

    public double getTopicDistribution() {
        return topicDistribution;
    }

    public int[] getWordsIds() {
        return wordsIds;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTopicDistribution(double topicDistribution) {
        this.topicDistribution = topicDistribution;
    }

    public void setWordsIds(int[] wordsIds) {
        this.wordsIds = wordsIds;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public int[] getWordsWeight() {
        return wordsWeight;
    }

    public void setWordsWeight(int[] wordsWeight) {
        this.wordsWeight = wordsWeight;
    }
}
