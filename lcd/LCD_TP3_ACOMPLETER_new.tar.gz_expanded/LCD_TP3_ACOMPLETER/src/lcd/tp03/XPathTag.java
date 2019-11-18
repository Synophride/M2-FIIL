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


		/******
		 * A COMPLETER Renvoyer un Vector<Node> contenant tous les nœuds (dans l'ordre du document)
		 * tels que :
		 *  - si tag = "node()" renvoyer le nœud
		 *  - si tag = "text()" renvoyer le nœud si son type (.getNodeType()) vaut Node.CDATA_SECTION_NODE
		 *                      ou Node.TEXT_NODE
		 *  - si tag = "*" renvoyer le nœud si son type est Node.ELEMENT_NODE
		 *  - si tag = "foo" renvoyer le nœud si son nom (.getNodeName()) vaut 'foo'
		 */
		Vector<Node> res = null;//à initialiser ou remplacer.
		return res;
	}

	@Override
	String getLabel() {
		// TODO Auto-generated method stub
		return "::" + tag;
	}

}
