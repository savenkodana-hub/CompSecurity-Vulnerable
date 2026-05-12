<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<html>
<head>
    <title>Forgot Password</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page auth-page">

<main class="card">
    <div class="brand">
        <h1>Forgot Password</h1>
        <p class="brand-subtitle">Receive a reset code by email</p>
    </div>

    <%
        String forgotPasswordError = (String) request.getAttribute("forgotPasswordError");
    %>

    <% if (forgotPasswordError != null) { %>
        <p class="message message-error"><%= StringEscapeUtils.escapeHtml4(forgotPasswordError) %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/forgot-password" method="post">
        <div class="form-group">
            <label for="email">Email</label>
            <input id="email" type="email" name="email" required>
        </div>

        <div class="button-row">
            <button class="btn" type="submit">Send Code</button>
            <a class="btn btn-secondary" href="login.jsp">Back to Login</a>
        </div>
    </form>
</main>

</body>
</html>
