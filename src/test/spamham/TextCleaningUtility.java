package test.spamham;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextCleaningUtility {

	File stopWordFile = null;
	StopWordsLoader stopWordsLoader = new StopWordsLoader();

	public String cleanTextFile(final String token) {
		String word = getOnlyStrings(token);
		String finalWord = null;
		if (!stopWordsLoader.prepareStopWordSet().contains(word)) {
			finalWord = word;
		}
		return finalWord;
	}

	private String getOnlyStrings(String word) {
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
	/*
	 * public void loadStopWords() {
	 * 
	 * Scanner scanner = null; stopWordFile = new File("stopWords.txt"); try {
	 * scanner = new Scanner(stopWordFile);
	 * 
	 * while (scanner.hasNextLine()) { String line = scanner.nextLine();
	 * String[] strArr = line.split(","); for (int i = 0; i < strArr.length;
	 * i++) { stopWordSet.add(strArr[i]); } }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } finally { scanner.close();
	 * } }
	 */
}
