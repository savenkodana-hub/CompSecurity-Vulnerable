package com.communicationltd.controller;

import com.communicationltd.dao.CustomerDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer-details")
public class CustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("email");

        String customerName = request.getParameter("customerName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        int packageId = Integer.parseInt(request.getParameter("packageId"));
        int sectorId = Integer.parseInt(request.getParameter("sectorId"));

        CustomerDao.addCustomer(email, customerName, phone, address, packageId, sectorId);

        session.setAttribute("customerName", customerName);
        response.sendRedirect("dashboard.jsp");
    }
}
