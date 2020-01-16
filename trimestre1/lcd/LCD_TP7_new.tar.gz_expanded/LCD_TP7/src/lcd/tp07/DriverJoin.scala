package lcd.tp07

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

// une classe singleton avec une méthode main

object DriverJoin {
  
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
  
  
  val movieFile = sc.textFile("hdfs://localhost:9000/user/kn/input/movie.txt")  
  val roleFile = sc.textFile("hdfs://localhost:9000/user/kn/input/role.txt")  
  val peopleFile = sc.textFile("hdfs://localhost:9000/user/kn/input/people.txt")
  
  /*
   * On crée une liste de paires (id, Movie)
   * La fonction utilise le pattern-matching sur les tableaux:
   *  - si le split renvoie un tableau d'exactement 5 cases, on crée un Movie avec ces cases
   *    et on le place dans une liste
   *  - sinon on renvoie la liste vide.
   *  En utilisant flatMap, on a une liste de film et pas une liste de listes de films (autrement
   *  dit, on utilise des listes à 0 ou 1 éléments comme des Options).
   * 
   */
  
  val movieList = movieFile.flatMap(line => line.split(";") match {
    case Array(id, title, year, runtime, rank) =>
        val m = new Movie(id,title,year,runtime,rank)
          List((m.id, m))
    case _ => Nil
  })

  
  /*
   * On crée une liste de paires (mid, Role) où mid est l'id du film associé au rôle
   */
  
  val roleList = roleFile.flatMap (line => line.split(";") match {
    case Array(mid, pid, role) => 
         val r = new Role(mid,pid,role)
          List((r.mid,r))
    case _ => Nil
  })
  
  /*
   *  On calcule la jointure : le  résultat de join est un RDD 
   *  de paires (mid, (Movie,Role)) où le film le role ont le même mid
   *  on applique un map sur ce RDD pour obtenir un Rdd de paires:
   *  (pid, (Movie, Role)) où pid est le pid de la personne qui joue le role.
   */
  val movieRoles = movieList.join(roleList)
                            .map( line =>
                                   line match { case (mid,(movie,role)) =>
                                                 (role.pid,(movie,role)) })
  
  /*
   * On crée la liste de paires (id, Person)
   */
    
  val personList = peopleFile.flatMap(line => line.split(";") match {
    case Array(pid, first,last) => 
      val p = new Person (pid, first, last)
      List ((p.id, p))
    case _ => Nil
  })
    
   /*
    * On effectue la jointure finale par pid et on trie par titre croissant.
    * Sur un tuple scala, ._1 permet de prendre la première composante, ._2 la deuxième etc…
    */
                                                    
  val movieRolePerson = movieRoles.join(personList).sortBy(line => line._2._1._1.title)
  
  movieRolePerson.collect().foreach( line => {
    val (_,((m, r), p)) = line
    println("Dans " + m.title + " le role de " + r + " est joué par " + p)
  })
  
  
   
  }
  
    
}