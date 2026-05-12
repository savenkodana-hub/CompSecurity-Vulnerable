<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customer Details</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>
<body class="page auth-page">

<main class="card wide-card">
    <div class="brand">
        <h1>Add Customer</h1>
        <p class="brand-subtitle">Save customer details and select a package</p>
    </div>

    <form action="<%= request.getContextPath() %>/customer-details" method="post">
        <div class="form-group">
            <label for="customerName">Customer Name</label>
            <input id="customerName" type="text" name="customerName" required>
        </div>

        <div class="form-group">
            <label for="phone">Phone</label>
            <input id="phone" type="text" name="phone" required>
        </div>

        <div class="form-group">
            <label for="address">Address</label>
            <input id="address" type="text" name="address" required>
        </div>

        <div class="form-group">
            <label for="packageId">Internet Package</label>
            <select id="packageId" name="packageId" required>
                <option value="1">Basic - 100MB - 99</option>
                <option value="2">Premium - 500MB - 149</option>
                <option value="3">Ultra - 1GB - 199</option>
            </select>
        </div>

        <div class="form-group">
            <label for="sectorId">Sector</label>
            <select id="sectorId" name="sectorId" required>
                <option value="1">Private</option>
                <option value="2">Business</option>
                <option value="3">Education</option>
                <option value="4">Government</option>
            </select>
        </div>

        <div class="button-row">
            <button class="btn" type="submit">Add Customer</button>
        </div>
    </form>
</main>

</body>
</html>
