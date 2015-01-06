package pwr.pjn.wrongnessdetector.topics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pwr.pjn.wrongnessdetector.WordsUtils;
import pwr.pjn.wrongnessdetector.WrongnessDetector;
import pwr.pjn.wrongnessdetector.similar.WordnetUtils;
import pwr.pjn.wrongnessdetector.topics.LDA.LDAWrapper;
import pwr.pjn.wrongnessdetector.topics.LDA.Topic;
import pwr.pjn.wrongnessdetector.topics.LDA.TopicsUtils;

/**
 *
 * @author KonradOliwer
 */
public class TopicBasedWrongnessDetector implements WrongnessDetector {

    private static final int NO_INDEX = -1;
    private final int iterations;
    private final double similarityThreshold;
    private final int sameWordsThreshold;
    private final String stoplistFilePath;
    private final String lerningSetTopicsPath;
    private String inputFilePath;
    private final double topicsPerSentence;

    public TopicBasedWrongnessDetector(int iterations, double topicsPerSentence, double similarityThreshold, int sameWordsThreshold,
            String stoplistFilePath, String lerningSetTopicsPath) {
        this.iterations = iterations;
        this.topicsPerSentence = topicsPerSentence;
        this.similarityThreshold = similarityThreshold;
        this.sameWordsThreshold = sameWordsThreshold;
        this.stoplistFilePath = stoplistFilePath;
        this.lerningSetTopicsPath = lerningSetTopicsPath;
    }

    public void setInputFile(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    @Override
    public double[] detect() {
        double[] result = null;
        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            String[] words = content.split(" ");
            result = new double[words.length];
            List<String> wrongWords = pickWords();
            for (int i = 0; i < words.length; i++) {
                result[i] = wrongWords.contains(words[i]) ? 0 : 1;
            }
        } catch (IOException ex) {
            Logger.getLogger(TopicBasedWrongnessDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result == null ? new double[0] : result;
    }

    public List<String> pickWords() throws IOException {
        List<String> result = new ArrayList();
        LDAWrapper lda = new LDAWrapper(stoplistFilePath);
        int numTopics = (int) (TopicsUtils.getNumberOfSentyencesInDocument(inputFilePath) * topicsPerSentence);
        List<Topic> topics = lda.execute(stoplistFilePath, numTopics, iterations);
        List<Topic> lerningSetTopics = TopicsUtils.loadTopics(lerningSetTopicsPath);

        int[] temp1 = new int[numTopics];
        IndexedValue[] temp2 = new IndexedValue[lerningSetTopics.size()];
        for (int i = 0; i < temp2.length; i++) {
            temp2[i] = new IndexedValue(i);
        }
        for (Topic topic : topics) {
            result.addAll(findIncorectWords(topic, lerningSetTopics, temp1, temp2));
        }

        return result;
    }

    private List<String> findIncorectWords(Topic topic, List<Topic> lerningSetTopics, int[] closest, IndexedValue[] occurences) {
        findStreightDistance(topic, lerningSetTopics, closest, occurences);
        double[] avgWordDistances = new double[topic.getWordsNumber()];
        for (int i = 0; i < avgWordDistances.length; i++) {
            avgWordDistances[i] = calculateAvgWordSimilarity(topic.getWord(i), lerningSetTopics, closest);
        }
        List<String> result = new ArrayList();
        for (int i = 0; i < avgWordDistances.length; i++) {
            if (avgWordDistances[i] < similarityThreshold) {
                result.add(topic.getWord(i));
            }
        }
        return result;
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
        for (int i = 0; i < closest.length; i++) {
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
                LinkedList<String> pairs= new LinkedList<String>();
                for (int j = 0; j < lerningSetTopics.get(i).getWordsNumber(); j++) {
                	pairs.add(word);
                	pairs.add(lerningSetTopics.get(i).getWord(j));
                	
                	
                    //double similarity = WordsUtils.calculateSimilarity(word, lerningSetTopics.get(i).getWord(j));
                    
                }

                double[]r=WordnetUtils.calculateSimilarity(pairs.toArray(new String[0]));
        		
        		for(double d:r){
        			if (d > best) {
                        best = d;
                    }
        		}
                sum += best;
                number++;
            }
        }
        return number > 0 ? sum / closest.length : 1;
    }
}
