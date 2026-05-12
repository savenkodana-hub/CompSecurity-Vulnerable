<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Change Password</title>
</head>
<body>

<h1>Change Password</h1>

<form action="<%= request.getContextPath() %>/change-password" method="post">
    Current Password:<br>
    <input type="password" name="oldPassword" required><br><br>

    New Password:<br>
    <input type="password" name="newPassword" required><br><br>

    Confirm New Password:<br>
    <input type="password" name="confirmPassword" required><br><br>

    <button type="submit">Change Password</button>
</form>

</body>
</html>
