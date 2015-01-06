package pwr.pjn.wrongnessdetector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import pwr.pjn.wrongnessdetector.topics.TopicBasedWrongnessDetector;
import pwr.pjn.wrongnessdetector.similar.WordsAvgSimilarityWrongnessDetector;

/**
 *
 * @author KonradOliwer
 */
public class Algorithm {
    
    static String inputFile = "";
    static double TOPIC_BASED_WEIDTH = 0.5;
    static double SIMILARITY_BASED_WEIDTH = 1;
///TopicBasedWrongnessDetector
    static int iterations = 1000;
    static double topicsPerSentence = 0.7;
    static double similarityThreshold = 0.1;
    static int sameWordsThreshold = 3;
    static String stoplistFilePath = "stoplists/pl.txt";
    static String lerningSetTopicsPath = "knowledge/simple.wd";
//WordsAvgSimilarityWrongnessDetector
    static int maxStops = 1;
    static boolean countCommas = false;
    
    public void execute() throws IOException {
        TopicBasedWrongnessDetector topicDetector = new TopicBasedWrongnessDetector(iterations, topicsPerSentence, similarityThreshold,
                sameWordsThreshold, stoplistFilePath, lerningSetTopicsPath);
        topicDetector.setInputFile(inputFile);
        WordsAvgSimilarityWrongnessDetector similarityDetector = new WordsAvgSimilarityWrongnessDetector(stoplistFilePath, maxStops,
                countCommas);
        similarityDetector.setInputText(new String(Files.readAllBytes(Paths.get(inputFile))));
        
        double[] resultTB = topicDetector.detect();
        double[] resultS = similarityDetector.detect();
        
        double[] result = new double[resultTB.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (resultTB[i] * TOPIC_BASED_WEIDTH + resultS[i] * SIMILARITY_BASED_WEIDTH)
                    / (TOPIC_BASED_WEIDTH + SIMILARITY_BASED_WEIDTH);
        }
        System.out.println(Arrays.toString(result));
    }
    
    public static void main(String[] args) throws Exception {
        new Algorithm().execute();
    }
}
