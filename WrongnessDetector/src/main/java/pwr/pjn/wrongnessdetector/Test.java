package pwr.pjn.wrongnessdetector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import pwr.pjn.wrongnessdetector.topics.LDA.Topic;

public class Test {

    public final static String TEST_DATABASE = "knowledge/simple.wd";;

    public static void main(String[] args) throws Exception {
        testSavedTopics();
    }

    public static void testSavedTopics() throws IOException, ClassNotFoundException {
        List<Topic> topics;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEST_DATABASE))) {
            topics = (List<Topic>) ois.readObject();
        }

        for (Topic topic : topics) {
            System.out.println(topic.getTopicDescription(10000));
        }

    }
}
