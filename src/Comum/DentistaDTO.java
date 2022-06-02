/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        this.horarios = new ArrayList<>();

        List<String> horas = Arrays.asList("08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00");

        for(String hora: horas) {
            this.horarios.add(new Horario(hora, true));
        }
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
    
    public boolean cancelarHorario(Horario horario) throws Exception {
        Horario horarioExiste = this.horarios.stream().filter(h -> Objects.equals(h.getHora(), horario.getHora())).findFirst().orElse(null);

        if(horarioExiste == null) {
            throw new Exception("Horario inexistente");
        }
        
        horarioExiste.setDisponilidade(true);
        
        return true;
    }
    
    public boolean reservarHorario(Horario horario) throws Exception {
        Horario horarioExiste = this.horarios.stream().filter(h -> Objects.equals(h.getHora(), horario.getHora())).findFirst().orElse(null);

        if(horarioExiste == null) {
            throw new Exception("Horario inexistente");
        }

        if(!horarioExiste.isDisponilidade()) {
            throw new Exception("Horario indisponivel :(");
        }
        horarioExiste.setDisponilidade(false);
        
        System.out.println("Horario reservado com sucesso!");
        return true;
    }
}   
