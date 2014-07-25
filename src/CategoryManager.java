import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

public class CategoryManager {
	Set<Category> categories;
	File input;
	CategoryManager(){
		input = new File("./Data/categories.tsv");
		categories = new HashSet<Category>();
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(input));
			String line = "";
			while((line = br.readLine()) != null){
				System.out.println("new Category()");
				System.out.println(line.split("\t")[0]);
				categories.add(new Category(line.split("\t")[0]));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

	}
	
	public Category getCategoryByName(String n){
		for(Category c:this.categories){
			if(c.name.equals(n)){
				return c;
			}
		}
		return null;
	}

}
