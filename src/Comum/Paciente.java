
package Comum;

//Classe de Paciente,com associação do nome
public class Paciente {
    public Paciente(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
