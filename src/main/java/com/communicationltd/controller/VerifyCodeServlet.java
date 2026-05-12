package com.communicationltd.controller;

import com.communicationltd.util.DatabaseConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/verify-code")
public class VerifyCodeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");
        String code = request.getParameter("code");

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = """
                    SELECT token_hash
                    FROM password_reset_tokens
                    WHERE email = ? AND used = 0
                    ORDER BY id DESC
                    LIMIT 1
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (!rs.next() || !rs.getString("token_hash").equals(code)) {
                response.getWriter().println("Invalid code");
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("resetEmail", email);

            response.sendRedirect("new-password.jsp");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}