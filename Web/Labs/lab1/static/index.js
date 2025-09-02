"use strict";

const state = {
    x: 0,
    y: 0,
    r: 1.0,
};

const table = document.getElementById("result-table");
const error = document.getElementById("error");
const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");

// Draw initial graph
drawGraph();

// Add event listeners with improved validation
document.getElementById("x").addEventListener("change", (ev) => {
    const value = parseFloat(ev.target.value);
    if (isNaN(value) || value < -3 || value > 3) {
        showError("X must be a number between -3 and 3");
        ev.target.value = state.x;
    } else {
        state.x = value;
        error.hidden = true;
    }
});

document.getElementById("y").addEventListener("change", (ev) => {
    const value = parseFloat(ev.target.value);
    if (isNaN(value) || value < -5 || value > 3) {
        showError("Y must be a number between -5 and 3");
        ev.target.value = state.y;
    } else {
        state.y = value;
        error.hidden = true;
    }
});

document.getElementById("r").addEventListener("change", (ev) => {
    const value = parseFloat(ev.target.value);
    if (isNaN(value) || value < 1 || value > 4) {
        showError("R must be a number between 1 and 4");
        ev.target.value = state.r;
    } else {
        state.r = value;
        error.hidden = true;
    }
});

const validateState = (state) => {
    error.hidden = true;

    if (isNaN(state.x) || state.x < -3 || state.x > 3) {
        showError("X must be a number between -3 and 3");
        return false;
    }

    if (isNaN(state.y) || state.y < -5 || state.y > 3) {
        showError("Y must be a number between -5 and 3");
        return false;
    }

    if (isNaN(state.r) || state.r < 1 || state.r > 4) {
        showError("R must be a number between 1 and 4");
        return false;
    }

    return true;
}

function showError(message) {
    error.textContent = message;
    error.hidden = false;
}

document.getElementById("data-form").addEventListener("submit", async function (ev) {
    ev.preventDefault();

    if (!validateState(state)) return;

    const newRow = table.insertRow(-1);

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    const rowTime = newRow.insertCell(3);
    const rowExecTime = newRow.insertCell(4);
    const rowResult = newRow.insertCell(5);

    const params = new URLSearchParams();
    params.append('x', state.x);
    params.append('y', state.y);
    params.append('r', state.r);

    try {
        // ОТПРАВКА ЗАПРОСА НА СЕРВЕР
        const response = await fetch("/fcgi-bin/app.jar?" + params.toString());

        const results = {
            x: state.x,
            y: state.y,
            r: state.r,
            execTime: "",
            time: "",
            result: "",
        };

        if (response.ok) {
            const result = await response.json();
            results.time = new Date(result.now).toLocaleString();
            results.execTime = `${result.time} ns`;
            results.result = result.result.toString();
        } else if (response.status === 400) {
            const result = await response.json();
            results.time = new Date(result.now).toLocaleString();
            results.execTime = "N/A";
            results.result = `error: ${result.reason}`;

            rowResult.classList.add("error");
        } else {
            results.time = "N/A";
            results.execTime = "N/A";
            results.result = "server error";

            rowResult.classList.add("error");
        }

        const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
        localStorage.setItem("results", JSON.stringify([...prevResults, results]));

        rowX.innerText = results.x.toString();
        rowY.innerText = results.y.toString();
        rowR.innerText = results.r.toString();
        rowTime.innerText = results.time;
        rowExecTime.innerText = results.execTime;
        rowResult.innerText = results.result;

        // Add class for hit/miss styling based on server response
        if (results.result === "true") {
            rowResult.innerText = "hit";
            rowResult.classList.add("hit");
        } else if (results.result === "false") {
            rowResult.innerText = "miss";
            rowResult.classList.add("miss");
        }

    } catch (error) {
        console.error("Fetch error:", error);

        const results = {
            x: state.x,
            y: state.y,
            r: state.r,
            execTime: "N/A",
            time: "N/A",
            result: "connection error"
        };

        const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
        localStorage.setItem("results", JSON.stringify([...prevResults, results]));

        rowX.innerText = results.x.toString();
        rowY.innerText = results.y.toString();
        rowR.innerText = results.r.toString();
        rowTime.innerText = results.time;
        rowExecTime.innerText = results.execTime;
        rowResult.innerText = results.result;
        rowResult.classList.add("error");
    }
});

