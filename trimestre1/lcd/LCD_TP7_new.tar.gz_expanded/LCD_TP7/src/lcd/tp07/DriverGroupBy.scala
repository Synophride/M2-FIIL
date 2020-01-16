package lcd.tp07

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// une classe singleton avec une méthode main

object DriverGroupBy {
  
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
                     
  val (x,y) = (1,2)
  
  movieList.groupBy(m => m.year)
           .map( r => r match { case (y, mlist) => (y,mlist.size) })
           .sortBy((r) => r._2)         
           .collect()
           .foreach((s) => println(s))
           
  }
    
}