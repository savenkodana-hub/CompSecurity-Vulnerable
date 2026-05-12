package com.communicationltd.controller;

import com.communicationltd.dao.UserDao;
import com.communicationltd.dao.CustomerDao;
import com.communicationltd.model.User;
import com.communicationltd.config.PasswordPolicyConfig;
import com.communicationltd.security.PasswordHasher;

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
            User user = UserDao.findUserByUsernameOrEmail(usernameOrEmail);

            if (user != null) {
                if (user.isLocked()) {
                    showLoginError(request, response, "Account is locked after too many failed login attempts");
                    return;
                }

                String storedHash = user.getPasswordHash();
                String salt = user.getSalt();

                String enteredHash = PasswordHasher.hash(password, salt);

                if (storedHash.equals(enteredHash)) {
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

                    return;
                }

                UserDao.recordFailedLogin(user.getEmail(), PasswordPolicyConfig.getMaxLoginAttempts());
                showLoginError(request, response, "Username and/or password incorrect");
                return;
            }

            showLoginError(request, response, "Username and/or password incorrect");

        } catch (Exception e) {
            e.printStackTrace();
            showLoginError(request, response, "Login error");
        }
    }

    private void showLoginError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {

        request.setAttribute("loginError", message);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
