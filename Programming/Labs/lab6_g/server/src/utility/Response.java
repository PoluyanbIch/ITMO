package utility;

import java.io.Serializable;

public class Response implements Serializable {
    private int exitCode;
    private String message;
    private Object responseObj;

    public Response(int code, String s, Object obj) {
        exitCode = code;
        message = s;
        responseObj = obj;
    }

    public Response(int code, String s) {
        this(code, s, null);
    }

    public Response(String s, Object obj) {
        this(200, s, obj);
    }

    public Response(String s) {
        this(200, s, null);
    }

    public int getExitCode() { return exitCode; }
    public String getMessage() { return message; }
    public Object getResponseObj() { return responseObj; }
    public String toString() { return String.valueOf(exitCode)+";"+message+";"+(responseObj==null?"null":responseObj.toString()); }
}