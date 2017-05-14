package Search;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.fs.FSDataInputStream;;

public class SearchReadResult {
    public static boolean findFileIsExit(String query) throws IOException{
        String filepath = "hdfs://master:9000/"+ query;
        Configuration conf = new Configuration();  
        FileSystem fs = FileSystem.get(URI.create(filepath), conf);  
        boolean isExit=true;
        if (!fs.exists(new Path(filepath))) {  
            isExit=false;
        }  
        return isExit;
        
    }
	 public static ArrayList<String> ReadFile(String hdfs) throws IOException {
		  BufferedReader bufferedReader = null;
		  String lineTxt = null;
		  ArrayList <String> result = new ArrayList<String>();
		  
		  Configuration conf = new Configuration();
		  System.out.println(hdfs);

		  FileSystem fs = FileSystem.get(URI.create(hdfs),conf);
		  FSDataInputStream hdfsInStream = fs.open(new Path(hdfs));
		  
		  bufferedReader = new BufferedReader(new InputStreamReader(hdfsInStream));
		  while ((lineTxt = bufferedReader.readLine()) != null){
			  result.add(lineTxt);
			  //System.out.println(lineTxt);
		  }
		  
		  hdfsInStream.close();
		  fs.close(); 
		  return result;
	 	}
}