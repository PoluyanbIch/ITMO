package com.lab6.common.utility;

import com.lab6.common.models.Worker;
import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 11L;
    private String string;
    private Worker band = null;

    public Request(String string) {
        this.string = string;
    }

    public Request(String string, Worker band) {
        this.string = string;
        this.band = band;
    }

    public String[] getCommand() {
        String[] inputCommand = (string.trim() + " ").split(" ", 2);
        inputCommand[1] = inputCommand[1].trim();
        return inputCommand;
    }

    public Worker getBand() {
        return band;
    }

    public void setCommand(String command) {
        this.string = command;
    }

    public void setBand(Worker band) {
        this.band = band;
    }

    @Override
    public String toString() {
        return "Request{" +
                "string='" + string + '\'' +
                ", band=" + band +
                '}';
    }
}
