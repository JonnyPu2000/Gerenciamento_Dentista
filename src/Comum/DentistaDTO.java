
package Comum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DentistaDTO implements Serializable {

    //Possui uma lista de horários, nome e Matrícula
    private List<Horario> horarios;
    private String nome;
    private int matricula;

    public DentistaDTO(String nome, int matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.horarios = new ArrayList<>();

        //Setando os horários disponíveis
        List<String> horas = Arrays.asList("08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00"); 

        for(String hora: horas) {
            this.horarios.add(new Horario(hora, true));
        }
    }
    
    
    public void listarHorarios() {
        //Loop que mostra os horários em forma de String
        for(Horario horario: this.horarios) {
            String disp = horario.isDisponilidade() ? "Disponivel" : "Indisponivel"; 
            System.out.println(String.format("%s - %s", horario.getHora(), disp));
        }
    }


    //gets e sets
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


    //Função para Cancelar um agendemanto, utiliza a classe horário
    public boolean cancelarHorario(Horario horario) throws Exception {
        //Detecta se existe
        Horario horarioExiste = this.horarios.stream().filter(h -> Objects.equals(h.getHora(), horario.getHora())).findFirst().orElse(null);

        //Caso não exista retorna uma mensagem
        if(horarioExiste == null) {
            throw new Exception("Horario inexistente");
        }
        
        //Caso exista seta a disponibilidade para disponível
        horarioExiste.setDisponilidade(true);
        
        return true;
    }


    //Função para realizar o agendamento, utiliza a classe horário
    public boolean reservarHorario(Horario horario) throws Exception {
        //Detecta se existe
        Horario horarioExiste = this.horarios.stream().filter(h -> Objects.equals(h.getHora(), horario.getHora())).findFirst().orElse(null);

        //Caso não exista retorna uma mensagem
        if(horarioExiste == null) {
            throw new Exception("Horario inexistente");
        }

        //Caso exista e esteja indisponivel retorna uma mensagem
        if(!horarioExiste.isDisponilidade()) {
            throw new Exception("Horario indisponivel :(");
        }

        //Caso exista e esteja disponivel seta a disponibilidade
        horarioExiste.setDisponilidade(false);
        
        System.out.println("Horario reservado com sucesso!");
        return true;
    }
}   
