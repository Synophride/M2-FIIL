package lcd.tp03;

import java.util.Vector;

import org.w3c.dom.Node;

class XPathDiff extends XPathExpr {

	
	XPathDiff () { super(2); };
	@Override
	Vector<Node> eval() {
		Vector<Node> set1 = arguments[0].eval();
		Vector<Node> set2 = arguments[1].eval();
		Vector<Node> res = new Vector<>();
		
		int i = 0;
		int j = 0;
		while (i < set1.size() && j < set2.size()) {
			Node n1 = set1.get(i);
			int i1 = (Integer) n1.getUserData("preorder");
			int i2 = (Integer) set2.get(j).getUserData("preorder");
			
			if (i1 == i2){ i++; j++; }
			else if (i1 < i2) {
				res.add(n1);
				i ++;
			} else { j++; };
			
		}
		for (int k = i; k < set1.size(); k ++)
			res.add(set1.get(k));
	
		return res;
	}

	@Override
	String getLabel() {
		return "Diff";
	}

}
