
package Comum;

import java.io.Serializable;

//Classe Horário
public class Horario implements Serializable {
    //Horário possuii uma hora e disponibilidade em boolean
    private String hora;
    private boolean disponilidade;

    public Horario(String hora, boolean disponilidade) {
        this.hora = hora;
        this.disponilidade = disponilidade;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isDisponilidade() {
        return disponilidade;
    }

    public void setDisponilidade(boolean disponilidade) {
        this.disponilidade = disponilidade;
    }
}
