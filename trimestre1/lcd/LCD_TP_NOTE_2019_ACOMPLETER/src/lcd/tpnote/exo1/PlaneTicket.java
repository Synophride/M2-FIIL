package lcd.tpnote.exo1;
/***
 * Classe utilitaire permettant de stocker un billet, ne pas modifier.
 *
 */
public class PlaneTicket {
	public final int id;
	public final String city1;
	public final String city2;
	public final int price;
	public final int qtity;
	
	public PlaneTicket(int i, String c1, String c2, int p, int q) {
		id = i;
		city1 = c1;
		city2 = c2;
		price = p;
		qtity = q;
	}
	
	@Override
	public String toString() {
		return String.format("Départ: %s, Arrivée: %s, Prix: %d, Quantité: %d", city1, city2, price, qtity);
	}
}
