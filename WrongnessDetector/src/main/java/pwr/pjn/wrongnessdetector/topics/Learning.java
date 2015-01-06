package pwr.pjn.wrongnessdetector.topics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.pjn.wrongnessdetector.topics.LDA.LDAWrapper;
import pwr.pjn.wrongnessdetector.topics.LDA.Topic;
import pwr.pjn.wrongnessdetector.topics.LDA.TopicsUtils;

/**
 *
 * @author KonradOliwer
 */
public class Learning {

    private static final int ITERATIONS_NUMBER = 1000;
    private static final double LERNING_PER_SENTENCE_POW = 0.8;
    private static final String SAVE_FILE_BASEDIR = "knowledge";

    public static void main(String[] args) throws Exception {
        String stoplistFilePath = "stoplists/pl.txt";
        String processedFilePath = "learning_data/62956.1166476-1166576.txt";
        String saveFileName = SAVE_FILE_BASEDIR + "/simple.wd";
        new File(SAVE_FILE_BASEDIR).mkdir();
        learnPerSentence(processedFilePath, stoplistFilePath, saveFileName);
    }

    public static void learnPerSentence(String processedFilePath, String stoplistFilePath, String saveFileName) {
        int sentences = TopicsUtils.getNumberOfSentyencesInDocument(processedFilePath);
        int topicNumber = (int) Math.pow(sentences, LERNING_PER_SENTENCE_POW);
        learn(processedFilePath, stoplistFilePath, saveFileName, topicNumber);
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
