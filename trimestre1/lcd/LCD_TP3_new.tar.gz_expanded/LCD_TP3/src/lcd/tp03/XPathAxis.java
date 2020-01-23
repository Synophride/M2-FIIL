package lcd.tp03;

import java.util.BitSet;
import java.util.Vector;
import java.util.function.Function;

import org.w3c.dom.Node;

class Moves {

	static Node self(Node n) {
		return n;
	}

	static Node firstChild(Node n) {
		return n.getFirstChild();
	}

	static Node nextSibling(Node n) {
		return n.getNextSibling();
	}

	// le mouvement firstchild-1 du cours
	static Node firstParent(Node n) {
		return (n.getPreviousSibling() == null) ? n.getParentNode() : null;

	}

	// le mouvement nextSibling-1 du cours
	static Node previousSibling(Node n) {
		return n.getPreviousSibling();
	}

}

class XPathAxis extends XPathExpr {
	private String axis;

	XPathAxis(String axis) {
		super(1);
		this.axis = axis;
	}

	private Vector<Node> bitSetToVector(BitSet bits) {
		Vector<Node> res = new Vector<>();
		for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
			// itère le vecteur de 1 en 1
			res.add(this.dom.get(i));
		}
		return res;
	}

	/*
	 * Methode auxiliaire qui applique un mouvement à un ensemble de Noeuds. On
	 * doit utiliser un bitvector pour garantir le temps O(|D|) car pour un
	 * mouvement quelconque on n'a pas la garantie que l'image préserve l'ordre
	 * du document. Par exemple pour un arbre: 
	 *     a
	 *    / \ 
	 *   b   e 
	 *  / \
	 * c   d
	 * Pour l'ensemble initial { d, e } (qui est correctement ordonné), fair previousSibling
	 * donne {c, b } qui n'est plus dans l'ordre du document.
	 */

	private Vector<Node> evalMove(Vector<Node> set, Function<Node, Node> f) {
		
		BitSet bits = new BitSet(this.dom.size());

		for (Node n : set) {
			Node m = f.apply(n);
			if (m != null) {
				bits.set((Integer) m.getUserData("preorder"));
			}
		}
		return bitSetToVector(bits);
	}

	/*
	 * Méthode auxiliaire qui calcule la cloture transitive de l'union d'un
	 * ensemble de mouvement (implémente le pseudo code du cours, slide 18). On
	 * prend les mouvements sous forme d'un tableau de fonctions. Comme
	 * précédemment on utilise un bitSet pour s'assurer que l'on renvoie les
	 * nœuds dans l'ordre du document.
	 */

	private Vector<Node> starMoves(Vector<Node> set, Vector<Function<Node, Node>> moves) {
		BitSet bits = new BitSet(this.dom.size());
		Vector<Node> temp = new Vector<>(); // contient les résultats non
											// ordonnés.

		// S' = S
		for (Node n : set) {
			bits.set((Integer) n.getUserData("preorder"));
			temp.add(n);
		}

		int i = 0;
		while (i < temp.size()) {
			Node x = temp.get(i);
			for (Function<Node, Node> f : moves) {
				Node moved_x = f.apply(x);
				if (moved_x != null && !bits.get((Integer) moved_x.getUserData("preorder"))) {
					bits.set((Integer) moved_x.getUserData("preorder"));
					temp.add(moved_x);
				}
			}
			i++;
		}

		return bitSetToVector(bits);
	}

	@Override
	Vector<Node> eval() {
		Vector<Node> arg = this.arguments[0].eval();
		Vector<Function<Node, Node>> moves = new Vector<>();
		Vector<Node> res = null;
		switch (this.axis) {
		case "self":
			res = arg;
			break;
			
		case "child":
			moves.add(Moves::nextSibling);
			res = starMoves(evalMove(arg, Moves::firstChild), moves);
			break;
			
		case "parent":
			moves.add(Moves::previousSibling);
			res = evalMove(starMoves(arg, moves), Moves::firstParent);
			break;
			
		case "descendant":
			moves.add(Moves::firstChild);
			moves.add(Moves::nextSibling);
			res = starMoves(evalMove(arg, Moves::firstChild), moves);
			break;
			
		case "ancestor":
			moves.add(Moves::previousSibling);
			moves.add(Moves::firstParent);
			res = evalMove(starMoves(arg, moves), Moves::firstParent);
			break;
			
		default:
			;
		}
		
		return res;
	}

	@Override
	String getLabel() {
		return axis;
	}

}
