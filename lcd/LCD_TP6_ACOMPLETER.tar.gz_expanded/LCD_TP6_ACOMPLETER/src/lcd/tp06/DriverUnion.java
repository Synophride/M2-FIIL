package lcd.tp06;

import java.io.IOException;
import java.util.HashSet;

import javax.naming.Context;

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

class SubstringUnionMapper extends Mapper<Object, Text, Text, IntWritable> {
	@Override
	public void map(Object o, Text in, Context ctx) throws IOException, InterruptedException {
		// Object = paire ?
		HashSet<Integer> m = new HashSet<>();

		String line = in.toString();
		for (String word : line.split(" ")) {
			if (word == null) {
				System.out.println("Mot nul rencontré");
				continue;
			}
			Integer i = Integer.valueOf(word);
			if (i == null) {
				System.out.println("getinteger pourri srencontré");
				continue;
			}

			if (m.contains(i))
				continue;
			m.add(i);

			ctx.write(in, new IntWritable(i));
		}
	}
}

class SubstringUnionReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	public void reduce(Text str, Iterable<IntWritable> ints, Context ctx) throws IOException, InterruptedException {
		HashSet<Integer> m = new HashSet<>();
		for (IntWritable iw : ints) {
			int i = iw.get();
			if (m.contains(i))
				continue;

			m.add(i);
			ctx.write(str, iw);
		}

	}
}

public class DriverUnion {

	public static void main(String[] args) {
		if (args.length < 2)
			System.exit(2);

		try {
			// Création d'une nouvelle configuration
			Configuration conf = new Configuration();

			Job job = Job.getInstance(conf, "lcd.tp06");

			// Le nom de la classe 'main'
			job.setJarByClass(DriverUnion.class);

			// Le format d'entrée du Map
			// TextInputFormat est un alias pour
			// InputFormat<LongWritable, Text>;

			job.setInputFormatClass(TextInputFormat.class);

			// Initialisation du Map
			job.setMapperClass(SubstringUnionMapper.class);

			// Initialisation du Reduce
			job.setReducerClass(SubstringUnionReducer.class);

			// Sortie du Map
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);

			// Sortie (du Reduce)
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);

			FileOutputFormat.setOutputPath(job, new Path(args[0]));

			for (int i = 1; i < args.length; i++)
				FileInputFormat.addInputPath(job, new Path(args[i]));

			System.exit(job.waitForCompletion(true) ? 0 : 1);

		} catch (Exception e) {
			System.err.println("Erreur : " + e);
		}

	}

}
