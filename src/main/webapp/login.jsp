<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<h1>Login</h1>

<form action="<%= request.getContextPath() %>/login" method="post">
    Username or Email:<br>
    <input type="text" name="usernameOrEmail" required><br><br>

    Password:<br>
    <input type="password" name="password" required><br><br>

    <button type="submit">Login</button>
</form>

<br>
<a href="forgot-password.jsp">Forgot Password?</a>

</body>
</html>
