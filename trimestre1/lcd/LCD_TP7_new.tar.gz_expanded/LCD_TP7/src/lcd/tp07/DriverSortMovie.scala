package lcd.tp07

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// une classe singleton avec une méthode main

object DriverSortMovie {
  
  def main(args : Array[String]) : Unit = {

  /*
   * Création d'une configuration. Comme pour une configuration Hadoop on pourrait
   * y stocker des paramètres.
   */
    
  val conf = new SparkConf().setAppName("Display movie")

  /*
   * Création du contexte spark
   */
  
  val sc = new SparkContext(conf)
  
  
  val textFile = sc.textFile("hdfs://localhost:9000/user/kn/input/movie.txt")  
  
  val stringList = textFile.map(line => line.split(';'))
  val movieList = stringList.map(fields => new Movie (fields(0),
                                                      fields(1),
                                                      fields(2),
                                                      fields(3),
                                                      fields(4)))
                                                      
  movieList.filter(m => m.year >= 1990 && m.year <= 2000)
           .sortBy(m => m.year, false)
           .collect()
           .foreach(m => println(m))
           
           
  }
    
}