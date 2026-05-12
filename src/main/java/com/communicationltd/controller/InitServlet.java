package com.communicationltd.controller;

import com.communicationltd.util.DatabaseInitializer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/init-db", loadOnStartup = 1)
public class InitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        DatabaseInitializer.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        DatabaseInitializer.init();
        response.getWriter().println("Database initialized successfully!");
    }
}
