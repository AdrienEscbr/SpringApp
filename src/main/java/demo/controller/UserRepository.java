package demo.controller;

import java.util.stream.Stream;
import org.springframework.data.repository.CrudRepository;
import demo.model.User;

public interface UserRepository extends CrudRepository<User, String> {
    User findByLogin(String login);

    Stream<User> streamAllBy();
}
