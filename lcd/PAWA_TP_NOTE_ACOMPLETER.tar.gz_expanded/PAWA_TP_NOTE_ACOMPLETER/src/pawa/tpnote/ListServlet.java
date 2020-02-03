package pawa.tpnote;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//La classe du contrôleur
@WebServlet("/ListServlet")
public class ListServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * QUESTION 1.4.2
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			/* COMPLÉTER CI-DESSOUS */
			
		
		
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}
