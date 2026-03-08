package com.matrimony.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatEventListener {
    @EventListener
    @Transactional
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
      log.info("MatEventListener event {}",event);

    }
}
