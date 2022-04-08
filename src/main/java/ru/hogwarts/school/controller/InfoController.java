package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/port")
    public int getPort(){
        return serverPort;
    }

    @GetMapping("/int-value")
    public Integer getValue(){
        List<Integer> result = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .collect(Collectors.toList());

        return result.stream().parallel().mapToInt(Integer::intValue).sum();
    }
}
