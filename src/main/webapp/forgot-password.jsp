<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<body>
<h1>Forgot Password</h1>

<form action="<%= request.getContextPath() %>/forgot-password" method="post">
    Email:<br>
    <input type="email" name="email" required><br><br>
    <button type="submit">Send Code</button>
</form>

</body>
</html>
