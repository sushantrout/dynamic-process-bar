package com.tech.controller;

import com.tech.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/processor")
@Slf4j
public class ProcessorController {
    @GetMapping(produces = "text/event-stream")
    public Flux<Map<String, Integer>> getProcessor() {
        Map<String, Integer> response = new HashMap<>();
        response.put("total", 100);
        return Flux.create(fluxSink -> {
            for (int i = 1; i <= 100; i++) {
                Employee employee = new Employee(i, "Employee " + i);
                log.error(employee.toString());
                response.put("current", i);
                fluxSink.next(response);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fluxSink.complete();
        });
    }
}
