package lcd.tpnote.exo4

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// une classe singleton avec une méthode main

object DriverLess2000 {
  
  def main(args : Array[String]) : Unit = {

  /*
   * Création d'une configuration. Comme pour une configuration Hadoop on pourrait
   * y stocker des paramètres.
   */
    
  val conf = new SparkConf().setAppName("Less2000")

  /*
   * Création du contexte spark
   */
  
  val sc = new SparkContext(conf)
  
  val user = java.lang.System.getProperty("user.name");
  
  val textFile = sc.textFile("hdfs://localhost:9000/user/"+ user +"/input/capitals_distances.txt")  
  
  
  
  // À COMPLÉTER
  
    
  }
    
}