// Draw graph function
function drawGraph() {
    const width = canvas.width;
    const height = canvas.height;
    const r = state.r;
    const halfR = r / 2;

    // Clear canvas
    ctx.clearRect(0, 0, width, height);

    // Draw background
    ctx.fillStyle = "#2a2a2a";
    ctx.fillRect(0, 0, width, height);

    // Draw axes
    ctx.beginPath();
    ctx.strokeStyle = "#e1e1e1";
    ctx.lineWidth = 2;

    // Y axis
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2, height);

    // X axis
    ctx.moveTo(0, height/2);
    ctx.lineTo(width, height/2);

    // Arrow for Y axis
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2 - 5, 10);
    ctx.moveTo(width/2, 0);
    ctx.lineTo(width/2 + 5, 10);

    // Arrow for X axis
    ctx.moveTo(width, height/2);
    ctx.lineTo(width - 10, height/2 - 5);
    ctx.moveTo(width, height/2);
    ctx.lineTo(width - 10, height/2 + 5);

    ctx.stroke();

    // Draw labels
    ctx.fillStyle = "#e1e1e1";
    ctx.font = "12px monospace";

    // X axis labels
    ctx.fillText(`-R`, width/2 - 100, height/2 + 15);
    ctx.fillText(`-R/2`, width/2 - 50, height/2 + 15);
    ctx.fillText(`R/2`, width/2 + 50, height/2 + 15);
    ctx.fillText(`R`, width/2 + 100, height/2 + 15);
    ctx.fillText("x", width - 10, height/2 - 10);

    // Y axis labels
    ctx.fillText(`R`, width/2 + 5, height/2 - 100);
    ctx.fillText(`R/2`, width/2 + 5, height/2 - 50);
    ctx.fillText(`-R/2`, width/2 + 5, height/2 + 50);
    ctx.fillText(`-R`, width/2 + 5, height/2 + 100);
    ctx.fillText("y", width/2 + 10, 10);

    // Draw shape with the correct configuration
    ctx.fillStyle = "rgba(255, 105, 97, 0.5)";

    // 1st quadrant: Triangle from (0,0) to (R/2, 0) to (0, R/2)
    ctx.beginPath();
    ctx.moveTo(width/2, height/2); // (0,0)
    ctx.lineTo(width/2 + 50, height/2); // (R/2, 0)
    ctx.lineTo(width/2, height/2 - 50); // (0, R/2)
    ctx.closePath();
    ctx.fill();

    // 2nd quadrant: Sector (quarter circle) with radius R/2
    ctx.beginPath();
    ctx.moveTo(width/2, height/2); // center
    ctx.arc(width/2, height/2, 50, Math.PI, 3 * Math.PI / 2, false); // quarter circle
    ctx.lineTo(width/2, height/2); // back to center
    ctx.closePath();
    ctx.fill();

    // 3rd quadrant: Empty (nothing to draw)

    // 4th quadrant: Square from (0,0) to (-R, 0) to (-R, -R) to (0, -R)
    ctx.beginPath();
    ctx.moveTo(width/2, height/2); // (0,0)
    ctx.lineTo(width/2 + 100, height/2); // (-R, 0)
    ctx.lineTo(width/2 + 100, height/2 + 100); // (-R, -R)
    ctx.lineTo(width/2, height/2 + 100); // (0, -R)
    ctx.closePath();
    ctx.fill();

}

// Load previous results
const prevResults = JSON.parse(localStorage.getItem("results") || "[]");
const tbody = document.querySelector("#result-table tbody");

prevResults.forEach(result => {
    const newRow = tbody.insertRow(-1);

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    const rowTime = newRow.insertCell(3);
    const rowExecTime = newRow.insertCell(4);
    const rowResult = newRow.insertCell(5);

    rowX.innerText = result.x.toString();
    rowY.innerText = result.y.toString();
    rowR.innerText = result.r.toString();
    rowTime.innerText = result.time;
    rowExecTime.innerText = result.execTime;

    // Format result for display
    if (result.result === "true") {
        rowResult.innerText = "hit";
        rowResult.classList.add("hit");
    } else if (result.result === "false") {
        rowResult.innerText = "miss";
        rowResult.classList.add("miss");
    } else {
        rowResult.innerText = result.result;
        if (result.result.includes("error")) {
            rowResult.classList.add("error");
        }
    }
});

// Add CSS classes for styling
const style = document.createElement('style');
style.textContent = `
    .hit { color: #4caf50; font-weight: bold; }
    .miss { color: #f44336; font-weight: bold; }
    .error { color: #ff9800; font-weight: bold; }
`;
document.head.appendChild(style);