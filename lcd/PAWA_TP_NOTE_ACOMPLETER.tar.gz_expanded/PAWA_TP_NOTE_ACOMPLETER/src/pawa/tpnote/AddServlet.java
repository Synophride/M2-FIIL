package pawa.tpnote;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//La classe du contrôleur
@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	
	/**
	 * QUESTION 1.5
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String url = request.getParameter("url");
			String format = request.getParameter("format");
			
			if("json".equals(format)) {
				JsonObject j;
				if(url == null || ! (url.startsWith("http://") 
								    || url.startsWith("https://") )) {
					j = Json.createObjectBuilder()
						.add("status", "error").build();
					
				} else {
					URLDB urldb = URLDB.getFromSession(request.getSession());
					URLInfo info = urldb.insertURL(url);
					JsonObject json_info = info.asJSON();	
					j = Json.createObjectBuilder()
							.add("status", "ok")
							.add("json", json_info).build();
				}
				request.setAttribute("json", j);
				RequestDispatcher rd= request.getRequestDispatcher("/WEB-INF/jsp/json.jsp");
				rd.forward(request, response);
				
			} else {
				if(url == null)
					throw new Exception("cas null");
				if(!(url.startsWith("http://") || url.startsWith("https://"))) {
					response.sendRedirect("index.jsp");
					return;
				}
				URLDB urldb = URLDB.getFromSession(request.getSession());
				URLInfo info = urldb.insertURL(url);
				request.setAttribute("info", info);
				RequestDispatcher rd =
					request.getRequestDispatcher("/WEB-INF/jsp/add_view.jsp");
				rd.forward(request, response);
			}
			
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}
