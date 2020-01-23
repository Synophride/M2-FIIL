
package lcd.tp07 {
 
class Person (id_s : String, firstname_s : String, lastname_s : String) extends java.io.Serializable
{
  val id = id_s.toInt
  val lastname = lastname_s
  val firstname = firstname_s
  override def toString () = 
    firstname + " " + lastname
    


}

}