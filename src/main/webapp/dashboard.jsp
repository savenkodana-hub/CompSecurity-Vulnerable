<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page">

<%
    String username = (String) session.getAttribute("username");
    String customerName = (String) session.getAttribute("customerName");
    String passwordChanged = (String) session.getAttribute("passwordChanged");
%>

<main class="app-shell">
    <header class="topbar">
        <div>
            <h1>WELCOME <%= StringEscapeUtils.escapeHtml4(username) %></h1>
            <p class="muted">Communication LTD system dashboard</p>
        </div>
        <nav class="nav-actions">
            <a class="btn btn-secondary" href="change-password.jsp">Change Password</a>
            <a class="btn" href="index.jsp">Logout</a>
        </nav>
    </header>

    <section class="dashboard-grid">
        <div class="panel">
            <h2>Account</h2>
            <p class="muted">You are signed in as <strong><%= StringEscapeUtils.escapeHtml4(username) %></strong>.</p>
        </div>

        <div class="panel">
            <h2>Customer Status</h2>
            <% if (customerName != null) { %>
                <p class="message message-success">Customer <strong><%= StringEscapeUtils.escapeHtml4(customerName) %></strong> added successfully.</p>
            <% } else { %>
                <p class="muted">No customer was added in this session.</p>
            <% } %>
        </div>
    </section>

<% if (passwordChanged != null) { %>
    <p class="message message-success"><%= StringEscapeUtils.escapeHtml4(passwordChanged) %></p>
<%
    session.removeAttribute("passwordChanged");
}
%>
</main>

</body>
</html>
