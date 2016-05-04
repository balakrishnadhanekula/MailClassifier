package test.spamham;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SpamClassifierReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
		public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException{
			int wordcount=0;
			for(IntWritable value:values){
				wordcount+=value.get();
			}
			context.write(key, new IntWritable(wordcount));
		}
}


