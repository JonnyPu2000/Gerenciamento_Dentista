package Comum;

import java.io.Serializable;

//Classe de agendamento

public class Agendamento implements Serializable {

    //Recebe o horário e matrícula do dentista
    public Agendamento(String hora, int matriculaDentista) {
        this.hora = hora;
        this.matriculaDentista = matriculaDentista;
    }

    private String hora;
    private int matriculaDentista;

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getMatriculaDentista() {
        return matriculaDentista;
    }

    public void setMatriculaDentista(int matriculaDentista) {
        this.matriculaDentista = matriculaDentista;
    }
}
