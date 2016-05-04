package test.spamham;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class MailClassifier {

	static Logger LOG = Logger.getLogger(MailClassifier.class);
	static MailClassifier classifier = new MailClassifier();
	static Set<String> stopWordSet = new LinkedHashSet<String>();

	public static void main(String[] args) {
		classifier.loadStopWords();
		LOG.debug("Stop Words:: "+stopWordSet);
		Map<String, Integer> spamMap = classifier.loadMails("spamFile.txt");
		LOG.debug("Spam Mail Word Map"+spamMap);
		Map<String, Integer> hamMap = classifier.loadMails("hamFile.txt");
		LOG.debug("Ham Mail Word Map"+hamMap);
	}

	private Map<String, Integer> loadMails(String fileName) {
		Scanner scanner = null;
		ClassLoader classLoader = MailClassifier.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] strArr = line.split("\\s+");
				for (int i = 0; i < strArr.length; i++) {
					String key = strArr[i];
					key = getOnlyStrings(key);
					if (!stopWordSet.contains(key)) {
						if (map.containsKey(key)) {
							Integer val = map.get(key);
							map.put(key, (val + 1));
						} else {
							map.put(key, 1);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return map;
	}

	public void loadStopWords() {

		Scanner scanner = null;
		ClassLoader classLoader = MailClassifier.class.getClassLoader();
		File file = new File(classLoader.getResource("stopWords.txt").getFile());

		try {
			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] strArr = line.split(",");
				for (int i = 0; i < strArr.length; i++) {
					stopWordSet.add(strArr[i]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
	public String getOnlyStrings(String word) throws IOException {
		String finalWord = null;
		if (null != word) {
			if (word.trim().length() > 2) {
				Pattern pattern = Pattern.compile("[^a-z A-Z]");
				Matcher matcher = pattern.matcher(word);
				String str = matcher.replaceAll("");

				if (null != str) {
					if (str.trim().length() != 0) {
						finalWord = str;
					}
				}
			}
		} else {
			finalWord = "";
		}
		return finalWord;
	}
}