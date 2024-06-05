package demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Noeud {
    @Id
    private String hostname;

    public Noeud() {
    }

    public Noeud(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
