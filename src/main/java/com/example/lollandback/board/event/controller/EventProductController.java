package com.example.lollandback.board.event.controller;

import com.example.lollandback.board.event.service.EventProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eventProduct")
public class EventProductController {
    private final EventProductService service;

    @GetMapping("/list")
    public Map<String, Object> getEventProduct(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                               @RequestParam(value = "k", defaultValue = "") String keyword,
                                               @RequestParam(value = "c", defaultValue = "all") String category) {
        return service.getEventProduct(page, keyword, category);
    }

}
