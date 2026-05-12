package com.communicationltd.controller;

import com.communicationltd.dao.UserDao;
import com.communicationltd.dao.CustomerDao;
import com.communicationltd.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");

        try {
            // INTENTIONALLY VULNERABLE FOR COURSEWORK DEMO.
            // Part A section 3 / Part B demo: authentication trusts the concatenated SQL query result.
            User user = UserDao.findUserByVulnerableLogin(usernameOrEmail, password);

            if (user != null) {
                completeLogin(request, response, user);
                return;
            }

            showLoginError(request, response, "Username and/or password incorrect");

        } catch (Exception e) {
            e.printStackTrace();
            showLoginError(request, response, "Login error");
        }
    }

    private void completeLogin(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException, ServletException {

        if (user.isLocked()) {
            showLoginError(request, response, "Account is locked after too many failed login attempts");
            return;
        }

        UserDao.resetLoginAttempts(user.getEmail());

        HttpSession session = request.getSession();

        session.setAttribute("username", user.getUsername());
        session.setAttribute("email", user.getEmail());

        String userEmail = user.getEmail();

        if (!CustomerDao.hasCustomer(userEmail)) {
            session.removeAttribute("customerName");
            response.sendRedirect("customer-details.jsp");
        } else {
            String customerName = CustomerDao.getCustomerFirstName(userEmail);
            session.setAttribute("customerName", customerName);
            response.sendRedirect("dashboard.jsp");
        }
    }

    private void showLoginError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {

        request.setAttribute("loginError", message);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
