package test.spamham;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NaiveBayesClassifier {

	Map<String, Double> spamMap = null;
	Map<String, Double> hamMap = null;
	private ClassLoader classLoader = NaiveBayesClassifier.class
			.getClassLoader();
	TextCleaningUtility cleaningUtility = new TextCleaningUtility();

	public static void main(String[] args) {
		NaiveBayesClassifier classifier = new NaiveBayesClassifier();
		// Mail to be classified
		String file = args[0];
		List<String> wordList = classifier.mailCleaner(file);
		classifier.loadTfFile();
		classifier.calcProbability(wordList);
	}

	private List<String> mailCleaner(final String mailFile) {
		List<String> wordList = new ArrayList<String>();
		Scanner scanner = null;
		File file = new File(classLoader.getResource(mailFile).getFile());
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] strArr = line.split("\\s+");
				for (int i = 0; i < strArr.length; i++) {
					wordList.add(cleaningUtility.cleanTextFile(strArr[i]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wordList;
	}

	private void loadTfFile() {
		Scanner scanner = null;
		spamMap = new HashMap<String, Double>();
		hamMap = new HashMap<String, Double>();

		File file = new File(classLoader.getResource("tfcounters.txt")
				.getFile());

		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] strArr = line.split("\t");
				for (int i = 0; i < strArr.length; i++) {
					String[] keyArr = strArr[0].split("@");
					if (keyArr[1].equalsIgnoreCase("spamFile")) {
						spamMap.put(keyArr[0], new Double(strArr[1]));
					} else {
						hamMap.put(keyArr[0], new Double(strArr[1]));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	// probability of word being spam =
	// (Pr(s)*Pr(w|s))/((Pr(s)*Pr(w|s))+(Pr(h)+Pr(w|h)))
	// probability of word being ham =
	// (Pr(h)*Pr(w|h))/((Pr(h)*Pr(w|h))+(Pr(s)+Pr(w|s)))
	private void calcProbability(final List<String> wordList) {
		//System.out.println("spamMap::: " + spamMap);
		//System.out.println("hamMap::: " + hamMap);

		// Starting with prior probability as 0.5 for each class
		Double spamProb = 0.5;
		Double hamProb = 0.5;

		Double probWordGivenSpam = 0.0;
		Double probWordGivenHam = 0.0;
		Double aggregateSpamProb = 0.0;
		Double aggregateHamProb = 0.0;

		for (String word : wordList) {
			if (spamMap.containsKey(word)) {
				probWordGivenSpam = spamMap.get(word);
			}

			if (hamMap.containsKey(word)) {
				probWordGivenHam = hamMap.get(word);
			}

			Double probSpamGivenWord = java.lang.Math
					.log((spamProb * probWordGivenSpam)
							/ ((spamProb * probWordGivenSpam) + (hamProb * probWordGivenHam)));
			Double probHamGivenWord = java.lang.Math
					.log((hamProb * probWordGivenHam)
							/ ((hamProb * probWordGivenHam) + (spamProb * probWordGivenSpam)));

			aggregateSpamProb = aggregateSpamProb + probSpamGivenWord;
			aggregateHamProb = aggregateHamProb + probHamGivenWord;
		}
		System.out.println("::aggregateHamProb:: "+aggregateHamProb);
		System.out.println("::aggregateSpamProb:: "+aggregateSpamProb);
		if (java.lang.Math.abs(aggregateSpamProb) > java.lang.Math.abs(aggregateHamProb)) {
			System.out.println("Mail is spam");
		} else {
			System.out.println("Mail is not a spam");
		}
	}
}
