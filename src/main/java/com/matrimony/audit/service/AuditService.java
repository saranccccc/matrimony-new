package com.matrimony.audit.service;

import org.springframework.stereotype.Service;

@Service
public class AuditService {

    public void log(String entity,
                    String entityId,
                    String event,
                    String status) {
        // Async write to DynamoDB
    }
}
