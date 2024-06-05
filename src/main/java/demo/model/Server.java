package demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import demo.controller.NoeudRepository;

@RestController
@RequestMapping("/server")
public class Server {
    @Autowired
    private NoeudRepository noeudRepo;

    @PostMapping("/launch/{service}")
    public ResponseEntity<String> launchService(@PathVariable String service, @RequestParam int nbworkers) {
        List<Noeud> noeuds = noeudRepo.findAll();
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
                restTemplate.postForEntity(uri, /* configuration du worker */, String.class);
            }
        }

        return new ResponseEntity<>("Service launched", HttpStatus.OK);
    }
}
