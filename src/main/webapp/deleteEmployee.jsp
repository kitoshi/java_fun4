<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<h3>Remove an Employee</h3>
<form action="DatabaseServlet" method="POST">
		<input type="hidden" name="action" value="delete">
        <label for="employeeId">Employee ID:</label>
        <input type="text" id="employeeId" name="employeeId" required>
        <button type="submit">Remove</button>
    </form>
        <% if (request.getAttribute("deleteError") != null) { %>
        <p style="color: red;"><%= request.getAttribute("deleteError") %></p>
    <% } %>