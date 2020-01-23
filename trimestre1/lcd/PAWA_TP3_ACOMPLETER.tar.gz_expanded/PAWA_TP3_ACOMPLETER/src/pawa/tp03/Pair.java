package pawa.tp03;


//Classe utilitaire permettant de stocker deux
//résultats.

public class Pair<X,Y> {

	private final X first;
	private final Y second;
	
	Pair(X x, Y y)
	{
		this.first = x;
		this.second = y;
	}
	//deux getter publiques, qui sont appelés dans la vue.
	public X getFirst() { return first; }
	public Y getSecond() { return second; }
	
}