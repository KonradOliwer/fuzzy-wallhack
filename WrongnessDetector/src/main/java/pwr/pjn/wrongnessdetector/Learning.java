package pwr.pjn.wrongnessdetector;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.pjn.wrongnessdetector.LDA.LDAWrapper;
import pwr.pjn.wrongnessdetector.LDA.Topic;
import pwr.pjn.wrongnessdetector.LDA.TopicsUtils;

/**
 *
 * @author KonradOliwer
 */
public class Learning {

    public static final int ITERATIONS_NUMBER = 1000;
    public static final double LERNING_PER_SENTENCE_POW = 0.8;

    public static void main(String[] args) throws Exception {
        String stoplistFilePath = "stoplists/en.txt";
        String processedFilePath = "test_data/ap.txt";
        String saveFileName = "saved_topics/topics_per_sentence_en.txt";
        learnPerSentence(processedFilePath, stoplistFilePath, saveFileName);
    }

    public static void learnPerSentence(String processedFilePath, String stoplistFilePath, String saveFileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(processedFilePath));
            int ch;
            int counter = 0;
            while ((ch = reader.read()) != -1) {
                if (ch == '.') {
                    counter++;
                }
            }
            learn(processedFilePath, stoplistFilePath, saveFileName,  (int) Math.pow(counter, LERNING_PER_SENTENCE_POW));
        } catch (IOException ex) {
            Logger.getLogger(Learning.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Learning.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void learn(String processedFilePath, String stoplistFilePath, String saveFileName, int numTopics) {
        ObjectOutputStream oos = null;
        try {
            LDAWrapper lda = new LDAWrapper(stoplistFilePath);
            List<Topic> topics = lda.execute(processedFilePath, numTopics, ITERATIONS_NUMBER);
            TopicsUtils.topicsPruning(topics, 0, 4, 100000);
            oos = new ObjectOutputStream(new FileOutputStream(saveFileName));
            oos.writeObject(topics);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(Learning.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(Learning.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
