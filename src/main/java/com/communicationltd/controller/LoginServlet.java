package com.communicationltd.controller;

import com.communicationltd.dao.UserDao;
import com.communicationltd.dao.CustomerDao;
import com.communicationltd.model.User;
import com.communicationltd.security.PasswordHasher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");

        try {
            User user = UserDao.findUserByUsernameOrEmail(usernameOrEmail);

            if (user != null) {
                String storedHash = user.getPasswordHash();
                String salt = user.getSalt();

                String enteredHash = PasswordHasher.hash(password, salt);

                if (storedHash.equals(enteredHash)) {
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

                response.getWriter().println("Incorrect password");
                return;
            }

            response.getWriter().println("User does not exist");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login error");
        }
    }
}
