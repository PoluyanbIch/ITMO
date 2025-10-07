package ru.poluyanblch.web.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import ru.poluyanblch.web.exceptions.ValidationException;

import com.google.gson.Gson;
import ru.poluyanblch.web.models.Point;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/checkArea")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== AREA CHECK ===");
        System.out.println("Parameters received in checkArea:");
        System.out.println("x: " + request.getParameter("x"));
        System.out.println("y: " + request.getParameter("y"));
        System.out.println("r: " + request.getParameter("r"));

        try {
            String x_check = request.getParameter("x");
            String y_check = request.getParameter("y");
            String r_check = request.getParameter("r");
            validate(x_check, y_check, r_check);

            double x = Double.parseDouble(request.getParameter("x"));
            double y = Double.parseDouble(request.getParameter("y"));
            int r = Integer.parseInt(request.getParameter("r"));
            Point point = new Point(x, y, r);

            ServletContext ctx = getServletContext();
            List<Point> results = (List<Point>) ctx.getAttribute("results");
            results.add(point);
            ctx.setAttribute("results", results);


            String action = request.getParameter("action");
            if ("submitForm".equals(action)) {
                request.setAttribute("x", x);
                request.setAttribute("y", y);
                request.setAttribute("r", r);
                request.setAttribute("result", point.isInArea());

                jakarta.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("./result.jsp");
                dispatcher.forward(request, response);

            } else if ("checkPoint".equals(action)) {
                Gson gson = new Gson();
                Map<String, Object> json = new HashMap<>();
                json.put("x", x);
                json.put("y", y);
                json.put("r", r);
                json.put("result", point.isInArea());
                String msg = gson.toJson(json);

                response.setContentType("application/json");
                response.getWriter().write(msg);
            }
        }
        catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("./error.jsp").forward(request, response);
        }
        catch (Exception e) {
            request.getRequestDispatcher("./index.jsp").forward(request, response);
        }
    }

    private void validate(String x, String y, String r) throws ValidationException{
        if (x == null || x.isEmpty()) {
            throw new ValidationException("x is empty");
        }
        try {
            double x_parse =  Double.parseDouble(x);
            if (x_parse < -5 || x_parse > 5) {
                throw new ValidationException("x parse must be between -5 and 5");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("x is not a number");
        }

        if (y == null || y.isEmpty()) {
            throw new ValidationException("y is empty");
        }
        try {
            double y_parse =  Double.parseDouble(y);
            if (y_parse < -5 || y_parse > 5) {
                throw new ValidationException("y parse must be between -5 and 5");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("y is not a number");
        }

        if (r == null || r.isEmpty()) {
            throw new ValidationException("r is empty");
        }
        try {
            double r_parse =  Double.parseDouble(r);
            if (r_parse < 0 || r_parse > 5) {
                throw new ValidationException("r is not a number");
            }
        }  catch (NumberFormatException e) {
            throw new ValidationException("r is not a number");
        }
    }
}