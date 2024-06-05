package demo.controller;

import demo.model.Noeud;
import org.springframework.data.repository.CrudRepository;

public interface NoeudRepository extends CrudRepository<Noeud, String> {
}
