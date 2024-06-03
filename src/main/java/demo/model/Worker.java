package demo.model;
import jakarta.persistence.*;

@Entity
public class Worker {
    @Id
    private String hostname;
    private boolean actif;

    public Worker() {
    }

    public Worker(String hostname) {
        this.hostname = hostname;
        this.actif = true;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
