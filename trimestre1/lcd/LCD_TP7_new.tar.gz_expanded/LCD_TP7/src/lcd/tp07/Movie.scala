
package lcd.tp07 {
 
class Movie (id_s : String, title_s : String, year_s : String, runtime_s : String, rank_s : String)
extends java.io.Serializable
{
  val id = id_s.toInt
  val title = title_s
  val year = year_s.toInt
  val runtime = runtime_s.toInt
  val rank = rank_s.toInt
  
  override def toString () =
    title + " (" + year + ")"
}


}