<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<body>
<h1>Verify Code</h1>

<form action="<%= request.getContextPath() %>/verify-code" method="post">
    Email:<br>
    <input type="email" name="email" required><br><br>

    Code from email:<br>
    <input type="text" name="code" required><br><br>

    <button type="submit">Verify Code</button>
</form>

</body>
</html>
