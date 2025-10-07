"use strict";

const state = {
    x: null,
    y: null,
    r: null,
};

const table = document.getElementById("result-table");
const error = document.getElementById("error");
const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");

drawGraph();
updateSubmitButton();

document.querySelectorAll('.x-checkbox').forEach(checkbox => {
    checkbox.addEventListener('change', (ev) => {
        if (ev.target.checked) {
            document.querySelectorAll('.x-checkbox').forEach(cb => {
                if (cb !== ev.target) cb.checked = false;
            });
            state.x = parseFloat(ev.target.value);
            document.getElementById('x').value = state.x;
            error.hidden = true;
        } else {
            state.x = null;
            document.getElementById('x').value = '';
        }
        updateSubmitButton();
    });
});

document.getElementById("y").addEventListener("input", (ev) => {
    const value = parseFloat(ev.target.value);
    if (isNaN(value) || value < -5 || value > 5) {
        showError("Y must be a number between -5 and 5");
        state.y = null;
    } else {
        state.y = value;
        error.hidden = true;
    }
    updateSubmitButton();
});

document.querySelectorAll('.r-button').forEach(button => {
    button.addEventListener('click', (ev) => {
        document.querySelectorAll('.r-button').forEach(btn => {
            btn.classList.remove('active');
        });

        ev.target.classList.add('active');

        state.r = parseFloat(ev.target.value);
        document.getElementById('r').value = state.r;
        error.hidden = true;
        drawGraph();
        updateSubmitButton();
    });
});

const validateState = (state) => {
    error.hidden = true;

    if (state.x === null || isNaN(state.x) || state.x < -2 || state.x > 2) {
        showError("Please select X value between -2 and 2");
        return false;
    }

    if (state.y === null || isNaN(state.y) || state.y < -5 || state.y > 5) {
        showError("Y must be a number between -5 and 5");
        return false;
    }

    if (state.r === null || isNaN(state.r) || state.r < 1 || state.r > 5) {
        showError("Please select R value between 1 and 5");
        return false;
    }

    return true;
}

function showError(message) {
    error.textContent = message;
    error.hidden = false;
}

document.getElementById("data-form").addEventListener("submit", function (ev) {
    if (!validateState(state)) {
        ev.preventDefault();
    }
});

function updateSubmitButton() {
    const submitButton = document.querySelector('.submit-button');
    const isValid = state.x !== null && state.y !== null && state.r !== null;
    submitButton.disabled = !isValid;
}

function drawGraph() {
    const width = canvas.width;
    const height = canvas.height;
    const centerX = width / 2;
    const centerY = height / 2;
    const scale = 30;

    ctx.clearRect(0, 0, width, height);

    ctx.fillStyle = "#2a2a2a";
    ctx.fillRect(0, 0, width, height);

    ctx.fillStyle = "rgba(255, 105, 97, 0.5)";

    const rectWidth = (state.r) * scale;
    const rectHeight = (-state.r / 2) * scale;
    ctx.fillRect(centerX, centerY, rectWidth, rectHeight);

    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.lineTo(centerX, centerY - state.r * scale);
    ctx.lineTo(centerX - (state.r) * scale, centerY);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, (state.r) * scale, Math.PI, Math.PI * 0.5, true);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.strokeStyle = "#e1e1e1";
    ctx.lineWidth = 2;

    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, height);

    ctx.moveTo(0, centerY);
    ctx.lineTo(width, centerY);

    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX - 5, 10);
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX + 5, 10);

    ctx.moveTo(width, centerY);
    ctx.lineTo(width - 10, centerY - 5);
    ctx.moveTo(width, centerY);
    ctx.lineTo(width - 10, centerY + 5);

    ctx.stroke();

    ctx.strokeStyle = "rgba(255, 255, 255, 0.2)";
    ctx.lineWidth = 1;
    ctx.fillStyle = "#e1e1e1";
    ctx.font = "12px Arial";
    ctx.textAlign = "center";
    ctx.textBaseline = "middle";

    for (let i = -5; i <= 5; i++) {
        const x = centerX + i * scale;

        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, height);
        ctx.stroke();

        if (i !== 0 && Number.isInteger(i)) {
            ctx.fillText(i.toString(), x, centerY + 15);
        }
    }

    for (let i = -5; i <= 5; i++) {
        const y = centerY + i * scale;

        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();

        if (i !== 0 && Number.isInteger(i)) {
            ctx.fillText((-i).toString(), centerX - 15, y);
        }
    }

    ctx.fillText("x", width - 10, centerY - 10);
    ctx.fillText("y", centerX + 10, 10);
    ctx.fillText("R = " + state.r, 10, 20);

    drawPointsOnGraph();
}

