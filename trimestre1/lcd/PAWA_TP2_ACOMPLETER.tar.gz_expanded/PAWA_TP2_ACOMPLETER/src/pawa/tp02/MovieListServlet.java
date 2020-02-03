package pawa.tp02;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Cette annotation indique que la classe sera utilisée en réponse à une requête
 * faîte à l'URL ECS_TP8/MovieListServlet
 */
@WebServlet("/MovieListServlet")

public class MovieListServlet extends HttpServlet {

	private static final long serialVersionUID = 42L;
	MovieDB	mdb = null;
	
	/*
	 * Le comportement du servlet en cas de méthode Post.
	 * On exécute le même code que la méthode Get.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	 
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try 
		{
			MovieDB mdb =(MovieDB) request.getSession().getAttribute("db");  
			if(mdb == null) {
				mdb = new MovieDB();
				request.getSession().setAttribute("db", mdb);
			}
			
			String s_from = request.getParameter("from");
			String s_to = request.getParameter("to");
			int from = 0;
			
			if(s_from != null)
				from = Math.max(0, Integer.getInteger(s_from, from));
			
			int to = from + 10;
			
			if(s_to != null) 
				to = Math.min(mdb.getNumMovies(), Integer.getInteger(s_to, to));
			
			List<Pair<Integer, String>> movieList = mdb.getMovieList(from, to);
			RequestDispatcher rd = request.getRequestDispatcher("movie_list.jsp");
			request.setAttribute("movies", movieList);
			request.setAttribute("from", new Integer(from));
			request.setAttribute("to", new Integer(to));
			rd.forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}
