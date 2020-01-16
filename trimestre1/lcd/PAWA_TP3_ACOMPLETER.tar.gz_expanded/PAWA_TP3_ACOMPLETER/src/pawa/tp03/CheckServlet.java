package pawa.tp03;

import java.io.IOException;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//La classe du contrôleur
@WebServlet("/CheckServlet")
public class CheckServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String user = request.getParameter("user");
			String pass = request.getParameter("pass");
			
			if("michel".equals(user) && "banane48".equals(pass)) {
				request.getSession().setAttribute("status", "connected");
				response.sendRedirect("MovieListServlet");
				
			}
		} catch (Exception e) { 
			response.sendRedirect("login.jsp");
		}
	}


}

