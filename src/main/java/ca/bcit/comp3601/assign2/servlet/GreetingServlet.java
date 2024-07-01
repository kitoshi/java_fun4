package ca.bcit.comp3601.assign2.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Database;

@WebServlet("/A01285019_assign2/")
public class GreetingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Database database;
	Properties dbProperties = new Properties();

	public GreetingServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = config.getServletContext();

		String driver = context.getInitParameter("db.driver");
		String url = context.getInitParameter("db.url");
		String user = context.getInitParameter("db.user");
		String password = context.getInitParameter("db.password");
		String dbName = context.getInitParameter("db.name");

		dbProperties.setProperty("db.driver", driver);
		dbProperties.setProperty("db.url", url);
		dbProperties.setProperty("db.user", user);
		dbProperties.setProperty("db.password", password);
		dbProperties.setProperty("db.name", dbName);

		try {
			database = new Database(dbProperties);
		} catch (IOException e) {
			throw new ServletException("Failed to initialize database", e);
		}

		context.setAttribute("greeting", context.getInitParameter("greeting"));
		context.setAttribute("instruction", context.getInitParameter("instruction"));
		context.setAttribute("database", database);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher dispatcher = request.getRequestDispatcher("/DatabaseServlet");
		dispatcher.include(request, response);
		dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);

	}

}
