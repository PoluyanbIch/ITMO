import React, { useState, useEffect } from 'react';
import "./Clock.css";

export default function Clock({ updateInterval = 10000 }) {
  const [currentTime, setCurrentTime] = useState(new Date());
  const [timeUntilUpdate, setTimeUntilUpdate] = useState(updateInterval / 1000);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentTime(new Date());
      setTimeUntilUpdate(updateInterval / 1000);
    }, updateInterval);

    const countdownId = setInterval(() => {
      setTimeUntilUpdate(prev => {
        if (prev <= 1) {
          return updateInterval / 1000;
        }
        return prev - 1;
      });
    }, 1000);

    return () => {
      clearInterval(intervalId);
      clearInterval(countdownId);
    };
  }, [updateInterval]);

  return (
    <div className="clock-container">
      <div className="clock-time">
        {currentTime.toLocaleTimeString('ru-RU', {
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit'
        })}
      </div>
      <div className="clock-date">
        {currentTime.toLocaleDateString('ru-RU', {
          weekday: 'short',
          day: 'numeric',
          month: 'short'
        })}
      </div>
      <div className="clock-countdown">
        Обновление: {timeUntilUpdate} сек
      </div>
    </div>
  );
}
