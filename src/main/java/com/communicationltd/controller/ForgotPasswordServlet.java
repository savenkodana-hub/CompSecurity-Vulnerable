package com.communicationltd.controller;

import com.communicationltd.security.TokenGenerator;
import com.communicationltd.dao.UserDao;
import com.communicationltd.util.DatabaseConnection;
import com.communicationltd.util.EmailSender;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");

        if (!UserDao.emailExists(email)) {
            response.getWriter().println("User does not exist");
            return;
        }

        String token = TokenGenerator.generateToken(email);

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "INSERT INTO password_reset_tokens(email, token_hash, used) VALUES (?, ?, 0)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, token);
            ps.executeUpdate();

            EmailSender.sendResetCode(email, token);

            response.sendRedirect("verify-code.jsp");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
