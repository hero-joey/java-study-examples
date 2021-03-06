package com.hero;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @description: WordCountDemo
 * @date: 2021/1/26 16:16
 * @author: maccura
 * @version: 1.0
 */
public class WordCountDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();

        String[] otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: wordcount <in> [<in>...] <out>");
            System.exit(-1);
        }

        JobConf conf = new JobConf();

        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCountDemo.class);
        job.setMapperClass(WordMapper.class);
        job.setCombinerClass(WordReducer.class);
        job.setReducerClass(WordReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }

        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true)? 0 : 1);

    }

    static class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            Text keyOut;
            IntWritable valueOut = new IntWritable(1);

            StringTokenizer stringTokenizer = new StringTokenizer(value.toString());
            while (stringTokenizer.hasMoreTokens()) {
                keyOut = new Text(stringTokenizer.nextToken());
                context.write(keyOut, valueOut);
            }
        }
    }

    static class WordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            IntWritable wordCount = new IntWritable();

            int sum = 0;
            for (IntWritable intWritable : values) {
                sum += intWritable.get();
                System.out.println("Key: " + key);
                System.out.println("sum value inc: " + sum);
            }
            wordCount.set(sum);
            context.write(key, wordCount);
        }
    }
}
