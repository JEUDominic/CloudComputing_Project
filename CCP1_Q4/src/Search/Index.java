package Search;
import java.io.IOException;
import java.util.Iterator;
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

public class Index {
	

	
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/
	/********************************************Driver******************************************************/

	public static class WordCountDriver {
		public static void go(String[] args){
			
			JobClient my_client = new JobClient();
			JobConf job_conf = new JobConf(WordCountDriver.class);
			job_conf.setJobName("SalesCategory");
			job_conf.setOutputKeyClass(Text.class);
			job_conf.setOutputValueClass(IntWritable.class);
			
			
			// get category
			job_conf.setMapperClass(WordCountMapper.class);		
			job_conf.setReducerClass(WordCountReducer.class);
			
			
			
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
		
		
	}
	
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/
	/********************************************Mapper******************************************************/

	public static class WordCountMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>{
		int line =0;
		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {

				String valueString = value.toString();
				StringTokenizer tokenizerArticle = new StringTokenizer(valueString,"\n");
				IntWritable one = new IntWritable(1);
				//int line = 0;
				while(tokenizerArticle.hasMoreTokens()){
					
					StringTokenizer tokenizer = new StringTokenizer(tokenizerArticle.nextToken());
					//String current = tokenizerArticle.nextToken();
					line=line+1;
					//System.out.println("*****************************"+line);

					String[] words = valueString.split("[^a-zA-Z]+");
					
					for(int i = 0;i<words.length;i++){
						//Otherwise it will counts the number of ""
						if(words[i].equals(""))
						{
							continue;
						}/*
						else if(words[i].startsWith("'")){
							if(words[i].startsWith("''")){
								output.collect(new Text(words[i].substring(2).toLowerCase()), new IntWritable(line));
							}else{
								output.collect(new Text(words[i].substring(1).toLowerCase()), new IntWritable(line));
							}
							
						}*/
						if(words[i].startsWith("a")){
							output.collect(new Text(words[i].toLowerCase()), new IntWritable(line));
						}
						
					}
					/*
					String[] current_words = valueString.split("[^a-zA-Z0-9][^a-zA-Z0-9']+");
	    			for(int i=0; i<current_words.length; i++){		
	    				if(current_words[i].equals(""))
	    					continue;	
	    				/*
	    				if (result.get(current_words[i].toLowerCase()) == null){
	    					result.put(current_words[i].toLowerCase(), 1);
	    				}else{
	    					tmp = current_words[i].toLowerCase();
	    					if(result.get(current_words[i]) == null) {result.put(tmp, 1);}
	    					else{
	    						a = result.get(current_words[i])+1;
	    						result.put(tmp, a);
	    					}
	    				}
	    				
	    				
	    				output.collect(new Text(current_words[i].toLowerCase()),one);
	    			
	    				
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

	public static class WordCountReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text>{

		
		public void reduce(Text t_key, Iterator<IntWritable> values,  OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			Text key = t_key;
			
			
			//因为values不是iterable类 所以不可以for(DoubleWritable val:values){ sum += val.get;}
			String sum = "";
			for(IntWritable val:(Iterable<IntWritable>)() -> values){ 
				sum = sum +" "+val.toString();
			}
			
			
			output.collect(key, new Text(sum));
		}

	}

	
	public static void main(String[] args){
		long startTime=System.currentTimeMillis(); 
		WordCountDriver.go(args);
		long endTime=System.currentTimeMillis(); 
	    System.out.println("Time used： "+(endTime-startTime)+"ms");  


	}

}
