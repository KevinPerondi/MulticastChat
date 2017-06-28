package CódigosProfessor;

public class Individuos {
    private String apelido;
    private String ip;

    public Individuos(String apelido, String ip) {
        this.apelido = apelido;
        this.ip = ip;
    }

    public Individuos() {
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
}
