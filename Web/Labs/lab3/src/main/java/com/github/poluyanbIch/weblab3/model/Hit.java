package com.github.poluyanbIch.weblab3.model;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class Hit implements Serializable {

    private Long id;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    private boolean hit;
    private Double executionTime;
    private Date currentDatetime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public void setR(BigDecimal r) {
        this.r = r;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public void setExecutionTime(Double executionTime) {
        this.executionTime = executionTime;
    }

    public void setCurrentDatetime(Date currentDatetime) {
        this.currentDatetime = currentDatetime;
    }

    public Date getCurrentDatetime() {
        return currentDatetime;
    }

    public boolean isHit() {
        return hit;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public BigDecimal getR() {
        return r;
    }

    public boolean getHit() {
        return hit;
    }

    public Double getExecutionTime() {
        return executionTime;
    }

    public String toJson() {
        return "{" +
                "\"x\":" + x.toPlainString() +
                ",\"y\":" + y.toPlainString() +
                ",\"r\":" + r.toPlainString() +
                ",\"isHit\":" + (hit ? "true" : "false") +
                "}";
    }

}
