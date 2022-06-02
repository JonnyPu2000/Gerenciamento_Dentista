package Servidor;

import java.io.*;
import java.net.*;

import Comum.*;

import java.util.ArrayList;
import java.util.List;

public class Servidor {

    static ServerSocket serversocket;
    static Socket client_socket;
    static Conexao c;
    static String msg;
    static List<DentistaDTO> dentistas = new ArrayList<>();

    public Servidor() {
        try {
            serversocket = new ServerSocket(9600);
            System.out.println("Criando o Server Socket");
        } catch (Exception e) {
            System.out.println("Nao criei o server socket...");
        }
    }

    public static void main(String args[]) throws Exception {
        RequestEnt requisicao;
        new Servidor();

        while(true) {
            if(connect()) {
                requisicao = (RequestEnt) c.receive(client_socket);

                if(requisicao.getMetodo() == MethodEnum.CRIAR) {
                    dentistas.add((DentistaDTO) requisicao.getBody());
                    ResponseEnt<DentistaDTO> resp = new ResponseEnt<>(StatusEnum.SUCESSO);
                    c.send(client_socket, resp);
                    for(DentistaDTO d: dentistas) {
                        System.out.println(d.getMatricula());
                    }
                }

                if(requisicao.getMetodo() == MethodEnum.DELETAR) {
                    int matricula = (int) requisicao.getBody();
                    dentistas.removeIf(dentista -> dentista.getMatricula() == matricula);
                    ResponseEnt<Boolean> response = new ResponseEnt<>(StatusEnum.SUCESSO);
                    c.send(client_socket, response);
                }

                if(requisicao.getMetodo() == MethodEnum.LOGIN_DENT) {
                    int matricula = (int) requisicao.getBody();
                    DentistaDTO resultado = dentistas.stream().filter(dentista -> dentista.getMatricula() == matricula).findFirst().orElse(null);
                    ResponseEnt<Void> resp = new ResponseEnt<>(StatusEnum.ERRO);
                    if(resultado != null) {
                        resp.setStatus(StatusEnum.SUCESSO);
                    }
                    c.send(client_socket, resp);
                }

                if(requisicao.getMetodo() == MethodEnum.GET_DENTISTA) {
                    int matricula = (int) requisicao.getBody();
                    DentistaDTO resultado = dentistas.stream().filter(dentista -> dentista.getMatricula() == matricula).findFirst().orElse(null);
                    ResponseEnt<DentistaDTO> resp = new ResponseEnt(resultado, StatusEnum.SUCESSO);

                    c.send(client_socket, resp);
                }

                if(requisicao.getMetodo() == MethodEnum.AGENDAR) {
                    Agendamento agendamento = (Agendamento) requisicao.getBody();
                    DentistaDTO resultado = dentistas.stream().filter(dentista -> dentista.getMatricula() == agendamento.getMatriculaDentista()).findFirst().orElse(null);
                    Horario reserva = new Horario(agendamento.getHora(), false);
                    boolean reservado = resultado.reservarHorario(reserva);
                    ResponseEnt<Boolean> resp = new ResponseEnt<>(reservado, StatusEnum.SUCESSO);

                    c.send(client_socket, resp);
                }

                if(requisicao.getMetodo() == MethodEnum.CANCELAR) {
                    Agendamento agendamento = (Agendamento) requisicao.getBody();
                    DentistaDTO resultado = dentistas.stream().filter(dentista -> dentista.getMatricula() == agendamento.getMatriculaDentista()).findFirst().orElse(null);
                    Horario reserva = new Horario(agendamento.getHora(), false);
                    boolean cancelado = resultado.cancelarHorario(reserva);
                    ResponseEnt<Boolean> resp = new ResponseEnt<>(cancelado, StatusEnum.SUCESSO);

                    c.send(client_socket, resp);
                }

            }
        }











    }
    

    static boolean connect() {
        boolean ret;
        try {
            client_socket = serversocket.accept();              // fase de conexão
            ret = true;
        } catch (Exception e) {
            System.out.println("N�o fez conex�o" + e.getMessage());
            ret = false;
        }
        return ret;
    }
}
