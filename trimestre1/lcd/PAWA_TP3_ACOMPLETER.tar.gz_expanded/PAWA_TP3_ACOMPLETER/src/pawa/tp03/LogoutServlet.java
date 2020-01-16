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
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.getSession().invalidate();
			response.sendRedirect("login.jsp");
		} catch (Exception e) { request.getSession().invalidate();
			response.sendRedirect("login.jsp");
			throw new ServletException(e); }
	}


}

