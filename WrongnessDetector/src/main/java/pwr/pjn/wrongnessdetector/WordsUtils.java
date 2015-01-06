package pwr.pjn.wrongnessdetector;

/**
 *
 * @author KonradOliwer
 */
public class WordsUtils {

    public static double calculateSimilarity(String word1, String word2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static String extractRelevant(String input){
        return input.replace(",", " , ")
                .replace("[:]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ0-9]* ", " ")
                .replaceAll("[^,\\.A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ\\r\\n\\v\\f]", " ")
                .replaceAll("[ ]+[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]{1,1}[ ]+", " ")
                .replaceAll("[ ]{2,100}", " ");
    }
}