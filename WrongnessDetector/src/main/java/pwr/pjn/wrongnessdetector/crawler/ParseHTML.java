package pwr.pjn.wrongnessdetector.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorfeuszUsage;

public class ParseHTML {
    
    private static final boolean ENABLE_PROGRESS_LOGGING = false;
    private static final String STORGE_FILE_PATH = "learning_data/";
    private static final Morfeusz MORFEUSZ = Morfeusz.createInstance(MorfeuszUsage.ANALYSE_ONLY);
    private static int topic = 62956;
    
    static void getArticles(int from, int to) throws IOException {
        Document doc;
        String text;
        File f = new File(STORGE_FILE_PATH);
        f.mkdir();
        BufferedWriter out = null;
        f = new File(String.format("%s/%d.%d-%d.txt", STORGE_FILE_PATH, topic, from, to));
        try {
            out = new BufferedWriter(new FileWriter(f));
            for (int i = from; i <= to; i++) {
                doc = Jsoup.connect("http://www.rp.pl/artykul/" + topic + "," + i + ".html").get();
                text = doc.body().select(".storyContent > p").text();
                
                if (!text.isEmpty()) {
                    text = processInput(text, topic + "-" + i);
                    out.write(text);
                    out.newLine();
                }
                if (ENABLE_PROGRESS_LOGGING) {
                    System.out.println((i - from) * 100.0 / (double) (to - from) + "%");
                }
            }
            System.out.println("File saved in:" + f.getAbsolutePath());
        } finally {
            out.close();
        }
    }
    
    public static String processInput(String text, String name) {
        StringBuilder sb = new StringBuilder();
        text = text.replace(System.getProperty("line.separator"), " ").replace(".", " . ");
        for (String word : text.split(" ")) {
            word = word.trim();
            if (word.length() > 0) {
                if (word.length() > 1 && !word.contains(Character.toString ((char) 160))) {
                    sb.append(MORFEUSZ.analyseAsIterator(word).peek().getLemma());
                } else {
                    sb.append(word);
                }
                sb.append(" ");
            }
        }
        return String.format("%s \t_\t%s", name, sb);
    }
    
    public static void main(String args[]) throws IOException {
        getArticles(1111111, 1999999);
    }
}
