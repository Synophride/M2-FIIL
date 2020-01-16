package lcd.tp07

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// une classe singleton avec une méthode main

object DriverWordCount {
  
  def main(args : Array[String]) : Unit = {

  /*
   * Création d'une configuration. Comme pour une configuration Hadoop on pourrait
   * y stocker des paramètres.
   */
  val conf = new SparkConf().setAppName("Word count")

  /*
   * Création du contexte spark
   */
  
  val sc = new SparkContext(conf)
  
  
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> c842505... tomate_bis
=======
>>>>>>> ee12a7a... bite
  val textFile = sc.textFile("hdfs://localhost:9090/user/jguyot2/input/jungle2.txt")  // Chargement du fichiers
  // Map : RDD<String> -> (String -> U) -> RDD<U>  
  // T : RDD <String[]>
  val lines = textFile.map((str : String) => str.split("[\n \\!\\?\\.\\-]"))
<<<<<<< HEAD
<<<<<<< HEAD
  
  val wordOnes = words.map((word) => (null, 1))
=======
  val textFile = sc.textFile("hdfs://localhost:9090/user/jguyot2/input/jungle2.txt")  
  
  val words = textFile.flatMap((line) => line.split("\\W+")) 
  val wordOnes = words.map((word) => (words, 1))
>>>>>>> 4e19d34... ajout vendredi
=======
  
  val wordOnes = words.map((word) => (null, 1))
>>>>>>> c842505... tomate_bis
=======
  
  val wordOnes = words.map((word) => (null, 1))
>>>>>>> ee12a7a... bite
  val count = wordOnes.reduceByKey((acc, value) => acc + value)
  count.saveAsTextFile("hdfs://localhost:9000/user/jguyot2/output")
    
  }
    
}