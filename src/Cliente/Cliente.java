package Cliente;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

import Comum.*;

public class Cliente {

    static Conexao c;
    static Socket socket;

    private static boolean trocarUsuario = false;

    public Cliente() {
        try {
            socket = new Socket("localhost", 9600);
        } // fase de conex�o
        catch (Exception e) {
            System.out.println("Nao consegui resolver o host...");
        }
    }



    public static void main(String args[]) throws Exception{
        Scanner entrada =  new Scanner(System.in);
        int  tipoUsuario;
        boolean loop = true;

        System.out.println("Voce eh: [0] Dentista [1] Paciente [2] Administrador");
        tipoUsuario = entrada.nextInt();
        while(loop) {
            if(trocarUsuario) {
                System.out.println("Voce eh: [0] Dentista [1] Paciente [2] Administrador");
                tipoUsuario = entrada.nextInt();
                trocarUsuario = false;
            }
            loop = loopFunction(tipoUsuario);
        }

        try {
            socket.close();                               // fase de desconex�o
        } catch (IOException e) {
            System.out.println("N�o encerrou a conex�o corretamente" + e.getMessage());
        }
    }

    private static boolean loopFunction(int tipoUsuario) throws Exception {
        Scanner entrada = new Scanner(System.in);
        boolean loop = false;
        switch(tipoUsuario) {
            case 0:
                System.out.println("Digite sua matricula: ");
                int mat = entrada.nextInt();

                RequestEnt<Integer> requisicao = new RequestEnt<>(MethodEnum.LOGIN_DENT, mat);
                new Cliente();
                c.send(socket, requisicao);


                ResponseEnt<Void> resp1 =  (ResponseEnt<Void>) c.receive(socket);

                if(resp1.getStatus() == StatusEnum.ERRO) {
                    throw new Exception("Matricula Invalida!");
                }
                boolean flagDentista = true;
                while(flagDentista) {
                    System.out.println("===================================================");
                    System.out.println("[1] Listar Horarios  [2] Cancelar Consulta [3] sair");
                    System.out.println("===================================================");
                    int opcaoDentista = entrada.nextInt();

                    if(opcaoDentista == 1) {
                        listarHorariosDentista(mat);
                    }

                    if(opcaoDentista == 2) {
                        listarHorariosDentista(mat);
                        entrada = new Scanner(System.in);
                        System.out.println("Qual horario voce deseja cancelar?");
                        String horarioEscolhido = entrada.nextLine();
                        entrada = new Scanner(System.in);

                        RequestEnt<Agendamento> requisicaoCancelar = new RequestEnt<>(MethodEnum.CANCELAR, new Agendamento(horarioEscolhido, mat));

                        new Cliente();
                        c.send(socket, requisicaoCancelar);

                        ResponseEnt<Boolean> respCancelar =  (ResponseEnt<Boolean>) c.receive(socket);
                        if(respCancelar.getBody()) {
                            System.out.println("CANCELADO COM SUCESSO!");
                        } else {
                            throw new Exception("Algo de errado aconteceu!");
                        }
                    }

                    if(opcaoDentista == 3) {
                        flagDentista = false;
                        setTrocarUsuario(true);
                    }
                }


                loop = true;
                break;
            case 1:
                System.out.println("Digite seu nome: ");
                String nomePaciente = entrada.nextLine();
                entrada = new Scanner(System.in);
                Paciente paciente = new Paciente(nomePaciente);
                System.out.println("=================================================");
                System.out.println(String.format("        Bem vindo %s", nomePaciente));
                System.out.println("=================================================");
                System.out.println("[1] Agendar consulta      [2] sair");
                System.out.println("=================================================");
                int opcaoPaciente = entrada.nextInt();
                if(opcaoPaciente == 1) {
                    System.out.println("Qual a matricula do dentista que voce quer agendar?");
                    entrada = new Scanner(System.in);
                    int matriculaDentista = entrada.nextInt();
                    entrada = new Scanner(System.in);

                    RequestEnt<Integer> requisicaoGetDentista = new RequestEnt<>(MethodEnum.GET_DENTISTA, matriculaDentista);
                    new Cliente();
                    c.send(socket, requisicaoGetDentista);


                    ResponseEnt<DentistaDTO> respVerHorario =  (ResponseEnt<DentistaDTO>) c.receive(socket);
                    respVerHorario.getBody().listarHorarios();

                    System.out.println("=================================================");
                    System.out.println("SELECIONE O HORARIO: ");
                    String horarioEscolhido = entrada.nextLine();

                    RequestEnt<Agendamento> requisicaoAgendar = new RequestEnt<>(MethodEnum.AGENDAR, new Agendamento(horarioEscolhido, matriculaDentista));

                    new Cliente();
                    c.send(socket, requisicaoAgendar);

                    ResponseEnt<Boolean> respAgendar =  (ResponseEnt<Boolean>) c.receive(socket);
                    if(respAgendar.getBody()) {
                        System.out.println("AGENDADO COM SUCESSO!");
                    } else {
                        throw new Exception("Algo de errado aconteceu!");
                    }
                }

                setTrocarUsuario(true);
                loop = true;
                break;
            case 2:
                System.out.println("=================================================");
                System.out.println("[1] Criar dentista [2] Excluir Dentista  [3] sair");
                System.out.println("=================================================");
                int opcao = entrada.nextInt();
                if(opcao == 1) {
                    System.out.println("Nome do dentista: ");
                    entrada = new Scanner(System.in);
                    String nome = entrada.nextLine();
                    System.out.println("Numero da matricula: ");
                    entrada = new Scanner(System.in);
                    int matricula = entrada.nextInt();

                    DentistaDTO novoDentista = new DentistaDTO(nome, matricula);
                    RequestEnt<DentistaDTO> req = new RequestEnt<>(MethodEnum.CRIAR, novoDentista);
                    new Cliente();
                    c.send(socket, req);
                    System.out.println("Enviei o dentista");

                    ResponseEnt<DentistaDTO> resp =  (ResponseEnt<DentistaDTO>) c.receive(socket);
                    if(resp.getStatus() == StatusEnum.SUCESSO) {
                        System.out.println("Cadastrado com sucesso!");
                        loop = true;
                    }
                }

                if(opcao == 2) {
                    System.out.println("Numero da matricula: ");
                    int matricula = entrada.nextInt();

                    RequestEnt<Integer> requisicaoLogin = new RequestEnt<>(MethodEnum.DELETAR, matricula);
                    new Cliente();
                    c.send(socket, requisicaoLogin);

                    ResponseEnt<Boolean> resp =  (ResponseEnt<Boolean>) c.receive(socket);
                    if(resp.getStatus() == StatusEnum.SUCESSO) {
                        System.out.println("Deletado com sucesso!");
                        loop = true;
                    }
                }

                if(opcao == 3) {
                    loop = true;
                    setTrocarUsuario(true);
                }
                break;
            default:
                throw new Exception("Opcao invalida");
        }
        return loop;
    }

    public static void setTrocarUsuario(boolean trocarUsuario) {
        Cliente.trocarUsuario = trocarUsuario;
    }

    public static void listarHorariosDentista(int mat) {
        RequestEnt<Integer> requisicaoVerHorario= new RequestEnt<>(MethodEnum.GET_DENTISTA, mat);

        new Cliente();
        c.send(socket, requisicaoVerHorario);


        ResponseEnt<DentistaDTO> respVerHorario =  (ResponseEnt<DentistaDTO>) c.receive(socket);
        respVerHorario.getBody().listarHorarios();
    }
}
