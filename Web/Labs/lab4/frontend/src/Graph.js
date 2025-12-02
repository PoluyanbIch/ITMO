import React, { useEffect, useRef } from "react";

export default function Graph({ R, points, onAddPoint, currentX, currentY }) {
  const canvasRef = useRef(null);
  const tooltipRef = useRef(null);

  const draw = () => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");

    const rect = canvas.getBoundingClientRect();
    canvas.width = rect.width * devicePixelRatio;
    canvas.height = rect.height * devicePixelRatio;
    ctx.scale(devicePixelRatio, devicePixelRatio);

    const w = rect.width;
    const h = rect.height;
    
    const unit = w / 10;
    const localR = R ? R * unit : null;

    ctx.clearRect(0, 0, w, h);
    ctx.lineWidth = 1;

    if (localR) {
      ctx.fillStyle = "rgba(255,105,97,0.5)";
      ctx.strokeStyle = "#ff6961";

      const centerX = w / 2;
      const centerY = h / 2;

      ctx.beginPath();
      ctx.moveTo(centerX, centerY);
      ctx.lineTo(centerX + localR/2, centerY);
      ctx.lineTo(centerX, centerY - localR/2);
      ctx.closePath();
      ctx.fill();
      ctx.stroke();

      ctx.beginPath();
      ctx.rect(centerX - localR, centerY - localR/2, localR, localR/2);
      ctx.fill();
      ctx.stroke();

      ctx.beginPath();
      ctx.moveTo(centerX, centerY);
      ctx.arc(centerX, centerY, localR, Math.PI * 0.5, Math.PI);
      ctx.closePath();
      ctx.fill();
      ctx.stroke();
    }

    ctx.strokeStyle = "#bbbbbb";
    ctx.beginPath();
    ctx.moveTo(0, h/2); ctx.lineTo(w, h/2);
    ctx.moveTo(w/2, 0); ctx.lineTo(w/2, h);
    ctx.stroke();

    if (points && R && localR) {
      points.forEach(p => {
        const pR = Number(p.r);
        if (!pR) return;

        const x = w/2 + (p.x / pR) * localR;
        const y = h/2 - (p.y / pR) * localR;

        ctx.beginPath();
        ctx.arc(x, y, 4, 0, Math.PI * 2);
        ctx.fillStyle = p.isHit ? "#3ece4a" : "#f57138";
        ctx.fill();
        ctx.strokeStyle = "#000";
        ctx.stroke();
      });
    }

  };

  useEffect(draw, [R, points, currentX, currentY]);
  useEffect(() => window.addEventListener("resize", draw), []);

  const onClick = (e) => {
    if (!R) return alert("Сначала выберите R");
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const px = e.clientX - rect.left;
    const py = e.clientY - rect.top;

    const unit = rect.width / 10;
    const localR = R * unit;

    const x = (px - rect.width/2) * (R / localR);
    const y = (rect.height/2 - py) * (R / localR);

    onAddPoint(Number(x.toFixed(2)), Number(y.toFixed(2)), R);
  };

  const onMove = (e) => {
    if (!R) return;

    const tooltip = tooltipRef.current;
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();

    const px = e.clientX - rect.left;
    const py = e.clientY - rect.top;

    const unit = rect.width / 10;
    const localR = R * unit;

    const x = (px - rect.width/2) * (R / localR);
    const y = (rect.height/2 - py) * (R / localR);

    tooltip.style.left = e.pageX + 10 + "px";
    tooltip.style.top = e.pageY - 40 + "px";
    tooltip.textContent = `X: ${x.toFixed(2)}\nY: ${y.toFixed(2)}\nR: ${R}`;
    tooltip.style.display = "block";
  };

  return (
    <>
      <div style={{ position: "relative" }}>
        <canvas
          ref={canvasRef}
          onClick={onClick}
          onMouseMove={onMove}
          onMouseLeave={() => (tooltipRef.current.style.display = "none")}
          style={{
            width: "100%",
            height: "360px",
            border: "1px solid #e6eef8",
            borderRadius: "8px",
            background: "linear-gradient(180deg,#fbfdff,#f7fbff)"
          }}
        />
        <div
          ref={tooltipRef}
          style={{
            display: "none",
            position: "absolute",
            padding: "6px 10px",
            background: "#fff",
            border: "1px solid #ccc",
            borderRadius: "6px",
            whiteSpace: "pre",
            pointerEvents: "none"
          }}
        />
      </div>
    </>
  );
}
