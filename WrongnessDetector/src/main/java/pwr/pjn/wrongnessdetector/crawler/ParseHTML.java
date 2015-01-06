package pwr.pjn.wrongnessdetector.crawler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ParseHTML {
	
	private static int topic = 62956;
	
	static void getArticles(int from, int to)throws IOException{
		Document doc;
		FileWriter out;
		String text;
		File f=new File("/text-data/");
		f.mkdir();
		System.out.println("Files saved in:"+f.getAbsoluteFile());
		for (int i=from;i<=to;i++){
	        doc = Jsoup.connect("http://www.rp.pl/artykul/"+topic+","+i+".html").get();
	        text = doc.body().select(".storyContent > p").text();
	    
	        if(!text.isEmpty()){
	        	f=new File("/text-data/"+topic+"-"+i+".txt");
	            System.out.println((i-from)*100.0/(double)(to-from)+"%");    	
	        	out = new FileWriter(f);
	        	out.write(text);
	        	out.close();
	        }
		}
		
		
	}
	
	
    public static void main(String args[]) throws IOException{
    	
    	getArticles(1166476,1166576);

    }
    
    
}
