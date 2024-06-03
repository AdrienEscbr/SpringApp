package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.annotation.RequestScope;

import demo.model.User;

@Service
public class LoginService {
    @Autowired
    private ProxyUserRepository usersRepo;

    @Autowired
    private UserSession userSession;

    public boolean doLogin(String login, String password) {
        User user = usersRepo.findByLogin(login);
    
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }
        userSession.login = login;
        return true;
    }

    public boolean isLogged() {
        return userSession.login != null;
    }

    public String hello() {
        userSession.nbHello++;
        return "Hello " + userSession.login + ", pour la " + userSession.nbHello + "eme fois!";
    }
}
