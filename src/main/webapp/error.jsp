<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .error-container {
            background-color: #ffe6e6;
            padding: 20px;
            border: 1px solid #ff9999;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #cc0000;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h2>Error</h2>
        <p>${message}</p>
        <p><a href="${pageContext.request.contextPath}/DatabaseServlet">Go back</a></p>
    </div>
</body>
</html>
