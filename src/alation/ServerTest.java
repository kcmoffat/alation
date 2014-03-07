package alation;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ServerTest {

	@Test
	public void ServerTest() {
		// this is mainly a test of feasibility for large numbers of entries.
		// i tested each method with print statements as i went, and would
		// add more extensive unit tests to each method given more time
		InputStream randomWords;
		BufferedReader buffer;
		String line;
		List<String> words = new ArrayList<String> ();
		List<Pair> l = new LinkedList<Pair> ();
		
		// add individual words first, approx 100k words
		try {
			randomWords = new FileInputStream("/Users/kaseymoffat/Documents/workspace/alation/wordsEn.txt");
			buffer = new BufferedReader(new InputStreamReader(randomWords)); 
			while ((line = buffer.readLine()) != null) {
				words.add(line);
				int score = (int) Math.floor(Math.random()*100);
				Pair p = new Pair(line, score);
				l.add(p);
			}
		} catch (Exception e) {
			System.out.println("bad file name");
		}
		
		// now lets combine some words to get to 1MM words
		// NOTE: I added the following JVM arguments before running to increase available memory: -Xms4096m -Xmx4096m
		for (int i = 0; i < words.size(); i++) {
			int indexOne = (int) Math.floor(Math.random()*words.size());
			int indexTwo = (int) Math.floor(Math.random()*words.size());
			int indexThree = (int) Math.floor(Math.random()*words.size());
			String wordOne = words.get(indexOne);
			String wordTwo = words.get(indexTwo);
			String wordThree = words.get(indexThree);
			Pair p1 = new Pair(wordOne + "_" + wordTwo, (int) Math.floor(Math.random()*100));
			Pair p2 = new Pair(wordOne + "_" + wordThree, (int) Math.floor(Math.random()*100));
			Pair p3 = new Pair(wordTwo + "_" + wordThree, (int) Math.floor(Math.random()*100));
			Pair p4 = new Pair(wordOne + "_" + wordTwo + "_" + wordThree, (int) Math.floor(Math.random()*100));
			Pair p5 = new Pair(wordOne + "_" + wordThree + "_" + wordTwo, (int) Math.floor(Math.random()*100));
			Pair p6 = new Pair("_" + wordTwo + "_" + wordOne + "_" + wordThree, (int) Math.floor(Math.random()*100));
			Pair p7 = new Pair("_" + wordTwo + "_" + wordThree + "_" + wordOne + "_", (int) Math.floor(Math.random()*100));
			Pair p8 = new Pair(wordThree + "_" + wordOne + "_" + wordTwo + "_", (int) Math.floor(Math.random()*100));
			Pair p9 = new Pair(wordThree + "_" + wordTwo + "_" + wordOne, (int) Math.floor(Math.random()*100));
			l.add(p1);
			l.add(p2);
			l.add(p3);
			l.add(p4);
			l.add(p5);
//			l.add(p6);
//			l.add(p7);
//			l.add(p8);
//			l.add(p9);
		}
		// Write out file for comparison
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
										new FileOutputStream("/Users/kaseymoffat/Documents/workspace/alation/wordsEnOut.txt")));
			for (Pair p : l) {
				writer.write(p.name() + "\t" + p.score() + "\n");
			}
			} catch (Exception e) {
				
			} finally {
				try {writer.close();} catch (Exception e) {}
			}
		
		System.out.println("Number of entries: " + l.size());
        profileMemory();
		Constructor cp = new Constructor(l);
		System.out.println("Done with constructor setup");
		profileMemory();
		QueryServer qs = new QueryServer(cp.getJson());
		System.out.println("Done with server setup");
		// Some sample queries
		System.out.println(qs.query("as"));
		System.out.println(qs.query("ma"));
		System.out.println(qs.query("macadamiz"));
		System.out.println(qs.query("_"));
	}
	
	public static void profileMemory() {
		// Profile memory (http://viralpatel.net/blogs/getting-jvm-heap-size-used-memory-total-memory-using-java-runtime/)
		int mb = 1024*1024;
		Runtime runtime = Runtime.getRuntime();
        //Print used memory
        System.out.println("Used Memory:"
            + (runtime.totalMemory() - runtime.freeMemory()) / mb + "MB");
 
        //Print free memory
        System.out.println("Free Memory:"
            + runtime.freeMemory() / mb + "MB");
         
        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb + "MB");
 
        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb + "MB");
	}

}