function transformCanvasToPlane(canvasX, canvasY, r) {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 30;

    const planeX = (canvasX - centerX) / scale;
    const planeY = (centerY - canvasY) / scale;

    return {
        x: parseFloat(planeX.toFixed(2)),
        y: parseFloat(planeY.toFixed(2))
    };
}


async function sendCoordinatesToServer(x, y, r) {
    try {
        const params = new URLSearchParams();
        params.append('x', x);
        params.append('y', y);
        params.append('r', r);
        params.append('action', 'checkPoint');

        const response = await fetch("/webapp/controller?" + params.toString());

        if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const data = await response.json();

        if (data.error) {
            showError("Validation error: " + data.error);
            return;
        }

        drawPointOnGraph(x, y, r, data.result);

        addPointToTable(x, y, r, data.result);

        error.hidden = true;

    } catch (error) {
        console.error("Fetch error:", error);
        showError("Connection error: " + error.message);
    }
}

function addPointToTable(x, y, r, result) {
    const table = document.getElementById("result-table");
    const tbody = table.getElementsByTagName('tbody')[0];

    const newRow = tbody.insertRow(0);
    newRow.insertCell(0).innerText = x.toFixed(2);
    newRow.insertCell(1).innerText = y.toFixed(2);
    newRow.insertCell(2).innerText = r.toFixed(2);

    const resultCell = newRow.insertCell(3);
    resultCell.innerText = result ? "hit" : "miss";
    resultCell.className = result ? "hit" : "miss";
}

function drawPointOnGraph(x, y, r, result) {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 30;

    const scaledX = (x / r) * state.r;
    const scaledY = (y / r) * state.r;

    const pixelX = centerX + scaledX * scale;
    const pixelY = centerY - scaledY * scale;

    ctx.beginPath();
    ctx.arc(pixelX, pixelY, 4, 0, 2 * Math.PI);
    ctx.fillStyle = result ? '#4caf50' : '#f44336';
    ctx.fill();
    ctx.strokeStyle = '#fff';
    ctx.lineWidth = 1;
    ctx.stroke();
}

canvas.addEventListener('click', async (event) => {
    if (isNaN(state.r) || state.r < 1 || state.r > 5) {
        showError("Cannot determine coordinates: R parameter is not set or invalid");
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    const graphCoords = transformCanvasToPlane(x, y, state.r);


    await sendCoordinatesToServer(graphCoords.x, graphCoords.y, state.r);
});


function drawPointsOnGraph() {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 30;

    const table = document.getElementById("result-table");
    if (!table) return;

    const tbody = table.getElementsByTagName('tbody')[0];
    if (!tbody) return;

    const rows = tbody.rows;

    for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const pointX = parseFloat(row.cells[0].textContent);
        const pointY = parseFloat(row.cells[1].textContent);
        const pointR = parseFloat(row.cells[2].textContent);
        const isHit = row.cells[3].classList.contains('hit');

        if (!isNaN(pointX) && !isNaN(pointY) && !isNaN(pointR)) {
            const scaledX = (pointX / pointR) * state.r;
            const scaledY = (pointY / pointR) * state.r;

            const pixelX = centerX + scaledX * scale;
            const pixelY = centerY - scaledY * scale;

            if (pixelX >= 0 && pixelX <= canvas.width && pixelY >= 0 && pixelY <= canvas.height) {
                ctx.beginPath();
                ctx.arc(pixelX, pixelY, 4, 0, 2 * Math.PI);
                ctx.fillStyle = isHit ? '#4caf50' : '#f44336';
                ctx.fill();
                ctx.strokeStyle = '#fff';
                ctx.lineWidth = 1;
                ctx.stroke();
            }
        }
    }
}

const style = document.createElement('style');
style.textContent = `
    .hit { color: #4caf50; font-weight: bold; }
    .miss { color: #f44336; font-weight: bold; }
    .error { color: #ff9800; font-weight: bold; }
`;
document.head.appendChild(style);