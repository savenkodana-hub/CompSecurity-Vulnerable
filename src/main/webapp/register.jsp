<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>

<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page auth-page">

<main class="card">
    <div class="brand">
        <h1>Register</h1>
        <p class="brand-subtitle">Create a secure system user</p>
    </div>

    <%
        String registerError = (String) request.getAttribute("registerError");
    %>

    <% if (registerError != null) { %>
        <p class="message message-error"><%= StringEscapeUtils.escapeHtml4(registerError) %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/register" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input id="username" type="text" name="username" required>
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input id="email" type="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input id="password" type="password" name="password" required>
        </div>

        <div class="button-row">
            <button class="btn" type="submit">Register</button>
            <a class="btn btn-secondary" href="index.jsp">Back</a>
        </div>
    </form>

</main>

</body>
</html>
