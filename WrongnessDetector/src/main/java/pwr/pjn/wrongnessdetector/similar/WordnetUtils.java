package pwr.pjn.wrongnessdetector.similar;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WordnetUtils {

	private static final String FPATH = "similarity.py";

	public static double[] calculateSimilarity(String[] pairs) {
		double[] result= new double[pairs.length / 2];;
		
		try {

			String[] argss = new String[pairs.length + 2];
			argss[0] = "python";
			argss[1] = FPATH;
			System.arraycopy(pairs, 0, argss, 2, pairs.length);
			ProcessBuilder pb = new ProcessBuilder(argss);
			Process p = pb.start();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String ret;
			for (int i = 0; i < result.length; i++) {
				ret = in.readLine();
				result[i] = Double.parseDouble(ret);
			}
		} catch (Exception e) {
			//System.out.println(e);
		}
		return result;
	}

	public static void main(String a[]) {
		long start_time = System.currentTimeMillis();
		String[]pairs={"pies",  "kot",  "samolot",  "kot", "pies",   "samolot"};
		double[]r=calculateSimilarity(pairs);
		long end_time = System.currentTimeMillis();
		for(double d:r){
			System.out.println(d);
		}
		System.out.println(end_time - start_time);
	}
}