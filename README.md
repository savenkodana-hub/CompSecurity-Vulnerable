# Communication LTD Cyber Project - Vulnerable Part B Version

## Team Members

- Full Name: דנה סבנקו | ID: 323082115
- Full Name: ענבר רחנמי | ID: 318913670
- Full Name: רותם אלוני | ID: 213388028
- Full Name: חנה שמואל | ID: 214275703
- Full Name: איתי קריטמלר | ID: 322567264

This copy is the intentionally vulnerable Part B version of the Communication LTD coursework project.

WARNING: This version is for local educational demonstration only. Do not deploy it, expose it to a network, or use it with real data. Several required flows deliberately use unsafe SQL string concatenation and unsafe HTML rendering.

## How to run locally

1. Install Java 17 and Maven.
2. Copy local config templates:
   - `src/main/resources/mail-config.example.properties` to `src/main/resources/mail-config.properties`
   - `src/main/resources/security-config.example.properties` to `src/main/resources/security-config.properties`
3. Fill local values only in the non-example files. Do not commit real secrets.
4. Run:

```bash
mvn jetty:run
```

5. Open the app in the browser, usually:

```text
http://localhost:8080/
```

The database is initialized on startup. `/init-db` is also available if manual initialization is needed.

## Intentionally vulnerable locations

- Register SQLi: `src/main/java/com/communicationltd/dao/UserDao.java`, `registerUser`
- Login SQLi: `src/main/java/com/communicationltd/dao/UserDao.java`, `findUserByVulnerableLogin`, used by `src/main/java/com/communicationltd/controller/LoginServlet.java`
- Add Customer SQLi: `src/main/java/com/communicationltd/dao/CustomerDao.java`, `addCustomer`
- Add Customer / Dashboard Stored XSS: `src/main/java/com/communicationltd/controller/CustomerServlet.java`, `src/main/java/com/communicationltd/dao/CustomerDao.java`, and `src/main/webapp/dashboard.jsp`

All vulnerable code is marked with comments containing:

```text
INTENTIONALLY VULNERABLE FOR COURSEWORK DEMO.
```

## Local demo ideas

Register SQLi:

- Open `/register.jsp`.
- Enter normal-looking registration data first to create a user.
- Then try input containing a single quote in `username` or `email` to show that the concatenated SQL breaks or can be manipulated because the values are not parameterized.

Login SQLi:

- Make sure at least one user exists.
- Open `/login.jsp`.
- Use this username/email payload and any password:

```text
' OR 1=1) --
```

- The vulnerable login query treats the injected SQL match as authenticated.

Add Customer SQLi:

- Log in and open `/customer-details.jsp`.
- Enter this as the customer name, with any normal values in the other fields:

```text
Eve', '555-9999', 'Injected address', 1, 1) --
```

- The add-customer insert is built with string concatenation, so this payload changes the inserted phone/address/package/sector values instead of being handled as a plain customer name.

Stored XSS:

- Log in and open `/customer-details.jsp`.
- Enter this as the customer name:

```html
<script>alert('Stored XSS demo')</script>
```

- Submit the form.
- The payload is stored in the database and later rendered raw on `/dashboard.jsp`, so it executes when the dashboard displays the stored customer name.

## Main routes

- `/register` - creates a new user with username, email, and password.
- `/login` - authenticates a user, including the intentionally vulnerable SQLi demo path.
- `/customer-details` - adds customer details after login using intentionally vulnerable SQL.
- `/dashboard.jsp` - displays the logged-in user and intentionally renders customer name without HTML encoding for the Stored XSS demo.
- `/change-password` - changes password after verifying the current password.
- `/forgot-password` - starts password reset by email code.
- `/verify-code` - verifies the reset code.
- `/reset-password` - sets a new password after reset-code verification.
