package com.github.poluyanbIch.weblab3;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name="clockBean")
@RequestScoped
public class ClockBean implements Serializable {
    public Date getDateTime() {
        return new Date();
    }
    public ClockBean() {}
}
