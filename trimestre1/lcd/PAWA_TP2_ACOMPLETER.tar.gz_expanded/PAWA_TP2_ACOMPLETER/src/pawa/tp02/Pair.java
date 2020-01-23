package pawa.tp02;

public class Pair<X,Y> {

	private final X first;
	private final Y second;
	
	Pair(X x, Y y)
	{
		this.first = x;
		this.second = y;
	}
	
	public X getFirst() { return first; }
	public Y getSecond() { return second; }
	
}