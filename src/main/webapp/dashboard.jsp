<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>

<%
    String username = (String) session.getAttribute("username");
    String customerName = (String) session.getAttribute("customerName");
    String passwordChanged = (String) session.getAttribute("passwordChanged");
%>

<h1>WELCOME <%= username %></h1>

<% if (customerName != null) { %>
    <h2>Customer <%= customerName %> added successfully</h2>
<% } %>

<% if (passwordChanged != null) { %>
    <h3><%= passwordChanged %></h3>
<%
    session.removeAttribute("passwordChanged");
}
%>

<br>
<a href="change-password.jsp">Change Password</a>

<br><br>
<a href="index.jsp">Logout</a>

</body>
</html>