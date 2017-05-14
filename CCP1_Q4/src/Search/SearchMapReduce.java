package Search;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import javafx.util.Pair;

import org.apache.hadoop.conf.Configuration;

public class SearchMapReduce {
	public static String query = "";
	public static Text queryText = new Text("");
	public static Map<Integer, String> word_index;
	
	
	public static void startSearch(String[] args){
		query = args[2].toLowerCase();
		queryText = new Text(query+" :");
		//word_index = new HashMap<String, Integer>();
		word_index = new  HashMap<Integer, String>();

		go_index(args);
		go_search(args);
		


	}
	
	
	/********************************************Index Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/

	public static void go_index(String[] args){
		query = args[2];
		JobClient my_client = new JobClient();
		JobConf job_conf = new JobConf(SearchMapReduce.class);
		job_conf.setJobName("SalesCategory");
		job_conf.setOutputKeyClass(Text.class);
		job_conf.setOutputValueClass(Text.class);
		
		
		// get category
		job_conf.setMapperClass(IndexMapper.class);		
		job_conf.setReducerClass(IndexReducer.class);
		
		
		
		job_conf.setInputFormat(TextInputFormat.class);
		job_conf.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(job_conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(job_conf, new Path(args[1]));
		
		my_client.setConf(job_conf);
		try{
			JobClient.runJob(job_conf);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*******************************************Index Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/

	public static class IndexMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{
		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

				String valueString = value.toString();
				StringTokenizer tokenizerArticle = new StringTokenizer(valueString,"\n");
				IntWritable one = new IntWritable(1);
				while(tokenizerArticle.hasMoreTokens()){
					
					StringTokenizer tokenizer = new StringTokenizer(tokenizerArticle.nextToken());
					

					String[] words = valueString.split(" ");
					if(words[0].contains(query)){

						for(int i = 1;i<words.length;i++){

							output.collect(queryText, new Text(words[i]));
						}
						
					}
					
				
					
					
				}
		}


	}
	
	
	/********************************************Index Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/

	public static class IndexReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text>{

		
		public  void reduce(Text t_key, Iterator<Text> values,  OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String key = t_key.toString();
			
			
			for(Text val:(Iterable<Text>)() -> values){ 
				output.collect(t_key, val);
				word_index.put(Integer.parseInt(val.toString()),key);	
			}
			
			//System.out.println("The value of "+words[0]+" is "+word_index.get(words[0]));
		}

	}

	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/

	public static void go_search(String[] args){
		query = args[2];
		JobClient my_client = new JobClient();
		JobConf job_conf = new JobConf(SearchMapReduce.class);
		job_conf.setJobName("SalesCategory");
		job_conf.setOutputKeyClass(Text.class);
		job_conf.setOutputValueClass(Text.class);
		
		
		// get category
		job_conf.setMapperClass(WordCountMapper.class);		
		job_conf.setReducerClass(WordCountReducer.class);
		
		
		
		job_conf.setInputFormat(TextInputFormat.class);
		job_conf.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(job_conf, new Path(args[3]));
		FileOutputFormat.setOutputPath(job_conf, new Path(args[4]));
		
		my_client.setConf(job_conf);
		try{
			JobClient.runJob(job_conf);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/

	public static class WordCountMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{
		int line = 0;
		String queryString = "";
		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

				String valueString = value.toString();
				StringTokenizer tokenizerArticle = new StringTokenizer(valueString,"\n");
				IntWritable one = new IntWritable(1);
				while(tokenizerArticle.hasMoreTokens()){
					
					StringTokenizer tokenizer = new StringTokenizer(tokenizerArticle.nextToken());
					
					line++;
					// Store the lines before and when query exists
					/*
					if(word_index.containsValue(line+1) ){
						if(!word_index.containsValue(line)) queryString = valueString;
						else queryString += valueString;
						//System.out.println(line+" value:"+queryString);


					}
					*/
					if(word_index.containsKey(line)){
						queryString = valueString;
						output.collect(new Text("Line "+Integer.toString(line)+" :"), new Text(queryString));
					}
					/*
					if(word_index.containsValue(line-1)){
						//System.out.println(line+" match value:"+queryString);
						queryString += valueString;
						if(!word_index.containsValue(line)&&!word_index.containsValue(line+1)){
							output.collect(new Text("Line "+Integer.toString(line)+" :"), new Text(queryString));
						}
					}
					*/
					

						
				}
					
				
					
					
			}
		}


	
	
	
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/
	/********************************************Reducer******************************************************/

	public static class WordCountReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text>{

		
		public  void reduce(Text t_key, Iterator<Text> values,  OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String key = t_key.toString();
			String sum =  "";
			for(Text val:(Iterable<Text>)() -> values){ 
				sum = sum + val;
			}
			output.collect(t_key, new Text(sum));

			//output.collect(t_key, new Text(sum));			

		}

	}
	
}
	
	


