package lcd.tp03;

import java.util.Vector;

import org.w3c.dom.Node;

class XPathTag extends XPathExpr {

	String tag;

	XPathTag(String tag) {
		super(0);
		this.tag = tag;
	}

	@Override
	Vector<Node> eval() {

		Vector<Node> res = new Vector<>();

		for (Node n : this.dom) {
			if (this.tag.equals("node()"))
				res.add(n);
			else if (this.tag.equals("text()")
					&& (n.getNodeType() == Node.CDATA_SECTION_NODE || n.getNodeType() == Node.TEXT_NODE))
				res.add(n);
			else if (this.tag.equals("*") && (n.getNodeType() == Node.ELEMENT_NODE))
				res.add(n);
			else if (this.tag.equals(n.getNodeName()) && (n.getNodeType() == Node.ELEMENT_NODE))
				res.add(n);
		}

		return res;
	}

	@Override
	String getLabel() {
		// TODO Auto-generated method stub
		return "::" + tag;
	}

}
