package pwr.pjn.wrongnessdetector.similar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.pjn.wrongnessdetector.WordsUtils;

/**
 *
 * @author KonradOliwer
 */
public class WordsAvgSimilarityWrongnessDetector {

    /*
     * @similarityThreshhold value between 0 and 1, below that value avg similarity for the word will be considered to low
     *
     * @return indexes of wrong words
     */
    public List<Integer> detectWrongness(String input, double similarityThreshhold, int maxStops, boolean countCommas) {
        List<Integer> indexes = new ArrayList();
        String[] words = input.split(" ");
        Interval interval = new Interval();
        for (int i = 0; i < words.length; i++) {
            calculateInterval(interval, words, i, maxStops, countCommas);
            for (i = interval.begining; i < interval.end; i++) {
                double similarity = 1; //if there is one word it will pass every threshold
                for (int j = interval.begining; j < interval.end; j++) {
                    if (i != j) {
                        similarity += WordsUtils.calculateSimilarity(words[i], words[j]);
                    }
                }
                if (similarity / (1 + interval.end - interval.begining) < similarityThreshhold) {
                    indexes.add(i);
                }
            }
        }
        return indexes;
    }

    private void calculateInterval(Interval interval, String[] words, int i, int maxStops, boolean countCommas) {
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
