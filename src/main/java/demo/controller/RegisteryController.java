package demo.controller;

import demo.model.Worker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/workers")
public class RegisteryController {
    @Autowired
    private WorkerRepository workersRepo;
    private ConcurrentHashMap<String, Long> workerLastSeen = new ConcurrentHashMap<>();

    @Transactional
    @GetMapping
    public ResponseEntity<List<Worker>> getWorkers() {
        Stream<Worker> s = workersRepo.streamAllBy();
        return new ResponseEntity<>(s.filter(Worker::isActif).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Worker> registerWorker(@RequestBody Worker worker) {
        workersRepo.save(worker);
        workerLastSeen.put(worker.getHostname(), System.currentTimeMillis());
        return new ResponseEntity<>(worker, HttpStatus.OK);
    }

    @Scheduled(fixedRate = 60000) // 1 minute
    public void checkWorkerStatus() {
        long currentTime = System.currentTimeMillis();
        workersRepo.streamAllBy().forEach(worker -> {
            if (currentTime - workerLastSeen.getOrDefault(worker.getHostname(), 0L) > 120000) { // 2 minutes
                worker.setActif(false);
                workersRepo.save(worker);
            }
        });
        notifyLoadBalancer();
    }

    private void notifyLoadBalancer() {
        RestTemplate restTemplate = new RestTemplate();
        List<Worker> activeWorkers = workersRepo.streamAllBy().filter(Worker::isActif).collect(Collectors.toList());
        restTemplate.postForEntity(
                "http://loadbalancer:8080/updateWorkers",
                activeWorkers,
                String.class
        );
    }
}
