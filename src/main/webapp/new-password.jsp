<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<html>
<head>
    <title>New Password</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page auth-page">

<main class="card">
    <div class="brand">
        <h1>New Password</h1>
        <p class="brand-subtitle">Choose a new password for your account</p>
    </div>

    <%
        String resetPasswordError = (String) request.getAttribute("resetPasswordError");
    %>

    <% if (resetPasswordError != null) { %>
        <p class="message message-error"><%= StringEscapeUtils.escapeHtml4(resetPasswordError) %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/reset-password" method="post">
        <div class="form-group">
            <label for="newPassword">New Password</label>
            <input id="newPassword" type="password" name="newPassword" required>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <input id="confirmPassword" type="password" name="confirmPassword" required>
        </div>

        <div class="button-row">
            <button class="btn" type="submit">Change Password</button>
        </div>
    </form>
</main>

</body>
</html>
