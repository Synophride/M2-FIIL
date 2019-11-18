package lcd.tp06;

import java.io.IOException;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD

import javax.naming.Context;

=======
>>>>>>> cdb284a... ENUM DE MERDE
=======

import javax.naming.Context;

>>>>>>> c842505... tomate_bis
=======

import javax.naming.Context;

>>>>>>> ee12a7a... bite
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

class SubstringCountMapper
extends Mapper<Object, Text, Text, NullWritable> {

	@Override
	public void map(Object o, Text in, Context ctx) 
			throws IOException, InterruptedException
	{
	// Object = paire ? 
		String line = in.toString();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		for(String word : line.split("[.,;\"()!.? ]")){
			ctx.write(new Text(word.toLowerCase()), NullWritable.get());
		}
	}
=======
		for(String word : line.split("[.,; ]")){
			ctx.write(new Text(word), NullWritable.get());
		}
	}
	
>>>>>>> cdb284a... ENUM DE MERDE
=======
		for(String word : line.split("[.,;\"()!.? ]")){
			ctx.write(new Text(word.toLowerCase()), NullWritable.get());
		}
	}
>>>>>>> c842505... tomate_bis
=======
		for(String word : line.split("[.,;\"()!.? ]")){
			ctx.write(new Text(word.toLowerCase()), NullWritable.get());
		}
	}
>>>>>>> ee12a7a... bite
}

class SubstringCountReducer
extends Reducer<Text, NullWritable, Text, IntWritable> {

	@Override
	public void reduce(Text str, Iterable<NullWritable> nulls,
						Context ctx) 
	throws IOException, InterruptedException {
		int res = 0;
		for (Object i : nulls) {
			res ++;
		}
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		
		ctx.write(str, new IntWritable(res));	
	}
}
	
public class DriverWordCount {
=======
=======
		
		ctx.write(str, new IntWritable(res));	
>>>>>>> c842505... tomate_bis
	}
}
	
public class DriverWordCount {
<<<<<<< HEAD

	
>>>>>>> cdb284a... ENUM DE MERDE
=======
>>>>>>> c842505... tomate_bis
=======
		
		ctx.write(str, new IntWritable(res));	
	}
}
	
public class DriverWordCount {
>>>>>>> ee12a7a... bite
	
	public static void main(String[] args)
	{
		if (args.length < 2) System.exit(2);
		
		try {	
			//Création d'une nouvelle configuration
			Configuration conf = new Configuration();

			Job job = Job.getInstance(conf, "lcd.tp06");
		    
					
			
			//Le nom de la classe 'main'
			job.setJarByClass(DriverWordCount.class);

			//Le format d'entrée du Map
			//TextInputFormat est un alias pour
			//InputFormat<LongWritable, Text>;
			
			job.setInputFormatClass(TextInputFormat.class);
			
			//Initialisation du Map
			job.setMapperClass(SubstringCountMapper.class);
			
			//Initialisation du Reduce
			job.setReducerClass(SubstringCountReducer.class);

			//Sortie du Map
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(NullWritable.class);
			
			//Sortie (du Reduce)
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
						
			FileOutputFormat.setOutputPath(job, new Path(args[0]));
			
			for(int i = 1; i < args.length; i++)
				FileInputFormat.addInputPath(job, new Path(args[i]));

			
						
			
			System.exit(job.waitForCompletion(true) ?  0 : 1);
			
		} catch (Exception e) {
			System.err.println("Erreur : " + e);
		}
		
	}
	
}
