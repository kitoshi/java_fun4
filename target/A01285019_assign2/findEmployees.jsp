    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
    
    <h3>Find an Employee By ID</h3>
    <form action="DatabaseServlet" method="POST">
        <input type="hidden" name="action" value="find">
        <label for="searchQuery">Enter employee ID:</label>
        <input type="text" id="id" name="id" required>
        <input type="submit">Search</button>
    </form>

    <% if (request.getAttribute("findError") != null) { %>
        <p style="color: red;"><%= request.getAttribute("findError") %></p>
    <% } %>

    <% if (request.getAttribute("employee") != null) { %>
        <h3>Employee Details</h3>
        <ul>
            <li>ID: ${employee.id}</li>
            <li>First Name: ${employee.firstName}</li>
            <li>Last Name: ${employee.lastName}</li>
            <li>Date of Birth: ${employee.dob}</li>
        </ul>
    <% } %>

    <% if (request.getAttribute("message") != null) { %>
        <p><%= request.getAttribute("message") %></p>
    <% } %>
    <% if (request.getAttribute("statusCode") != null) { %>
        <p>Status Code: <%= request.getAttribute("statusCode") %></p>
    <% } %>
    <% if (request.getAttribute("statusText") != null) { %>
        <p>Status Description: <%=   request.getAttribute("statusText") %></p>
    <% } %>

