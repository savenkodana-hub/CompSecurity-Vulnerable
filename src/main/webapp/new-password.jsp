<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<body>
<h1>New Password</h1>

<form action="<%= request.getContextPath() %>/reset-password" method="post">
    New Password:<br>
    <input type="password" name="newPassword" required><br><br>

    Confirm Password:<br>
    <input type="password" name="confirmPassword" required><br><br>

    <button type="submit">Change Password</button>
</form>

</body>
</html>
