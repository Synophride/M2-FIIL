package pawa.tpnote;

import java.io.IOException;
import java.net.URL;

import javax.json.Json;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//La classe du contrôleur
@WebServlet("/RemoveServlet")
public class RemoveServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	/**
	 * QUESTION 1.3.1
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String str_hash = request.getParameter("hash");
			String str_pass = request.getParameter("pass");
			
			if("format".equals(request.getAttribute("format"))) {
				// JSon
				
			} else {
				
			}
	
			
		} catch (Exception e) { throw new ServletException(e); }
		
		
	}


}

