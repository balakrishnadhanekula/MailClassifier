package test.spamham;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SpamClassifierDriver{
	
	public static void main(String args[]) throws Exception{
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf, "SpamClassifierDriver");
		job.setJarByClass(SpamClassifierDriver.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		// configuration should contain reference to your namenode
		FileSystem fs = FileSystem.get(new Configuration());
		// true stands for recursively deleting the folder you gave
		fs.delete(new Path(args[1]), true);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(SpamClassifierMapper.class);
		job.setReducerClass(SpamClassifierReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		//job.addCacheFile(new URI("hdfs://localhost:9000/user/spamham/input/cache/stopWords.txt#stopWords.txt"));
		
		/*URI[] cacheFiles= job.getCacheFiles();
        if(cacheFiles != null) {
            for (URI cacheFile : cacheFiles) {
                System.out.println("Cache file ->" + cacheFile);
            }
        }*/
		
		job.waitForCompletion(true);
	}

}
