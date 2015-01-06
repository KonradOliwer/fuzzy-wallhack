package pwr.pjn.wrongnessdetector;

import java.util.List;
import pwr.pjn.wrongnessdetector.topics.LDA.Topic;
import pwr.pjn.wrongnessdetector.topics.LDA.TopicsUtils;

/**
 *
 * @author KonradOliwer
 */
public class Algorithm {

    private final String lerningSetTopicsPath = "saved_topics/topics_2248_en.txt";

    public void execute() {
        List<Topic> lerningSetTopics = TopicsUtils.loadTopics(lerningSetTopicsPath);
        TopicsUtils.topicsPruning(lerningSetTopics, 3, 6, 100);

        for (Topic topic : lerningSetTopics) {
            System.out.println(topic.getTopicDescription(20));
        }
    }

    public static void main(String[] args) throws Exception {
        new Algorithm().execute();
    }
}
