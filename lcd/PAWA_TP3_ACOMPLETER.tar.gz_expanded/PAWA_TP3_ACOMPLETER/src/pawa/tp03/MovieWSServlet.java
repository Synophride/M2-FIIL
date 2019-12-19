package pawa.tp03;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//La classe du contrôleur
@WebServlet("/MovieWSServlet")
public class MovieWSServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Integer from = Integer.getInteger(request.getParameter("from"), 0);
			Integer to = Integer.getInteger(request.getParameter("to"), from + 10 );
			// TODO 
			
			
			
		} catch (Exception e) { throw new ServletException(e); }
		
	}


}

