/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author profslvo
 */
public class DentistaDTO implements Serializable {
    private List<Horario> horarios;
    private String nome;
    private int matricula;

    public DentistaDTO(String nome, int matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }
    
    
    public void listarHorarios() {
        for(Horario horario: this.horarios) {
            String disp = horario.isDisponilidade() ? "Disponivel" : "Indisponivel"; 
            System.out.println(String.format("%s - %s", horario.getHora(), disp));
        }
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public String getNome() {
        return nome;
    }

    public int getMatricula() {
        return matricula;
    }
    
    
    public void addHorario(Horario horario) {
        boolean isDuplicado = this.horarios.contains(horario);
        
        if(isDuplicado) {
            System.out.println("Horario duplicado!");
            return;
        }
        
        this.horarios.add(horario);
    } 
    
    public boolean removerHorario(Horario horario) {
        boolean existe = this.horarios.contains(horario);
        
        if(!existe) {
            System.out.println("Horario inexistente!");
            return false;
        }
        
        this.horarios.remove(horario);
        
        return true;
    }
    
    public boolean reservarHorario(Horario horario) throws Exception {
        boolean horarioExiste = this.horarios.contains(horario);
        if(!horarioExiste) {
            throw new Exception("Horario inexistente");
        }
        int horarioIndex = this.horarios.indexOf(horario);
        this.horarios.get(horarioIndex).setDisponilidade(false);
        
        System.out.println("Horario reservado com sucesso!");
        return true;
    }
    
}   
