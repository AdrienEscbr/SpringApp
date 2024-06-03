package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import demo.model.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody User user) {
        if (loginService.doLogin(user.getLogin(), user.getPassword())) {
            return "OK";
        }
        return "KO";
    }

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> hello() {
        if (loginService.isLogged())
            return new ResponseEntity<>(loginService.hello(), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
