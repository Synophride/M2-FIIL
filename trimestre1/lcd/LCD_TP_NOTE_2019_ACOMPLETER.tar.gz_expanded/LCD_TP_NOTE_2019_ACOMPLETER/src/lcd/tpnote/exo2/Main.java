package lcd.tpnote.exo2;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Main {

	
	public static void main(String args[]) {
		
		String xpath = question09();        // REMPLACER par question01, question02, ... pour tester.
		evalXPath(xpath); 					// ne pas modifier.
		
	}
	
	public static String question00() {
		//Exemple, ne rapporte pas de point.
		return "//produit[ nom = 'Chai' ]";
	}
	
	
	public static String question01() {
		return "//produit/nom";
	}
	
	public static String question02() {
		return "//produit [child::presentation/conditionnement = 'bouteille' ]/nom";
		
	}
	// Rue contient le nom
	public static String question03() {
		// TODO
		return "//fournisseur[ contains(child::nom, child::addresse/rue)]";
	}	
	
	public static String question04() {
		return "//produit[child::prix>100]/nom";
	}
	
	public static String question05() {
		return "//fournisseur[child::adresse/pays = 'France']/nom";
	}
	
	public static String question06() {
		return "//produit[not(@categorie='boisson') and child::presentation/conditionnement = 'bouteille']/nom";
	}
	
	public static String question07() {
		return "//*";
	}
	
	public static String question08() {
		return "//*";
	}
	
	public static String question09() {
		//return "//fournisseur[ancestor::catalogue//produit/@categorie = 'viande']/nom";
		return "//*";
	}
	
	public static String question10() {
		return "//*";
	}
	
	
	
	
	
	/*
	 * MÉTHODE UTILITAIRE, NE PAS MODIFIER
	 */
	
	static void evalXPath(String xpath) {
		
		try {
			
			String url =  Main.class.getClassLoader().getResource("resources/produits.xml").toExternalForm();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url);
		
			XPath exec = XPathFactory.newInstance().newXPath();
			
			NodeList results = (NodeList) exec.evaluate(xpath, doc, XPathConstants.NODESET);
		
			Document output = db.newDocument();
			Element root = output.createElement("output");
			output.appendChild(root);
			for(int i = 0; i < results.getLength(); i++) {
				root.appendChild(output.importNode(results.item(i), true));
				if (i != results.getLength() - 1) root.appendChild(output.createTextNode("\n---\n"));
			}
			root.setAttribute("count", results.getLength() + "");
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer tr = tf.newTransformer();
			tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			tr.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			tr.setOutputProperty(OutputKeys.INDENT, "yes");
			tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			tr.transform(new DOMSource(output), new StreamResult(System.out));
			
			
			
		} catch (XPathExpressionException e) {
			System.err.println("Erreur dans l'expression XPath '" + xpath + "'");
			System.err.println(e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
