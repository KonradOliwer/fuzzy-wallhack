package pwr.pjn.wrongnessdetector;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import pwr.pjn.wrongnessdetector.LDA.Topic;

public class Test {

    public final static String TEST_DATABASE = "saved_topics/topics_per_sentence_en.txt";

    public static void main(String[] args) throws Exception {
        List<Topic> topics;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEST_DATABASE))) {
            topics = (List<Topic>) ois.readObject();
        }

        for (Topic topic : topics) {
            System.out.println(topic.getTopicDescription(10000));
        }
    }

}
