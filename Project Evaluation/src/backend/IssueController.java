package com.civicpulse.issue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/report")
    public ResponseEntity<Issue> reportIssue(@RequestBody Issue issue) {
        Issue savedIssue = issueRepository.save(issue);

        // Fire-and-forget Event-Driven Alert
        String eventMessage = "🚨 New Issue Reported: " + savedIssue.getTitle() + " at " + savedIssue.getLocation();
        kafkaTemplate.send("issue-alerts", eventMessage);

        return ResponseEntity.ok(savedIssue);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(issueRepository.findAll());
    }
}