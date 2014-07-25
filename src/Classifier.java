import java.util.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

public class Classifier {
	Set<WebPage> pages;
	File input;
	File output;

	Classifier(){
		input = new File("./Data/input.csv");
		pages = new HashSet<WebPage>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			String line = "";
			while((line = br.readLine()) != null){
				WebPage p = new WebPage(line);
				p.getPageContents();
				p.getTaggedBody();
				this.pages.add(p);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean classify(){
		this.setScores();

		int i = 0;
		for(WebPage p:this.pages){
			BufferedWriter bw;
			output = new File("./Data/output" + String.valueOf(i) + ".tsv");
			try {
				bw = new BufferedWriter(new FileWriter(this.output, true));
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			System.out.println(String.valueOf(p.tokens.size()) + ", " + String.valueOf(p.words.size()));
			try {
				bw.write(p.url + "\n");
				bw.write("Word\tTFIDF\n");
				System.out.println(p.url + "\n");
				for(Map.Entry<String, Double> ent:p.scores.entrySet()){
					bw.write(ent.getKey() + "\t" + String.valueOf(ent.getValue()) + "\n");
//					System.out.println(ent.getKey() + " : " + String.valueOf(ent.getValue()));
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			i ++;
		}
		return true;
	}
	
	public double IDF(String word){
		int cnt_pages = 0;
		for(WebPage p:pages){
			if(p.isIncluded(word)){
				cnt_pages ++;
			}
		}
		return Math.log((double)this.pages.size()/(double)cnt_pages) + (double)1.0;
	}
	
	public double TFIDF(WebPage page, String word){
//		System.out.println(String.valueOf(page.TF(word)) + String.valueOf(this.IDF(word)));
		return page.TF(word) * this.IDF(word);
	}
	
	public void setScores(){
		for(WebPage p:this.pages){
			for(String w:p.words){
				p.setScore(w, this.TFIDF(p, w));
			}
		}
	}
}
