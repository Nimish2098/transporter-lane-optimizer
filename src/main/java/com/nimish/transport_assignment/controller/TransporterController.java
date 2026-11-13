package com.nimish.transport_assignment.controller;

import com.nimish.transport_assignment.dto.AssignmentRequestDto;
import com.nimish.transport_assignment.dto.AssignmentResponseDto;
import com.nimish.transport_assignment.dto.InputRequestDto;
import com.nimish.transport_assignment.service.TransporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transporters")
@RequiredArgsConstructor

public class TransporterController {

  private final TransporterService service;

  @PostMapping("/input")
  public Map<String, String> input(@RequestBody InputRequestDto request) {
    service.saveInput(request);
    Map<String, String> body = new LinkedHashMap<>();
    body.put("status", "success");
    body.put("message", "Input data saved successfully.");
    return body;
  }

  @PostMapping("/assignment")
  public AssignmentResponseDto assignment(@RequestBody AssignmentRequestDto request) {
    return service.assign(request.getMaxTransporters());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
    Map<String, String> body = new LinkedHashMap<>();
    body.put("status", "error");
    body.put("message", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }
}