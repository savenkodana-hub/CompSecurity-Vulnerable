<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page auth-page">

<main class="card">
    <div class="brand">
        <h1>Login</h1>
        <p class="brand-subtitle">Access the Communication LTD system</p>
    </div>

    <%
        String loginError = (String) request.getAttribute("loginError");
        String loginSuccess = (String) request.getAttribute("loginSuccess");
    %>

    <% if (loginError != null) { %>
        <p class="message message-error"><%= StringEscapeUtils.escapeHtml4(loginError) %></p>
    <% } %>

    <% if (loginSuccess != null) { %>
        <p class="message message-success"><%= StringEscapeUtils.escapeHtml4(loginSuccess) %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="form-group">
            <label for="usernameOrEmail">Username or Email</label>
            <input id="usernameOrEmail" type="text" name="usernameOrEmail" required>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input id="password" type="password" name="password" required>
        </div>

        <div class="button-row">
            <button class="btn" type="submit">Login</button>
            <a class="text-link" href="forgot-password.jsp">Forgot Password?</a>
        </div>
    </form>

    <p class="muted"><a href="register.jsp">Create a new account</a></p>
</main>

</body>
</html>
