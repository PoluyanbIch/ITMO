<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.poluyanblch.web.models.Point" %>
<%
    List<Point> results = (List<Point>) application.getAttribute("results");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Web Lab 1</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
    <script defer src="${pageContext.request.contextPath}/scripts/index.js"></script>
</head>
<body>
<nav class="navbar">
    <div id="info">
        <span class="name">Poluyanov Igor</span>
        <span class="group">Group: P3210</span>
        <span class="variant">Variant: <span class="fire">467139</span></span>
    </div>
    <a href="https://github.com/poluyanbIch" target="_blank" id="github" class="github-link">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
        </svg>
        GitHub
    </a>
</nav>
<main class="container">
    <section class="input-section">
        <h2 class="section-title">Point Coordinates</h2>
        <form action="${pageContext.request.contextPath}/controller" method="GET" id="data-form">
            <input type="hidden" name="action" value="submitForm">
            <input type="hidden" id="x" name="x" value="">

            <div class="input-group">
                <label class="input-label">X Coordinate</label>
                <div class="checkbox-group">
                    <%
                        double[] xValues = {-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2};
                        for (double xVal : xValues) {
                    %>
                    <label class="checkbox-label">
                        <input type="checkbox" value="<%= xVal %>" class="x-checkbox">
                        <span class="checkbox-text"><%= xVal %></span>
                    </label>
                    <% } %>
                </div>
                <div class="input-hint">Select one value between -2 and 2</div>
            </div>

            <div class="input-group">
                <label for="y" class="input-label">Y Coordinate</label>
                <input type="text" id="y" name="y" class="text-input" placeholder="-5 ... 5" required>
                <div class="input-hint">Value between -5 and 5</div>
            </div>

            <div class="input-group">
                <label class="input-label">R Parameter</label>
                <div class="button-group">
                    <%
                        double[] rValues = {1, 2, 3, 4, 5};
                        for (double rVal : rValues) {
                    %>
                    <button type="button" class="r-button" value="<%= rVal %>"><%= rVal %></button>
                    <% } %>
                </div>
                <input type="hidden" id="r" name="r" value="">
                <div class="input-hint">Select one value between 1 and 5</div>
            </div>

            <button type="submit" class="submit-button" disabled>Check Point</button>
        </form>
        <div id="error" class="error-message" hidden></div>
    </section>
    <section class="results-section">
        <h2 class="section-title">Results</h2>
        <div class="canvas-container">
            <canvas id="graph" width="300" height="300"></canvas>
        </div>
        <div class="table-container">
            <table id="result-table" class="results-table">
                <thead>
                <tr>
                    <th>x</th>
                    <th>y</th>
                    <th>r</th>
                    <th>result</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (results != null) {
                        for (int i = results.size() - 1 ; i >= 0 ; i--) {
                            Point point = results.get(i);
                %>
                <tr>
                    <td><%= point.getX() %></td>
                    <td><%= point.getY() %></td>
                    <td><%= point.getR() %></td>
                    <td class="<%= point.isInArea() ? "hit" : "miss" %>">
                        <%= point.isInArea() ? "hit" : "miss" %>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </section>
</main>
</body>
</html>