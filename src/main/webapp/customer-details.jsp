<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customer Details</title>
</head>
<body>

<h1>Add Customer</h1>

<form action="<%= request.getContextPath() %>/customer-details" method="post">
    Customer Name:<br>
    <input type="text" name="customerName" required><br><br>

    Phone:<br>
    <input type="text" name="phone" required><br><br>

    Address:<br>
    <input type="text" name="address" required><br><br>

    Internet Package:<br>
    <select name="packageId" required>
        <option value="1">Basic - 100MB - 99</option>
        <option value="2">Premium - 500MB - 149</option>
        <option value="3">Ultra - 1GB - 199</option>
    </select><br><br>

    Sector:<br>
    <select name="sectorId" required>
        <option value="1">Private</option>
        <option value="2">Business</option>
        <option value="3">Education</option>
        <option value="4">Government</option>
    </select><br><br>

    <button type="submit">Add Customer</button>
</form>

</body>
</html>
