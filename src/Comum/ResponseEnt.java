package Comum;

import java.io.Serializable;

public class ResponseEnt<T> implements Serializable {
    private T body;
    private StatusEnum status;

    public ResponseEnt(StatusEnum status) {
        this.body = body;
        this.status = status;
    }
    public ResponseEnt(T body, StatusEnum status) {
        this.body = body;
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
