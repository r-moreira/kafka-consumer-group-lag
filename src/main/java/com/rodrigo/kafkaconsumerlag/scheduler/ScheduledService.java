package com.rodrigo.kafkaconsumerlag.scheduler;

import com.rodrigo.kafkaconsumerlag.service.ConsumerLagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledService.class);

    @Autowired
    private ConsumerLagService consumerLag = new ConsumerLagService();

    @Scheduled(cron ="*/10 * * * * *")
    private void run() {
        LOGGER.info("### Starting to get kafka metadata");
        consumerLag.execute();
        LOGGER.info("### Finishing to persist kafka metadata on elastic");
    }

}
