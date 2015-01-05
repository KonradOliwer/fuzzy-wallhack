package pwr.pjn.wrongnessdetector;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import pwr.pjn.wrongnessdetector.LDA.LDAWrapper;
import pwr.pjn.wrongnessdetector.LDA.Topic;

public class Test {

    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
        test3();
    }
    
    public static void test1() throws IOException{
        String stoplistFilePath = "stoplists/en.txt";
        String processedFilePath = "test_data/ap.txt";
        int numTopics = 2248;
        int numIterations = 2000;

        LDAWrapper lda = new LDAWrapper(stoplistFilePath);
        List<Topic> topics = lda.execute(processedFilePath, numTopics, numIterations);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_topics/topics_2248_en.txt"));
        oos.writeObject(topics);
        oos.close();
    }
    
    public static void test2() throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved_topics/topics_2248_en.txt"));
        List<Topic> topics = (List<Topic>) ois.readObject();
        ois.close();

        for (Topic topic : topics) {
            System.out.println(topic.getTopicDescription(10000));
        }
    }
    
    public static void test3() throws IOException, ClassNotFoundException{
        new Algorithm().execute();
    }

}
