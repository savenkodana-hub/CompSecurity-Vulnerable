package com.communicationltd.controller;

import com.communicationltd.dao.UserDao;
import com.communicationltd.security.PasswordHasher;
import com.communicationltd.security.PasswordValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String validation = PasswordValidator.validate(password);
        if (validation != null) {
            response.getWriter().println(validation);
            return;
        }

        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hash(password, salt);

        boolean registered = UserDao.registerUser(username, email, hashedPassword, salt);

        if (!registered) {
            response.getWriter().println("Username or email already exists");
            return;
        }

        response.sendRedirect("login.jsp");
    }
}
