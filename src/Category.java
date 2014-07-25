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

public class Category {
	// User Agents
	public static final String IE = "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko";
	public static final String Chrome = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.63 Safari/537.36";
	public static final String FireFox = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:9.0.1) Gecko/20100101 Firefox/9.0.1";
	public static final String Safari = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8) AppleWebKit/536.25 (KHTML, like Gecko) Version/6.0 Safari/536.25";
	public static final String iOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A403 Safari/8536.25";
	public static final String Android = "Mozilla/5.0 (Linux; U; Android 4.0.1; ja-jp; Galaxy Nexus Build/ITL41D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	String name;
	ArrayList<Token> tokens;
	Set<String> words;
	Map<String, Double> scores;
	Set<WebPage> pages;
	File categories;

	Category(String name){
		this.name = name;
		this.tokens = new ArrayList<Token>();
		this.words = new HashSet<String>();
		this.pages = new HashSet<WebPage>();
		this.scores = new HashMap<String, Double>();
		this.categories = new File("./Data/categories.tsv");

        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(categories));
			String line = "";
			while((line = br.readLine()) != null){
				String category_name = line.split("\t")[0];
				String u = line.split("\t")[1];
				URL url = new URL(u);

				if(category_name.equals(this.name)){ 
					System.out.println(this.name);
					// カテゴリのRSSページ取得
					Document rss = Jsoup.parse(url, 5000);
//					Document doc = Jsoup.connect(u).userAgent(Chrome).timeout(5000).get();
					Elements items = rss.select("item");
					for(Element i:items){
						String topURL = i.select("guid").text().replace("yahoo/news/topics", "http://news.yahoo.co.jp/pickup");
						Document topPage = Jsoup.connect(topURL).userAgent(Chrome).timeout(5000).get();
						String articleURL = topPage.select("a.newsLink").attr("href").toString();
						pages.add(new WebPage(articleURL));
					}

					// get page title
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
