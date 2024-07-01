<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <h3>Add Employee</h3>
    <form action="DatabaseServlet" method="post">
    	<input type="hidden" name="action" value="create">
    
        <label for="id">ID:</label>
        <input type="text" id="id" name="id" required><br><br>
        
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required><br><br>
        
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required><br><br>
        
        <label for="dob">Date of Birth:</label>
        <input type="date" id="dob" name="dob" placeholder="YYYY/MM/DD" required><br><br>
        
        <input type="submit" value="Add Employee">
    </form>
        <% if (request.getAttribute("addError") != null) { %>
        <p style="color: red;"><%= request.getAttribute("addError") %></p>
    <% } %>
