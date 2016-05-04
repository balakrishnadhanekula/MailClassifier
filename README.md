# MailClassifier
Spam Ham Mail Classifier

In this project implemented Naive Bayes Classifier on the Enron mails corpus

Downloaded the preprocessed mail from http://www.aueb.gr/users/ion/data/enron-spam/

Consolidated the spam and ham as two seperate text files.

Imported the two files to HDFS.

Step 1: Developed the MR job SpamClassifierDriver which performs below tasks
  Removed the numerals
  Removed special characters
  Filtered the stop words available from wiki.
  Calculated the each word count.
  
Step 2: Developed second MR job TfCalculatorDriver
  Calculate the each term frequencies (word count/total words in each class).
  A single file is created as 
  daughters@hamFile	1.673948341954167E-5
  claimed@hamFile	3.347896683908334E-5
  efface@spamFile	4.634435711107815E-6

Step 3: Developed an utility NaiveBayesClassifier which takes the term frequency file and a new mail as input
    Parsed the new mail to individual words and applied the text cleaner utility.
    Calculated the probability of each word and applied log function. Added all the probability values. Highest probability value decided the class the the new mail belongs.
