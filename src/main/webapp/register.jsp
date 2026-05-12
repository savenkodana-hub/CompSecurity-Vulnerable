<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Register</title>
</head>
<body>

<h1>Register</h1>

<form action="<%= request.getContextPath() %>/register" method="post">

    Username:<br>
    <input type="text" name="username" required><br><br>

    Email:<br>
    <input type="email" name="email" required><br><br>

    Password:<br>
    <input type="password" name="password" required><br><br>

    <button type="submit">Register</button>

</form>

<br>
<a href="index.jsp">Back</a>

</body>
</html>
