package Cliente;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

import Comum.*;


//Declaração da classe Cliente
public class Cliente {


    //Variáveis para conexão com o servidor
    static Conexao c;
    static Socket socket;

    //Flag para troca de Usuário (Paciente,Dentista ou Administrador)
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
        
        //Criando um menu com opções para cada tipo de usuário
        Scanner entrada =  new Scanner(System.in);
        int  tipoUsuario;
        boolean loop = true; //Flag para loop de Usuário


        //Sistema para troca de usuário
        System.out.println("Voce eh: [0] Dentista [1] Paciente [2] Administrador");
        tipoUsuario = entrada.nextInt();
        while(loop) {
            if(trocarUsuario) { //Caso quiser trocar de usuário, mostra o menu novamente
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
            
            //Switch para detecção de cada tipo de usuário
            case 0: //Caso Usuário --> Dentista

                //Inserir a Matrícula para detectar o Login, precisa existir um dentista criado no sistema
                System.out.println("Digite sua matricula: ");
                int mat = entrada.nextInt();

                //Criando um Request para detectar se existe no sistema
                RequestEnt<Integer> requisicao = new RequestEnt<>(MethodEnum.LOGIN_DENT, mat);
                new Cliente();
                c.send(socket, requisicao);


                ResponseEnt<Void> resp1 =  (ResponseEnt<Void>) c.receive(socket);

                // caso não exista a matricula, retorna uma exceção
                if(resp1.getStatus() == StatusEnum.ERRO) {
                    throw new Exception("Matricula Invalida!");
                }

                //Loop para menu do dentista
                boolean flagDentista = true;
                while(flagDentista) {
                    System.out.println("===================================================");
                    System.out.println("[1] Listar Horarios  [2] Cancelar Consulta [3] sair");
                    System.out.println("===================================================");
                    int opcaoDentista = entrada.nextInt();

                    //Mostra todos os horários do dentista
                    if(opcaoDentista == 1) {
                        listarHorariosDentista(mat);
                    }

                    //Lista os horários e cancela a consulta, tornando um horário indisponivel disponivel novamente
                    if(opcaoDentista == 2) {
                        listarHorariosDentista(mat);
                        entrada = new Scanner(System.in);
                        System.out.println("Qual horario voce deseja cancelar?"); //Seleção do horário
                        String horarioEscolhido = entrada.nextLine();
                        entrada = new Scanner(System.in);

                        RequestEnt<Agendamento> requisicaoCancelar = new RequestEnt<>(MethodEnum.CANCELAR, new Agendamento(horarioEscolhido, mat));

                        new Cliente();
                        c.send(socket, requisicaoCancelar);

                        //Envio da Requisição e recepção da resposta
                        ResponseEnt<Boolean> respCancelar =  (ResponseEnt<Boolean>) c.receive(socket);

                        //Caso seja cancelado retorna mensagem, e lista horários
                        if(respCancelar.getBody()) {
                            System.out.println("CANCELADO COM SUCESSO!");
                            listarHorariosDentista(mat);
                        } else {
                            throw new Exception("Algo de errado aconteceu!");
                        }
                    }


                    //Opção para sair do loop e voltar para o menu de seleção de usuário
                    if(opcaoDentista == 3) {
                        flagDentista = false;
                        setTrocarUsuario(true);
                    }
                }


                loop = true;
                break;
            
            //Loop do Paciente
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

                //Agendamento de consulta
                if(opcaoPaciente == 1) {
                    //Detecta a matrícula do dentista 
                    System.out.println("Qual a matricula do dentista que voce quer agendar?"); 
                    entrada = new Scanner(System.in);
                    int matriculaDentista = entrada.nextInt();
                    entrada = new Scanner(System.in);

                    //Enviando a requisição
                    RequestEnt<Integer> requisicaoGetDentista = new RequestEnt<>(MethodEnum.GET_DENTISTA, matriculaDentista);
                    new Cliente();
                    c.send(socket, requisicaoGetDentista);


                    //Lista os horários do dentista
                    ResponseEnt<DentistaDTO> respVerHorario =  (ResponseEnt<DentistaDTO>) c.receive(socket);
                    respVerHorario.getBody().listarHorarios();


                    //Seleção do horário
                    System.out.println("=================================================");
                    System.out.println("SELECIONE O HORARIO: "); //Formato HH:MM
                    String horarioEscolhido = entrada.nextLine();

                    //Enviando a requisição do agendamento 
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

                //Troca de usuário
                setTrocarUsuario(true);
                loop = true;
                break;
            case 2: //Loop do Administrador
                System.out.println("=================================================");
                System.out.println("[1] Criar dentista [2] Excluir Dentista  [3] sair");
                System.out.println("=================================================");
                int opcao = entrada.nextInt();

                //Criando dentista dentro do sistema
                if(opcao == 1) {
                    //Adicionando nome e Matricula
                    System.out.println("Nome do dentista: ");
                    entrada = new Scanner(System.in);
                    String nome = entrada.nextLine();

                    System.out.println("Numero da matricula: ");
                    entrada = new Scanner(System.in);
                    int matricula = entrada.nextInt();

                    //Criando novo objeto Dentista
                    DentistaDTO novoDentista = new DentistaDTO(nome, matricula);

                    //Requisição de criação 
                    RequestEnt<DentistaDTO> req = new RequestEnt<>(MethodEnum.CRIAR, novoDentista);
                    new Cliente();
                    c.send(socket, req);
                    System.out.println("Criei o dentista");

                    ResponseEnt<DentistaDTO> resp =  (ResponseEnt<DentistaDTO>) c.receive(socket);
                    if(resp.getStatus() == StatusEnum.SUCESSO) {
                        System.out.println("Cadastrado com sucesso!");
                        loop = true;
                    }
                }

                //Removendo dentista do Sistema
                if(opcao == 2) {
                    
                    //Inserção da Matricula
                    System.out.println("Numero da matricula: ");
                    int matricula = entrada.nextInt();

                    //Requisição de Deletar
                    RequestEnt<Integer> requisicaoLogin = new RequestEnt<>(MethodEnum.DELETAR, matricula);
                    new Cliente();
                    c.send(socket, requisicaoLogin);

                    ResponseEnt<Boolean> resp =  (ResponseEnt<Boolean>) c.receive(socket);
                    if(resp.getStatus() == StatusEnum.SUCESSO) {
                        System.out.println("Deletado com sucesso!");
                        loop = true;
                    } 
                }

                //Sair do Loop
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

        //Retorna os horários dentro da lista acossiada com o Dentista esccolhhido
        RequestEnt<Integer> requisicaoVerHorario= new RequestEnt<>(MethodEnum.GET_DENTISTA, mat);

        new Cliente();
        c.send(socket, requisicaoVerHorario);


        ResponseEnt<DentistaDTO> respVerHorario =  (ResponseEnt<DentistaDTO>) c.receive(socket);
        respVerHorario.getBody().listarHorarios();
    }
}
