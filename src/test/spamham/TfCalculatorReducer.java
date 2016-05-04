package test.spamham;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TfCalculatorReducer extends Reducer<Text, Text, Text, Double> {
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException,InterruptedException{
		Double sumOfWordsInDocument = 0.0;
		Map<String, Integer> tempCounter = new HashMap<String, Integer>();
        for (Text val : values) {
            String[] wordCounter = val.toString().split("=");
            tempCounter.put(wordCounter[0], Integer.valueOf(wordCounter[1]));
            sumOfWordsInDocument += Integer.parseInt(val.toString().split("=")[1]);
        }
        for (String wordKey : tempCounter.keySet()) {
        	Integer val = tempCounter.get(wordKey);
            context.write(new Text(wordKey + "@" + key.toString()), new Double(val/sumOfWordsInDocument));
        }
	}

}
