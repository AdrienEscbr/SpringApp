package demo.controller;

import demo.model.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProxyUserRepository {
    public User findByLogin(String login){
        RestClient restClient = RestClient.create();

        User user = restClient.get()
            .uri("http://localhost:8081/users/{login}", login)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(User.class);

        return user;
    }
}
