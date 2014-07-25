import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.io.*;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WebPage {
	// User Agents
	public static final String IE = "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko";
	public static final String Chrome = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.63 Safari/537.36";
	public static final String FireFox = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:9.0.1) Gecko/20100101 Firefox/9.0.1";
	public static final String Safari = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8) AppleWebKit/536.25 (KHTML, like Gecko) Version/6.0 Safari/536.25";
	public static final String iOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25";
	public static final String Android = "Mozilla/5.0 (Linux; U; Android 4.0.1; ja-jp; Galaxy Nexus Build/ITL41D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	String url;
	String title;
	Elements body;
	StringTagger tagger;
	ArrayList<Token> tokens;
	Set<String> words;
	Map<String, Double> scores;

	WebPage(String URL){
		url = URL;
		tagger = SenFactory.getStringTagger(null);
		tokens = new ArrayList<Token>();
		words = new HashSet<String>();
		scores = new HashMap<String, Double>();
	}
	
	public int getPageContents(){
		try {
			// need http protocol
			try {
				Document doc = Jsoup.connect(this.url).userAgent(IE).timeout(5000).get();
				// get page title
				this.title = doc.title();
				this.body = doc.select("body p");
			} catch (HttpStatusException e){
//				e.printStackTrace();
				title = "[Scraping] Error";
				return 1;
			}


		} catch (IOException e) {
			e.printStackTrace();
			title = "[Scraping] Error";
			return 1;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println(e);
			title = "[Scraping] Illegal URL";
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			title = "[Scraping] Unable to get Page";
			return 1;
		}
		return 0;
	}
	
	public void getTaggedBody(){
		if(this.body != null){
			for(Element p:this.body){
				ArrayList<Token> tmpTokens = new ArrayList<Token>();
				try {
					this.tagger.analyze(p.text(), tmpTokens);
                } catch (IOException e) {
                	e.printStackTrace();
                }
//                for(Token token:this.tokens){
//                	if(token.getMorpheme().getPartOfSpeech().indexOf("–¼ŽŒ") >= 0){
//                		System.out.println(token.getSurface() + "," + token.getCost() + "," + token.getMorpheme().getPartOfSpeech());
//                	}
//                }
				for(Token t:tmpTokens){
					this.tokens.add(t);
				}
			}

			for(Token t: this.tokens){
                this.words.add(t.getSurface());
        	}
		}
	}
	
	public double TF(String word){
		int word_cnt = 0;
		for(Token t:this.tokens){
//			if(word.equals("‚Ì") && t.getSurface().equals("‚Ì")){
//				System.out.println("match");
//			}
			if(t.getSurface().equals(word)){
				word_cnt ++;
//				System.out.println(t.getSurface() + ", " + word);
			}
		}
		return (double)word_cnt/(double)this.tokens.size();
	}
	
	public boolean isIncluded(String word){
		for(Token t:this.tokens){
			if(t.getSurface().equals(word)){
				return true;
			}
		}
		return false;
	}
	
	public void setScore(String word, double tfidf){
		scores.put(word, tfidf);
	}
	
}
