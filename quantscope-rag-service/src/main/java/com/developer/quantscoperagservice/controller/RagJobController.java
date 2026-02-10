package com.developer.quantscoperagservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.developer.quantscoperagservice.config.RagMqProperties;
import com.developer.quantscoperagservice.constant.RagJobStatus;
import com.developer.quantscoperagservice.model.dto.RagReportRequest;
import com.developer.quantscoperagservice.model.mq.RagJobRequestedMessage;
import com.developer.quantscoperagservice.service.RagJobEventService;
import com.developer.quantscoperagservice.service.RagJobTaskService;
import com.developer.quantscopecommon.entity.RagJobEvent;
import com.developer.quantscopecommon.entity.RagJobTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RAG job controller.
 */
@RestController
@RequestMapping("/openapi/rag")
public class RagJobController {

    private final RagJobTaskService ragJobTaskService;
    private final RagJobEventService ragJobEventService;
    private final RabbitTemplate rabbitTemplate;
    private final RagMqProperties ragMqProperties;
    private final ObjectMapper objectMapper;

    public RagJobController(RagJobTaskService ragJobTaskService,
                            RagJobEventService ragJobEventService,
                            RabbitTemplate rabbitTemplate,
                            RagMqProperties ragMqProperties,
                            ObjectMapper objectMapper) {
        this.ragJobTaskService = ragJobTaskService;
        this.ragJobEventService = ragJobEventService;
        this.rabbitTemplate = rabbitTemplate;
        this.ragMqProperties = ragMqProperties;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> createReport(
        @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
        @RequestHeader(value = "X-User-Id", required = false) Long userId,
        @RequestBody RagReportRequest request
    ) throws JsonProcessingException {

        if (tenantId == null || userId == null || request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "missing tenantId/userId"));
        }

        String jobId = UUID.randomUUID().toString().replace("-", "");
        String payload = objectMapper.writeValueAsString(request);

        RagJobTask task = new RagJobTask();
        task.setJobId(jobId);
        task.setTenantId(tenantId);
        task.setUserId(userId);
        task.setStatus(RagJobStatus.QUEUED);
        task.setRequestPayload(payload);
        task.setCreatedAt(java.util.Date.from(Instant.now()));
        task.setUpdatedAt(java.util.Date.from(Instant.now()));
        ragJobTaskService.save(task);

        RagJobEvent event = new RagJobEvent();
        event.setJobId(jobId);
        event.setEventType("REQUESTED");
        event.setPayload(payload);
        event.setCreatedAt(java.util.Date.from(Instant.now()));
        ragJobEventService.save(event);

        RagJobRequestedMessage message = new RagJobRequestedMessage();
        message.setJobId(jobId);
        message.setTenantId(tenantId);
        message.setUserId(userId);
        message.setPayload(payload);

        rabbitTemplate.convertAndSend(ragMqProperties.getExchange(), ragMqProperties.getRoutingKey(), message);

        Map<String, Object> response = new HashMap<>();
        response.put("jobId", jobId);
        response.put("status", RagJobStatus.QUEUED);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/report/{jobId}")
    public ResponseEntity<Map<String, Object>> getReport(@PathVariable String jobId) {
        if (!StringUtils.hasText(jobId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "missing jobId"));
        }

        RagJobTask task = ragJobTaskService.getOne(
            new LambdaQueryWrapper<RagJobTask>().eq(RagJobTask::getJobId, jobId)
        );
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "job not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("jobId", task.getJobId());
        response.put("status", task.getStatus());
        response.put("resultJson", task.getResultJson());
        response.put("resultPdfUrl", task.getResultPdfUrl());
        response.put("errorMsg", task.getErrorMsg());
        return ResponseEntity.ok(response);
    }
}
