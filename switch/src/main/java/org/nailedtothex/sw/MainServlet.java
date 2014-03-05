package org.nailedtothex.sw;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Properties envSpecific = new Properties();
		try (InputStream is = MainServlet.class.getResourceAsStream("/env-specific.properties")) {
			envSpecific.load(is);
		}

		Properties common = new Properties();
		try (InputStream is = MainServlet.class.getResourceAsStream("/common.properties")) {
			common.load(is);
		}

		request.setAttribute("envSpecific", envSpecific.getProperty("value"));
		request.setAttribute("common", common.getProperty("value"));
		
		request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
	}
}
