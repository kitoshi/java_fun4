<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Employee Admin Tool</title>
    <style>
        .grid-container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .grid-item {
            padding: 10px;
            border: 1px solid #ccc;
            background-color: #f0f0f0;
        }
        .full-width {
            width: 100%; 
            background-color: #fff; 
            border-collapse: collapse; 
        }

    </style>
</head>
<body>

    <div class="grid-container">
        <div class="grid-item">
            <jsp:include page="Employees.jsp" />
        </div>
        <div class="grid-item">
            <jsp:include page="addEmployees.jsp" />
            <jsp:include page="findEmployees.jsp" />
            <jsp:include page="deleteEmployee.jsp" />
        </div>
    </div>
    <% if (request.getAttribute("tableError") != null) { %>
        <p style="color: red;"><%= request.getAttribute("tableError") %></p>
    <% } %>
</body>
</html>
