package lcd.tpnote.exo3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

class MapperOneStop
extends Mapper<Object, Text, Text, Text> {

	@Override
	public void map(Object o, Text in, Context ctx) 
			throws IOException, InterruptedException
	{
		//À COMPLÉTER
		
		
	}
	
}

class ReducerOneStop
extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text ville1, Iterable<Text> others,
						Context ctx) 
	throws IOException, InterruptedException {
		
		//À COMPLÉTER
		
			
	}
	
}
	
public class DriverOneStop {

	
	
	public static void main(String[] args)
	
	{
		if (args.length < 2) System.exit(2);
		
		
		try {
			
			//Création d'une nouvelle configuration
			Configuration conf = new Configuration();

			Job job = Job.getInstance(conf, "lcd.tpnote.exo3");
		    
					
			
			//Le nom de la classe 'main'
			job.setJarByClass(DriverOneStop.class);

			//Le format d'entrée du Map
			//TextInputFormat est un alias pour
			//InputFormat<LongWritable, Text>;
			
			job.setInputFormatClass(TextInputFormat.class);
			
			//Initialisation du Map
			job.setMapperClass(MapperOneStop.class);
			
			//Initialisation du Reduce
			job.setReducerClass(ReducerOneStop.class);

			//Sortie du Map
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			
			//Sortie (du Reduce)
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
						
			FileOutputFormat.setOutputPath(job, new Path(args[0]));
			
			for(int i = 1; i < args.length; i++)
				FileInputFormat.addInputPath(job, new Path(args[i]));

						
			
			System.exit(job.waitForCompletion(true) ?  0 : 1);
			
		} catch (Exception e) {
			System.err.println("Erreur : " + e);
		}
		
	}
	
}
