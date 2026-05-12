<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<html>
<head>
    <title>Change Password</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page auth-page">

<main class="card">
    <div class="brand">
        <h1>Change Password</h1>
        <p class="brand-subtitle">Update your password using the current one</p>
    </div>

    <%
        String changePasswordError = (String) request.getAttribute("changePasswordError");
    %>

    <% if (changePasswordError != null) { %>
        <p class="message message-error"><%= StringEscapeUtils.escapeHtml4(changePasswordError) %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/change-password" method="post">
        <div class="form-group">
            <label for="oldPassword">Current Password</label>
            <input id="oldPassword" type="password" name="oldPassword" required>
        </div>

        <div class="form-group">
            <label for="newPassword">New Password</label>
            <input id="newPassword" type="password" name="newPassword" required>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Confirm New Password</label>
            <input id="confirmPassword" type="password" name="confirmPassword" required>
        </div>

        <div class="button-row">
            <button class="btn" type="submit">Change Password</button>
            <a class="btn btn-secondary" href="dashboard.jsp">Back to Dashboard</a>
        </div>
    </form>
</main>

</body>
</html>
