package pwr.pjn.wrongnessdetector;

import java.io.IOException;
import java.nio.charset.Charset;
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
    
    static String inputFile = "C:\\Users\\kcomr_000\\Documents\\NetBeansProjects\\fuzzy-wallhack\\WrongnessDetector\\test_data\\ap.2.txt";
    static double TOPIC_BASED_WEIDTH = 0;
    static double SIMILARITY_BASED_WEIDTH = 1;
///TopicBasedWrongnessDetector
    static int iterations = 1;
    static double topicsPerSentence = 0.04;
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
//        similarityDetector.setInputText(String.join(" ", (Files.readAllLines(Paths.get(inputFile), Charset.forName("UTF-8")))));
        similarityDetector.setInputText(WordsUtils.loadFile(inputFile));
        
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
