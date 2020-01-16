package lcd.tp03;

import java.util.Vector;

import org.w3c.dom.Node;

class XPathInter extends XPathExpr {

	
	XPathInter () { super (2); }
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
			
			if (i1 == i2){ res.add(n1); i++; j++; }
			else if (i1 < i2) { i ++; }
			else { j++; };
			
		}
	
		return res;
	}
	@Override
	String getLabel() {
		// TODO Auto-generated method stub
		return "Inter";
	}

}
