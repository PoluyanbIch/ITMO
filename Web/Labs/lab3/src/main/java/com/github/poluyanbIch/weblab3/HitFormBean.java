package com.github.poluyanbIch.weblab3;

import com.github.poluyanbIch.weblab3.model.Hit;
import org.primefaces.PrimeFaces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ManagedBean(name="hitFormBean")
@SessionScoped
public class HitFormBean implements Serializable {

    @ManagedProperty("#{hitsBean}")
    private HitsBean hitsBean;

    private String selectedX;
    private String yCoordinate;
    private String selectedR;

    private final List<String> xValues = List.of("-2", "-1.5", "-1", "-0.5", "0", "0.5", "1", "1.5");
    private final List<String> radiusList = List.of("1", "1.5", "2", "2.5", "3");

    public HitFormBean() {
        selectedR = radiusList.get(0);
    }

    public List<String> getxValues() { return xValues; }
    public List<String> getradiusList() { return radiusList; }

    public String getselectedX() { return selectedX; }
    public void setselectedX(String selectedX) { this.selectedX = selectedX; }

    public String getyCoordinate() { return yCoordinate; }
    public void setyCoordinate(String yCoordinate) { this.yCoordinate = yCoordinate; }

    public String getselectedR() { return selectedR; }
    public void setselectedR(String selectedR) { this.selectedR = selectedR; }

    public String getglobalXAsJs() {
        return selectedX != null ? selectedX : "null";
    }

    public String getglobalYAsJs() {
        return yCoordinate != null ? yCoordinate : "null";
    }

    public String getglobalRAsJs() {
        return selectedR != null ? selectedR : "null";
    }

    public void submitPoint() {
    FacesContext context = FacesContext.getCurrentInstance();

    if (selectedR == null || selectedR.isEmpty()) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select R", null));
        context.validationFailed();
        return;
    }

    if (selectedX == null || selectedX.isEmpty() || yCoordinate == null || yCoordinate.isEmpty()) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Coordinates are required", null));
        context.validationFailed();
        return;
    }

    long timestamp = System.nanoTime();

    Hit hit = new Hit();
    hit.setX(new BigDecimal(selectedX));      // X из клика
    hit.setY(new BigDecimal(yCoordinate));    // Y из клика
    hit.setR(new BigDecimal(selectedR));      // R текущий
    hit.setHit(AreaChecker.hit(hit.getX(), hit.getY(), hit.getR()));
    hit.setCurrentDatetime(new Date());
    hit.setExecutionTime((System.nanoTime() - timestamp) / 1_000_000_000.0);

    hitsBean.addHit(hit);

    String json = hitsBean.generateHitsAsJson();
    PrimeFaces.current().executeScript("updateHits(" + json + ");");
}


    public void submit() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (selectedX == null) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select X", null));
            context.validationFailed();
            return;
        }

        if (yCoordinate == null || yCoordinate.isEmpty()) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter Y", null));
            context.validationFailed();
            return;
        }

        long timestamp = System.nanoTime();

        Hit hit = new Hit();
        hit.setX(new BigDecimal(selectedX));
        hit.setY(new BigDecimal(yCoordinate));
        hit.setR(new BigDecimal(selectedR));
        hit.setHit(AreaChecker.hit(hit.getX(), hit.getY(), hit.getR()));
        hit.setCurrentDatetime(new Date());
        hit.setExecutionTime((System.nanoTime() - timestamp) / 1_000_000_000.0);

        hitsBean.addHit(hit);

        String json = hitsBean.generateHitsAsJson();
        PrimeFaces.current().executeScript("updateHits(" + json + ");");
    }

    public void sethitsBean(HitsBean hitsBean) { this.hitsBean = hitsBean; }
    public HitsBean gethitsBean() { return hitsBean; }
}
