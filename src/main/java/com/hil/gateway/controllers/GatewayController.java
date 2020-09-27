package com.hil.gateway.controllers;

import com.hil.gateway.factories.MSFactory;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequestMapping("/gateway")
public class GatewayController extends AbstractController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private MSFactory msFactory;

    @GetMapping("/{serviceName}/**")
    public ResponseEntity<?> get(HttpServletRequest request, @PathVariable("serviceName") String serviceName) {
        logger.trace("sending({})" + request);
        String MSURI = msFactory.findMicroService(serviceName) + request.getRequestURL().toString().split(serviceName)[1];

        return ResponseEntity.ok(webClientBuilder.build().get().uri(MSURI)
                .retrieve().bodyToMono(Object.class).block());
    }

    @PostMapping("/{serviceName}/**")
    public ResponseEntity<?> post(HttpServletRequest request, @RequestBody Object object, @PathVariable("serviceName") String serviceName) {
        logger.trace("sending({})", request);
        String MSURI = msFactory.findMicroService(serviceName) + request.getRequestURL().toString().split(serviceName)[1];

        return ResponseEntity.ok(webClientBuilder.build().post().uri(MSURI).bodyValue(object)
                .retrieve().bodyToMono(Object.class).block());
    }

    @PutMapping("/{serviceName}/**")
    public ResponseEntity<?> put(HttpServletRequest request, @PathVariable("serviceName") String serviceName, @RequestBody Object object) {
        logger.trace("sending({})" + request);
        String MSURI = msFactory.findMicroService(serviceName) + request.getRequestURL().toString().split(serviceName)[1];

        return ResponseEntity.ok(webClientBuilder.build().put().uri(MSURI).bodyValue(object)
                .retrieve().bodyToMono(Object.class).block());
    }

    @DeleteMapping("/{serviceName}/**")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable("serviceName") String serviceName) {
        logger.trace("sending({})" + request);
        String MSURI = msFactory.findMicroService(serviceName) + request.getRequestURL().toString().split(serviceName)[1];

        return ResponseEntity.ok(webClientBuilder.build().delete().uri(MSURI)
                .retrieve().bodyToMono(Object.class).block());
    }
}
