
package lcd.tp07 {
 
class Role (mid_s : String, pid_s : String, role_s : String) extends java.io.Serializable
{
  val mid = mid_s.toInt
  val pid = pid_s.toInt
  val role = role_s
  
  override def toString () = role
    


}
}