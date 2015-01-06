package pwr.pjn.wrongnessdetector.topics;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import pwr.pjn.wrongnessdetector.topics.LDA.LDAWrapper;
import pwr.pjn.wrongnessdetector.topics.LDA.Topic;
import pwr.pjn.wrongnessdetector.topics.LDA.TopicsUtils;

/**
 *
 * @author KonradOliwer
 */
public class TopicBasedWrongnessDetector {

    private static final int NO_INDEX = -1;
    private final int iterations;
    private final int consideredTopicsNumber;
    private final double similarityThreshold;
    private final int sameWordsThreshold;

    TopicBasedWrongnessDetector(int iterations, int consideredTopicsNumber, double similarityThreshold, int sameWordsThreshold) {
        this.iterations = iterations;
        this.consideredTopicsNumber = consideredTopicsNumber;
        this.similarityThreshold = similarityThreshold;
        this.sameWordsThreshold = sameWordsThreshold;
    }

    public String detect(String inputFilePath, String stoplistFilePath, String lerningSetTopicsPath) throws IOException {
        StringBuilder sb = new StringBuilder();
        LDAWrapper lda = new LDAWrapper(stoplistFilePath);
        int numTopics = TopicsUtils.getNumberOfSentyencesInDocument(inputFilePath);
        List<Topic> topics = lda.execute(stoplistFilePath, numTopics, iterations);
        List<Topic> lerningSetTopics = TopicsUtils.loadTopics(lerningSetTopicsPath);

        int[] temp1 = new int[consideredTopicsNumber];
        IndexedValue[] temp2 = new IndexedValue[lerningSetTopics.size()];
        for (int i = 0; i < temp1.length; i++) {
            temp2[i] = new IndexedValue(i);
        }
        for (Topic topic : topics) {
            sb.append(findIncorectWords(topic, lerningSetTopics, temp1, temp2));
        }

        return sb.toString();
    }

    private StringBuilder findIncorectWords(Topic topic, List<Topic> lerningSetTopics, int[] closest, IndexedValue[] occurences) {
        findStreightDistance(topic, lerningSetTopics, closest, occurences);
        double[] avgWordDistances = new double[topic.getWordsNumber()];
        for (int i = 0; i < avgWordDistances.length; i++) {
            avgWordDistances[i] = calculateAvgWordSimilarity(topic.getWord(i), lerningSetTopics, closest);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < avgWordDistances.length; i++) {
            if (avgWordDistances[i] < similarityThreshold){
                sb.append(topic.getWord(i)).append(' ');
            }
        }
        return sb;
    }

    private void findStreightDistance(Topic topic, List<Topic> lerningSetTopics, int[] closest, IndexedValue[] occurences) {
        for (IndexedValue occurence : occurences) {
            occurence.value = 0;
        }
        for (int index = 0; index < lerningSetTopics.size(); index++) {
            for (int i = 0; i < topic.getWordsNumber(); i++) {
                for (int j = 0; j < lerningSetTopics.get(index).getWordsNumber(); j++) {
                    if (topic.getWord(i).equals(lerningSetTopics.get(index).getWord(j))) {
                        occurences[index].value++;
                        break;
                    }
                }
            }
        }
        Arrays.sort(occurences);
        for (int i = 0; i < consideredTopicsNumber; i++) {
            if (sameWordsThreshold >= occurences[occurences.length - i - 1].value) {
                closest[i] = occurences[occurences.length - i - 1].id;
            } else {
                closest[i] = NO_INDEX;
            }
        }
    }

    private double calculateAvgWordSimilarity(String word, List<Topic> lerningSetTopics, int[] closest) {
        double sum = 0;
        int number = 0;
        for (int i : closest) {
            if (i != NO_INDEX) {
                double best = Double.MIN_VALUE;
                for (int j = 0; j < lerningSetTopics.get(i).getWordsNumber(); j++) {
                    double similarity = calculateSimilarity(word, lerningSetTopics.get(i).getWord(j));
                    if (similarity > best) {
                        best = similarity;
                    }
                }
                sum += best;
                number++;
            }
        }
        return number > 0 ? sum / closest.length : 1;
    }

    private double calculateSimilarity(String word1, String word2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
