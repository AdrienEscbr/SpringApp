package demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import demo.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserRepository usersRepo;

    @PostConstruct
    private void init() {
        User titi = new User("titi", "pass");
        User toto = new User("toto", "1234");
        usersRepo.save(titi);
        usersRepo.save(toto);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getUsers() {
        Stream<User> s = usersRepo.streamAllBy();
        return new ResponseEntity<>(s.toList(), HttpStatus.OK);
    }

    @GetMapping("/{login}")
    public ResponseEntity<User> get(@PathVariable("login") String login) {
        User user = usersRepo.findByLogin(login);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<User> put(@RequestBody User user) {
        User u = usersRepo.findByLogin(user.getLogin());
        if (u == null) {
            usersRepo.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }
}
