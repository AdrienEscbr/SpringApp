package demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.model.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

import java.util.List;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Random;

@Controller
public class LoadBalancer {
    private List<Worker> workers;
    private int index = 0;
    private final Random random = new Random();

    @PostMapping("/updateWorkers")
    public ResponseEntity<Void> updateWorkers(@RequestBody List<Worker> workers) {
        this.workers = workers;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/hi")
    public ResponseEntity<String> hello() {
        if (workers == null || workers.isEmpty()) {
            return new ResponseEntity<>("No active workers available", HttpStatus.SERVICE_UNAVAILABLE);
        }
        
        int attempts = 0;
        while (attempts < workers.size()) {
            this.index = random.nextInt(workers.size());
            String uri = "http://" + this.workers.get(this.index).getHostname() + ":8081/hello2";
            try {
                RestClient restClient = RestClient.create();
                String rw = restClient.get().uri(uri).retrieve().body(String.class);
                return new ResponseEntity<>(rw, HttpStatus.OK);
            } catch (Exception e) {
                attempts++;
            }
        }        
        return new ResponseEntity<>("All workers are unresponsive", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
