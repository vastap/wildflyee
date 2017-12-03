package com.github.vastap.wildflyee;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Message")
public class MessageServlet extends HttpServlet {

    @Inject
    MessageService messageService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<html><head><title>Message Service</title></head><body>");
        writer.println("<h1>" + messageService.createMessage("World") + "</h1>");
        writer.println("</body></html>");
        writer.close();
    }

}
