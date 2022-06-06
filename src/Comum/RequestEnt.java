package Comum;

import java.io.Serializable;


//Classe para solicitação de Métodos
public class RequestEnt<T> implements Serializable {

    public RequestEnt(MethodEnum metodo, T body) {
        this.metodo = metodo;
        this.body = body;
    }

    public MethodEnum getMetodo() {
        return metodo;
    }

    public void setMetodo(MethodEnum metodo) {
        this.metodo = metodo;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    private MethodEnum metodo;
    private T body;
}
