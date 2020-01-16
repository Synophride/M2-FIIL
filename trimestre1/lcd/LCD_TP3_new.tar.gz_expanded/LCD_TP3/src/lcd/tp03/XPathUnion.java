package lcd.tp03;

import java.util.Vector;

import org.w3c.dom.Node;

class XPathUnion extends XPathExpr {

	XPathUnion() {
		super(2);
	}

	@Override
	Vector<Node> eval() {
		// A COMPLETER
		Vector<Node> set1 = arguments[0].eval();
		Vector<Node> set2 = arguments[1].eval();
		Vector<Node> res = new Vector<>();

		int i = 0;
		int j = 0;
		while (i < set1.size() && j < set2.size()) {
			Node n1 = set1.get(i);
			Node n2 = set2.get(j);
			int i1 = (Integer) n1.getUserData("preorder");
			int i2 = (Integer) n2.getUserData("preorder");

			if (i1 == i2) {
				res.add(n1);
				i++;
				j++;
			} else if (i1 < i2) {
				res.add(n1);
				i++;
			} else {
				res.add(n2);
				j++;
			}
			;

		}
		Vector<Node> todo;
		int start = 0;
		if (i < set1.size()) {
			// On est sorti car set2 était plus court, on ajoute tout ce qui
			// reste dans set1
			todo = set1;
			start = i;
		} else {
			// Sinon on rajoute tout ce qui reste dans set2:
			todo = set2;
			start = j;
		}

		for (int k = start; k < todo.size(); k++)
			res.add(todo.get(k));

		return res;

	}

	@Override
	String getLabel() {
		return "Union";
	}

}
