package pwr.pjn.wrongnessdetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KonradOliwer
 */
public class WordsUtils {

    public static double calculateSimilarity(String word1, String word2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String extractRelevant(String input) {
        return input.replace(",", " , ")
                .replace("[:]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ0-9]* ", " ")
                .replaceAll("[^,\\.A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\\r\\n\\v\\f]", " ")
                .replaceAll("[ ]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,1}[ ]+", " ")
                .replaceAll("[ ]{2,100}", " ");
    }

    public static String loadFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String ls = System.getProperty("line.separator");
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
                if (line != null) {
                    sb.append(ls);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(WordsUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(WordsUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString();
    }
}
