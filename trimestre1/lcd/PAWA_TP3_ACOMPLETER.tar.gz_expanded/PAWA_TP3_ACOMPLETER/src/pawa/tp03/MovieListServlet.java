package pawa.tp03;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//La classe du contrôleur

@WebServlet("/MovieListServlet")
public class MovieListServlet extends HttpServlet {

	private static final long serialVersionUID = 6489069438791307255L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String froms = request.getParameter("from");
		String tos = request.getParameter("to");

		int from = 0;
		try {
			from = Integer.parseInt(froms);
		} catch (NumberFormatException e) {
		}
		
		int to = from + 10;

		try {
			to = Integer.parseInt(tos);
		} catch (NumberFormatException e) {
		}

		// On récupère une connection à la base :
		try {

			MovieDB db = (MovieDB) request.getSession().getAttribute("db");
			if (db == null) {

				db = new MovieDB();
				request.getSession().setAttribute("db", db);

			}
			
			// On s'assure que from et to ont des valeurs raisonnables.
			to = Integer.min(to,db.getNumMovies());
			from = Integer.max(from,  0);

			RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/movie_list.jsp");
			request.setAttribute("movies", db.getMovieList(from, to));
			request.setAttribute("to", new Integer(to));
			request.setAttribute("from", new Integer(from));
			rd.forward(request, response);
			
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}