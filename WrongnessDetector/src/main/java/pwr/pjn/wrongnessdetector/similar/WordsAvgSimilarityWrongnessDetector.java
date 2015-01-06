package pwr.pjn.wrongnessdetector.similar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.pjn.wrongnessdetector.WordsUtils;
import pwr.pjn.wrongnessdetector.WrongnessDetector;

/**
 *
 * @author KonradOliwer
 */
public class WordsAvgSimilarityWrongnessDetector implements WrongnessDetector {

    private final String stoplistDir;
    private final int maxStops;
    private final boolean countCommas;
    private String[] text;

    public WordsAvgSimilarityWrongnessDetector(String stoplistDir, int maxStops, boolean countCommas) {
        this.stoplistDir = stoplistDir;
        this.maxStops = maxStops;
        this.countCommas = countCommas;
    }
    
    public void setInputText(String text){
        this.text = text.split(" ");
    }
    
    @Override
    public double[] detect() {
        return getAvgSimilarity(text);
    }

    public double[] getAvgSimilarity(String[] words) {
        List<String> stoplist = loadStoplis(stoplistDir);
        double[] similarities = new double[words.length];
        Interval interval = new Interval();
        for (int i = 0; i < words.length; i++) {
            calculateInterval(interval, words, i);
            for (i = interval.begining; i < interval.end; i++) {
                double sumSimilarity = 1; //if there is one word it will pass every threshold
                if (!skipWord(words[i], stoplist)) {
                    for (int j = interval.begining; j < interval.end; j++) {
                        if (i != j && !skipWord(words[j], stoplist)) {
                            sumSimilarity += WordsUtils.calculateSimilarity(words[i], words[j]);
                        }
                    }
                }
                similarities[i] = sumSimilarity / (1 + interval.end - interval.begining);
            }
        }
        return similarities;
    }

    private boolean skipWord(String word, List<String> stoplist) {
        return word.equals(".") || word.equals(",") || word.contains(" ") || !stoplist.contains(word);
    }

    private void calculateInterval(Interval interval, String[] words, int i) {
        interval.begining = i;
        int stopChars = 0;
        while (i < words.length) {
            if (words[i].equals(".") || (countCommas && words[i].equals(","))) {
                stopChars++;
            }
            if (stopChars >= maxStops) {
                interval.end = i;
                break;
            }
            i++;
        }
        if (i == words.length) {
            interval.end = i - 1;
        }
    }

    private List<String> loadStoplis(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException ex) {
            Logger.getLogger(WordsAvgSimilarityWrongnessDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList();
    }
}
