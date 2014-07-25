import java.util.StringTokenizer;

public class PageClassification {
	public static void main(String[] args) {
		CategoryManager cm = new CategoryManager();
		Classifier c = new Classifier();
		c.setInputPages(cm.getCategoryByName("entertainment").pages);

		c.classify();
		System.out.println("FINISH");
	}
}
