const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");
const tooltip = document.getElementById("tooltip");

let canvasWidth, canvasHeight, localR;

// Объявляем глобальные переменные только если они еще не существуют
if (typeof globalX === 'undefined') window.globalX = null;
if (typeof globalY === 'undefined') window.globalY = null;
if (typeof globalR === 'undefined') window.globalR = null;
if (typeof hits === 'undefined') window.hits = [];

// Настройки отображения
const imagePartRatio = 0.7;
const hatchLength = 10;
const letterHeight = 15;

function resizeCanvas() {
    const rect = canvas.getBoundingClientRect();
    const cssWidth = rect.width;
    const cssHeight = rect.height;
    const dpr = window.devicePixelRatio || 1;

    const displayWidth = Math.round(cssWidth * dpr);
    const displayHeight = Math.round(cssHeight * dpr);

    if (canvas.width !== displayWidth || canvas.height !== displayHeight) {
        canvas.width = displayWidth;
        canvas.height = displayHeight;
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.scale(dpr, dpr);
    }

    canvasWidth = canvas.clientWidth;
    canvasHeight = canvas.clientHeight;

    // фиксированная сетка —3..+3
    localUnit = canvasWidth / 6;

    if (globalR != null) {
        // фигура масштабирующаяся
        localR = globalR * localUnit;
    }
}

function drawLine(x1, y1, x2, y2) {
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
}

function drawAxis() {
    ctx.strokeStyle = "#cccccc";
    ctx.lineWidth = 1;
    ctx.fillStyle = "#cccccc";
    ctx.font = `${letterHeight}px Arial`;
    ctx.textAlign = "center";
    ctx.textBaseline = "middle";

    // X-axis
    ctx.beginPath();
    drawLine(0, canvasHeight / 2, canvasWidth, canvasHeight / 2);
    ctx.stroke();

    // Y-axis
    ctx.beginPath();
    drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight);
    ctx.stroke();

    // Ха́тчи и подписи для R
    if (localR != null && globalR != null) {
        ctx.beginPath();
        
        // X-axis hatches
        drawLine(canvasWidth/2 + localR, canvasHeight/2 - hatchLength/2, canvasWidth/2 + localR, canvasHeight/2 + hatchLength/2);
        drawLine(canvasWidth/2 - localR, canvasHeight/2 - hatchLength/2, canvasWidth/2 - localR, canvasHeight/2 + hatchLength/2);
        drawLine(canvasWidth/2 + localR/2, canvasHeight/2 - hatchLength/2, canvasWidth/2 + localR/2, canvasHeight/2 + hatchLength/2);
        drawLine(canvasWidth/2 - localR/2, canvasHeight/2 - hatchLength/2, canvasWidth/2 - localR/2, canvasHeight/2 + hatchLength/2);
        
        // Y-axis hatches
        drawLine(canvasWidth/2 - hatchLength/2, canvasHeight/2 - localR, canvasWidth/2 + hatchLength/2, canvasHeight/2 - localR);
        drawLine(canvasWidth/2 - hatchLength/2, canvasHeight/2 + localR, canvasWidth/2 + hatchLength/2, canvasHeight/2 + localR);
        drawLine(canvasWidth/2 - hatchLength/2, canvasHeight/2 - localR/2, canvasWidth/2 + hatchLength/2, canvasHeight/2 - localR/2);
        drawLine(canvasWidth/2 - hatchLength/2, canvasHeight/2 + localR/2, canvasWidth/2 + hatchLength/2, canvasHeight/2 + localR/2);
        ctx.stroke();

        // Подписи осей
        ctx.fillText("X", canvasWidth - 10, canvasHeight/2 - 10);
        ctx.fillText("Y", canvasWidth/2 + 10, 10);
        
        // Подписи значений R
        ctx.fillText(globalR.toString(), canvasWidth/2 + localR, canvasHeight/2 + 20);
        ctx.fillText('-' + globalR.toString(), canvasWidth/2 - localR, canvasHeight/2 + 20);
        ctx.fillText((globalR/2).toString(), canvasWidth/2 + localR/2, canvasHeight/2 + 20);
        ctx.fillText('-' + (globalR/2).toString(), canvasWidth/2 - localR/2, canvasHeight/2 + 20);
        
        ctx.fillText(globalR.toString(), canvasWidth/2 - 20, canvasHeight/2 - localR);
        ctx.fillText('-' + globalR.toString(), canvasWidth/2 - 20, canvasHeight/2 + localR);
        ctx.fillText((globalR/2).toString(), canvasWidth/2 - 25, canvasHeight/2 - localR/2);
        ctx.fillText('-' + (globalR/2).toString(), canvasWidth/2 - 25, canvasHeight/2 + localR/2);
    }
}

