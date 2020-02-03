package pawa.tpnote;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//La classe du contrôleur
@WebServlet("/GoServlet")
public class GoServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * QUESTION 1.2.2
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			String hash_str = request.getParameter("h");
			if (null == hash_str || "null".equals(hash_str)) {
				response.sendRedirect("index.jsp");
				return;
			}
			int hash = 0;
			try {
				hash = Integer.parseInt(hash_str);
			} catch (NumberFormatException e){	
				response.sendRedirect("index.jsp");
				return;
			}
			URLDB urldb = URLDB.getFromSession(request.getSession());
			URLInfo info = urldb.getURLbyHash(hash);
			if(info == null) {
				response.sendRedirect("index.jsp");
				return;
			}
			response.sendRedirect(info.getUrl());

		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}
