package pwr.pjn.wrongnessdetector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.pjn.wrongnessdetector.LDA.Topic;

/**
 *
 * @author KonradOliwer
 */
public class Algorithm {

    private final String lerningSetTopicsPath = "saved_topics/topics_2248_en.txt";

    public void execute() {
        List<Topic> lerningSetTopics = loadTopics(lerningSetTopicsPath);
        topicsPruning(lerningSetTopics, 3, 6, 100);

        for (Topic topic : lerningSetTopics) {
            System.out.println(topic.getTopicDescription(20));
        }
    }

    public static List<Topic> loadTopics(String path) {
        ObjectInputStream ois = null;
        List<Topic> topics = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            topics = (List<Topic>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return topics;
    }

    public static void topicsPruning(List<Topic> topics, int minVotes, int minWords, int maxWords) {
        Iterator<Topic> iterator = topics.iterator();
        while (iterator.hasNext()) {
            Topic topic = iterator.next();
            if (topic.getWordsIds().length < minWords) {
                iterator.remove();
            } else {
                int index = 0;
                for (; index < maxWords && index < topic.getWordsIds().length; index++) {
                    if (topic.getWordsWeight()[index] < minVotes) {
                        break;
                    }
                }
                if (index < minWords) {
                    iterator.remove();
                } else {
                    int size = index < maxWords ? index : maxWords;
                    int[] ids = new int[size];
                    System.arraycopy(topic.getWordsIds(), 0, ids, 0, size);
                    topic.setWordsIds(ids);
                    int[] weights = new int[size];
                    System.arraycopy(topic.getWordsWeight(), 0, weights, 0, size);
                    topic.setWordsIds(weights);
                }
            }
        }
    }
}
