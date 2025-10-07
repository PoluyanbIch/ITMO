package ru.poluyanblch.web.servlets;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String INVALID_DATA_MSG = "Please set the data values in correct form.";
        System.out.println("=== DEBUG ===");
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Query String: " + request.getQueryString());
        System.out.println("Parameter x: " + request.getParameter("x"));
        System.out.println("Parameter y: " + request.getParameter("y"));
        System.out.println("Parameter r: " + request.getParameter("r"));
        System.out.println("Parameter action: " + request.getParameter("action"));
        System.out.println("==============");

        try {
            if (
                    request.getParameter("r") == null
                            || request.getParameter("x") == null
                            || request.getParameter("y") == null
            ) {
                sendError(response, INVALID_DATA_MSG);
                return;
            }
            if (
                    request.getParameter("r").isEmpty()
                            || request.getParameter("x").isEmpty()
                            || request.getParameter("y").isEmpty()
            ) {
                sendError(response, INVALID_DATA_MSG);
                return;
            }
            if (
                    Double.parseDouble(request.getParameter("y")) < -5
                            || Double.parseDouble(request.getParameter("y")) > 5
            ) {
                sendError(response, INVALID_DATA_MSG);
                return;
            }

            Double.parseDouble(request.getParameter("x"));
            Integer.parseInt(request.getParameter("r"));

            response.sendRedirect("./checkArea?" + request.getQueryString());
            System.out.println("Запрос отправлен");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sendError(response, e.toString());
        }
    }

    private void sendError(HttpServletResponse response, String errorMessage) throws IOException {
        Gson json = new Gson();
        Map<String, Object> jsonResponse = new HashMap<String, Object>() {{
            put("error", errorMessage);
            put("status", "UNPROCESSABLE_ENTITY");
        }};

        response.setContentType("application/json");
        response.getWriter().write(json.toJson(jsonResponse));
        response.setStatus(422);
    }
}