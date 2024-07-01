package ca.bcit.comp3601.assign2.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Database;
import model.Employee;

/**
 * Servlet implementation class DatabaseServlet
 */
@WebServlet("/DatabaseServlet")
public class DatabaseServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;
	private Database database;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DatabaseServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		List<Employee> employees;
		try {
			database = (Database) context.getAttribute("database");
			employees = database.getAllEmployees();
			context.setAttribute("employees", employees);
		} catch (SQLException e) {
			request.setAttribute("tableError", "Error retrieving employees: " + e.getMessage());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		ServletContext context = getServletContext();
		database = (Database) context.getAttribute("database");

		switch (action) {
		case "find":
			String employeeId = request.getParameter("id");
			if (isValidIdFormat(employeeId)) {
				handleFind(request, response);
			} else {
				request.setAttribute("findError", "Invalid ID format");
			}
			break;
		case "create":
			handleCreate(request, response);
			break;
		case "delete":
			handleDelete(request, response);
			break;
		default:
			response.sendRedirect(request.getContextPath() + "/error");
			break;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}

	private void handleFind(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String employeeId = request.getParameter("id");
		Employee employee;
		try {
			employee = database.findEmployeeById(employeeId);
			if (employee != null) {
				request.setAttribute("employee", employee);
				request.setAttribute("statusCode", HttpServletResponse.SC_OK);
				request.setAttribute("statusText", "OK");
			} else {
				request.setAttribute("message", "Employee not found");
				request.setAttribute("statusCode", HttpServletResponse.SC_NOT_FOUND);
				request.setAttribute("statusText", "Not Found");
			}
		} catch (SQLException e) {
			request.setAttribute("findError", "Error finding employee: " + e.getMessage());
			request.setAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			request.setAttribute("statusText", "Internal Server Error");
		}

	}

	private void handleCreate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String dobStr = request.getParameter("dob");
		Employee newEmployee = new Employee();
		Date dob = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dob = dateFormat.parse(dobStr);
		} catch (ParseException e) {
			request.setAttribute("createError", "Error parsing date of birth: " + e.getMessage());
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}
		newEmployee.setId(id);
		newEmployee.setFirstName(firstName);
		newEmployee.setLastName(lastName);
		newEmployee.setDob(dob);

		boolean success;
		try {
			success = database.createEmployee(newEmployee);
			if (success) {
				request.setAttribute("employee", newEmployee);
				request.setAttribute("statusCode", HttpServletResponse.SC_OK);
				request.setAttribute("statusText", "OK");
			}
		} catch (SQLException e) {
			request.setAttribute("findError", "Error finding employee: " + e.getMessage());
			request.setAttribute("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			request.setAttribute("statusText", "Internal Server Error");
		}

	}

	private void handleDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String employeeId = request.getParameter("employeeId");
		boolean success;
		try {
			success = database.deleteEmployee(employeeId);
			if (success) {
				request.setAttribute("message", "Deleted employee");
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				request.setAttribute("message", "Employee not found");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				request.setAttribute("statusText", "Internal Server Error");
				response.getWriter().write("Error finding employee.");
			}
		} catch (SQLException e) {
			request.setAttribute("deleting", "Error deleting employee: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			request.setAttribute("statusText", "Internal Server Error");
			response.getWriter().write("Error deleting employee:" + e);
		}

	}

	private boolean isValidIdFormat(String id) {
		// Regular expression for the specified format
		String regex = "^A0\\d{7}$";
		return id.matches(regex);
	}

}
