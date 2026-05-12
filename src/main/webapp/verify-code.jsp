<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<html>
<head>
    <title>Verify Code</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page auth-page">

<main class="card">
    <div class="brand">
        <h1>Verify Code</h1>
        <p class="brand-subtitle">Enter the reset code sent to your email</p>
    </div>

    <%
        String verifyCodeError = (String) request.getAttribute("verifyCodeError");
    %>

    <% if (verifyCodeError != null) { %>
        <p class="message message-error"><%= StringEscapeUtils.escapeHtml4(verifyCodeError) %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/verify-code" method="post">
        <div class="form-group">
            <label for="email">Email</label>
            <input id="email" type="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="code">Code from email</label>
            <input id="code" type="text" name="code" required>
        </div>

        <div class="button-row">
            <button class="btn" type="submit">Verify Code</button>
            <a class="btn btn-secondary" href="forgot-password.jsp">Back</a>
        </div>
    </form>
</main>

</body>
</html>
