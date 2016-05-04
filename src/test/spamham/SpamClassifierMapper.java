package test.spamham;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class SpamClassifierMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {
	private Text word = new Text();
	StopWordsLoader stopWordsLoader = new StopWordsLoader();
	TextCleaningUtility cleaningUtility = new TextCleaningUtility();

	@Override
	protected void setup(Context context) throws IOException {

	}

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line);
		String fileName = ((FileSplit) context.getInputSplit()).getPath()
				.getName();
		StringBuffer sb = null;
		try {
			while (tokenizer.hasMoreTokens()) {
				String token = cleaningUtility.cleanTextFile(tokenizer
						.nextToken());
				sb = new StringBuffer();
				word.set(token);
				sb.append(word);
				sb.append("@");
				sb.append(fileName);
				context.write(new Text(sb.toString()), new IntWritable(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
