package pwr.pjn.wrongnessdetector.morfeusz;


import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorfeuszUsage;

public class MorfeuszTest {

    public static void main(String args[]) {
    	
    	Morfeusz m = Morfeusz.createInstance(MorfeuszUsage.ANALYSE_ONLY);
    	System.out.println(m.analyseAsIterator("Z.O.O").peek().getLemma());

    }
}
