package pawa.tp03;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter(urlPatterns = { "/*" }) 
public class LoginFilter implements Filter {
	static String[] UNFILTERED_PATTERNS = { 
			"login.jsp",
			"CheckServlet",
			"MovieWSServlet"
			};

	@Override
	public void doFilter(ServletRequest req_, ServletResponse resp_, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) req_;
		HttpServletResponse resp = (HttpServletResponse) resp_;
		
		String status = (String) req.getSession().getAttribute("status");
		boolean unfiltered = false;
		for(String pattern : UNFILTERED_PATTERNS)
			if(("/PAWA_TP3_ACOMPLETER/" + pattern).equals(req.getRequestURI())) {
				unfiltered = true;
				break;
			}
		if("connected".equals(status) || unfiltered){			
			chain.doFilter(req, resp);
		} else {
			resp.sendRedirect("login.jsp");
		}
		return;
	}

}