function drawFigure() {
    if (localR == null || globalR == null) return;

    ctx.fillStyle = "rgba(255, 105, 97, 0.5)";
    ctx.strokeStyle = "#ff6961";
    ctx.lineWidth = 2;

    // I четверть: квадрат (x>0, y>0)
    ctx.beginPath();
    ctx.rect(canvasWidth/2, canvasHeight/2 - localR, localR, localR);
    ctx.fill();
    ctx.stroke();
    ctx.closePath();

    // III четверть: треугольник (x<0, y<0)
    ctx.beginPath();
    ctx.moveTo(canvasWidth/2, canvasHeight/2);
    ctx.lineTo(canvasWidth/2 - localR, canvasHeight/2);
    ctx.lineTo(canvasWidth/2, canvasHeight/2 + localR);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    // IV четверть: сектор круга (x>0, y<0)
    ctx.beginPath();
    ctx.moveTo(canvasWidth/2, canvasHeight/2);
    ctx.arc(canvasWidth/2, canvasHeight/2, localR, 0, Math.PI/2, false);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
}

function drawHistory() {
    if (!hits || hits.length === 0 || !localR || !globalR) return;

    for (let i = 0; i < hits.length; i++) {
        const point = hits[i];
        
        // Получаем R, с которым точка была сохранена
        const pointR = Number(point.r); 

        // Пропускаем точку, если ее R невалидный
        if (!pointR) {
            continue; 
        }

        // Масштабируем точку относительно её собственного R
        const x = canvasWidth/2 + (Number(point.x) / pointR) * localR;
        const y = canvasHeight/2 - (Number(point.y) / pointR) * localR;

        ctx.beginPath();
        ctx.arc(x, y, 4, 0, Math.PI*2);
        ctx.fillStyle = point.isHit ? "#3ece4a" : "#f57138";
        ctx.fill();
        ctx.strokeStyle = "#000000";
        ctx.lineWidth = 1;
        ctx.stroke();
    }
}

function drawHitPoint() {
    // Проверяем, что все значения есть и они валидны
    if (globalX === null || globalY === null || !globalR || !localR) return;

    const y = canvasHeight/2 - (localR / globalR) * globalY;
    const x = canvasWidth/2 + (localR / globalR) * globalX;

    ctx.beginPath();
    ctx.strokeStyle = "green";
    ctx.lineWidth = 3;
    const size = 12;
    drawLine(x - size/2, y, x + size/2, y);
    drawLine(x, y - size/2, x, y + size/2);
    ctx.stroke();
}

function fillCanvas() {
    ctx.clearRect(0, 0, canvasWidth, canvasHeight);
    resizeCanvas();
    drawFigure();
    drawAxis();
    drawHistory();
    drawHitPoint();
}

// Отправка точки на сервер через PrimeFaces
function sendPoint(x, y, r) {
    canvasHandler([{name: 'x', value: x}, {name: 'y', value: y}, {name: 'r', value: r}]);
}

// События мыши
canvas.addEventListener("mousemove", (event) => {
    if (!globalR || !localR) return;

    const rect = canvas.getBoundingClientRect();
    const x = ((event.clientX - rect.left) - canvasWidth/2) * (globalR / localR);
    const y = (canvasHeight/2 - (event.clientY - rect.top)) * (globalR / localR);

    if (tooltip) {
        tooltip.classList.remove("hidden");
        tooltip.style.whiteSpace = "pre";
        tooltip.textContent = `X: ${x.toFixed(2)}\nY: ${y.toFixed(2)}\nR: ${globalR}`;
        tooltip.style.left = `${event.pageX + 10}px`;
        tooltip.style.top = `${event.pageY - 40}px`;
    }
});

canvas.addEventListener("mouseleave", () => {
    if (tooltip) {
        tooltip.classList.add("hidden");
    }
});

canvas.addEventListener("click", (event) => {
    if (!globalR) {
        alert("R must be set!");
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const x = ((event.clientX - rect.left) - canvasWidth/2) * (globalR / localR);
    const y = (canvasHeight/2 - (event.clientY - rect.top)) * (globalR / localR);

    sendPoint(x.toFixed(2), y.toFixed(2), globalR);
});

// Обновление после событий PrimeFaces
document.addEventListener("updateGlobalValues", function(event){
    if(event.detail){
        globalX = event.detail.x;
        globalY = event.detail.y;
        globalR = event.detail.r;
        fillCanvas();
    }
});

document.addEventListener("updateHits", function(event){
    if(event.detail){
        hits = event.detail.hits || [];
        fillCanvas();
    }
});

window.addEventListener("resize", fillCanvas);

// Инициализация при загрузке
document.addEventListener("DOMContentLoaded", function() {
    fillCanvas();
});