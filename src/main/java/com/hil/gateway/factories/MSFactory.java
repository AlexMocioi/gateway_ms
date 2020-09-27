package com.hil.gateway.factories;

import org.springframework.stereotype.Component;

@Component
public class MSFactory {

    private static final String dataMSURI = "http://localhost:8081/";
    private static final String notificationMSURI = "http://localhost:8082/";

    public String findMicroService(String serviceName) {
        switch (serviceName) {
            case "data":
                return dataMSURI + serviceName;
            case "notification":
                return notificationMSURI + serviceName;
        }
        return null;
    }
}
