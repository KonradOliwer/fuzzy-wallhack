package pwr.pjn.wrongnessdetector.similar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void setInputText(String text) {
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
        List<String> pairs;
        int i = 0;
        while (i < words.length) {
            if (skipWord(words[i], stoplist)) {
                similarities[i++] = 1;
            } else {
                calculateInterval(interval, words, i);
                pairs = new ArrayList();
                double sumSimilarity = 0; //if there is one word it will pass every threshold
                for (int j = i + 1; j < interval.end; j++) {
                    if (!skipWord(words[j], stoplist)) {
                        pairs.add(words[i]);
                        pairs.add(words[j]);
                    }
                }
                double[] r = WordnetUtils.calculateSimilarity(pairs.toArray(new String[pairs.size()]));

                for (double d : r) {
                    sumSimilarity += d;
                }
                for (i = interval.begining; i < interval.end + 1; i++) {
                    if (interval.end - interval.begining == 1) {
                        similarities[i] = 1;
                    } else {
                        similarities[i] = sumSimilarity / (interval.end - interval.begining);
                    }
                }
            }
        }
        return similarities;
    }

    private boolean skipWord(String word, List<String> stoplist) {
        return word.equals(".") || word.equals(",") || word.contains(" ") || stoplist.contains(word);
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
            interval.end = i;
        }
    }

    private List<String> loadStoplis(String path) {
        return new ArrayList(Arrays.asList(WordsUtils.loadFile(path).split(System.getProperty("line.separator"))));
    }
}
