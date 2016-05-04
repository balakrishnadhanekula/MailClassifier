package test.spamham;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TfCalculatorDriver extends Configured implements Tool{

	private static final String OUTPUT_PATH = "/user/spamham/tfoutput";
    private static final String INPUT_PATH = "/user/spamham/output/";//part-r-00000";

    @Override
	public int run(String[] arg0) throws Exception {
		Configuration conf=new Configuration();
		Job job=Job.getInstance(conf, "TfCalculatorDriver");
		job.setJarByClass(TfCalculatorDriver.class);
		
		FileInputFormat.addInputPath(job, new Path(INPUT_PATH));
		// configuration should contain reference to your namenode
		FileSystem fs = FileSystem.get(new Configuration());
		// true stands for recursively deleting the folder you gave
		fs.delete(new Path(OUTPUT_PATH), true);
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));
		
		job.setMapperClass(TfCalculatorMapper.class);
		job.setReducerClass(TfCalculatorReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
		job.waitForCompletion(true);
        return job.waitForCompletion(true) ? 0 : 1;
    }
 
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new TfCalculatorDriver(), args);
        System.exit(res);
    }
}