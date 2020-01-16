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
  
  
  val textFile = sc.textFile("hdfs://localhost:9000/user/kn/input/jungle2.txt")  
  
  val wordList = textFile.flatMap(line => line.split("[ \t.;:,``'()-]+"))
  
  val mappedWordList = wordList.map(word => (word.toLowerCase(), 1))
  
  
  val reducedList = mappedWordList.reduceByKey((x,y) => x+y)
  
  
  reducedList.saveAsTextFile("hdfs://localhost:9000/user/kn/output/");
    
  }
    
}