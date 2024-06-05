package demo.controller;

import demo.model.Noeud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/server")
public class Server {
    @Autowired
    private NoeudRepository noeudRepo;

    @PostMapping("/launch/{service}")
    public ResponseEntity<String> launchService(@PathVariable String service, @RequestParam int nbworkers) {
        Iterable<Noeud> noeudsIterable = noeudRepo.findAll();
        List<Noeud> noeuds = StreamSupport.stream(noeudsIterable.spliterator(), false).collect(Collectors.toList());
        if (noeuds.isEmpty()) {
            return new ResponseEntity<>("No available nodes", HttpStatus.SERVICE_UNAVAILABLE);
        }

        RestTemplate restTemplate = new RestTemplate();
        int workersPerNode = nbworkers / noeuds.size();
        int remainingWorkers = nbworkers % noeuds.size();

        for (Noeud noeud : noeuds) {
            int workersToLaunch = workersPerNode + (remainingWorkers > 0 ? 1 : 0);
            remainingWorkers--;

            for (int i = 0; i < workersToLaunch; i++) {
                String uri = "http://" + noeud.getHostname() + ":2375/containers/create";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Replace with your actual worker configuration JSON
                String workerConfig = "{ \"Image\": \"gestiondeprojet-loginapp:latest\", \"ExposedPorts\": { \"8081/tcp\": {} } }";

                HttpEntity<String> request = new HttpEntity<>(workerConfig, headers);
                restTemplate.postForEntity(uri, request, String.class);
            }
        }

        return new ResponseEntity<>("Service launched", HttpStatus.OK);
    }
}
