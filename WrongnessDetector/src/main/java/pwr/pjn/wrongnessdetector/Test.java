package pwr.pjn.wrongnessdetector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import pwr.pjn.wrongnessdetector.topics.LDA.Topic;

public class Test {

    public final static String TEST_DATABASE = "knowledge/simple.wd";

    ;

    public static void main(String[] args) throws Exception {
//        testSavedTopics();  
        testRelevantFilter();
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

    public static void testRelevantFilter() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("learning_data/62956.1111111-1999999.txt")));
        System.out.println(WordsUtils.extractRelevant(content));
    }
}
