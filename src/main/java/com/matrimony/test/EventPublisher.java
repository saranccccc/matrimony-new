package com.matrimony.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void initEvent(){
        log.info(" EventPublisher - Initiating the event start");
        eventPublisher.publishEvent(PaymentSuccessEvent.builder().referenceType("TEST").build());
        log.info(" EventPublisher - Initiating the event complete");
    }
}
