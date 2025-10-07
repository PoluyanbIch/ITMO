package ru.poluyanblch.web.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.poluyanblch.web.models.Point;

import java.util.ArrayList;
import java.util.List;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        List<Point> results = new ArrayList<>();

        ctx.setAttribute("results", results);
    }
}