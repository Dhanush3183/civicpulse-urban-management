package com.civicpulse.issue;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IssueEventListener {

    @KafkaListener(topics = "issue-alerts", groupId = "civicpulse-group")
    public void listenToIssueAlerts(String message) {
        System.out.println("\n========================================================");
        System.out.println(" KAFKA EVENT CONSUMED SUCCESSFULLY ");
        System.out.println(" " + message);
        System.out.println("========================================================\n");
    }
}