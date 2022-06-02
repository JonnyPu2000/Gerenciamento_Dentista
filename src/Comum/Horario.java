/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comum;

import java.io.Serializable;

/**
 *
 * @author profslvo
 */
public class Horario implements Serializable {
    private String hora;
    private boolean disponilidade;
    private Paciente paciente; 

    public Horario(String hora, boolean disponilidade) {
        this.hora = hora;
        this.disponilidade = disponilidade;
    }
    
    public Horario(String hora, boolean disponilidade, Paciente paciente) {
        this.hora = hora;
        this.disponilidade = disponilidade;
        this.paciente = paciente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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
