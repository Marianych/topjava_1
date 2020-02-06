package ru.javawebinar.topjava.web;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//request.getRequestDispatcher("/users.jsp").forward(request,response);
        response.sendRedirect("users.jsp");
    }
}
