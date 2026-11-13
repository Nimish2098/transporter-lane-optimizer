package com.nimish.transport_assignment.service;

import com.nimish.transport_assignment.dto.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransporterService {

  // The in-memory store: the latest input replaces the previous one
  private volatile InputRequestDto data;

  public void saveInput(InputRequestDto input) {
    if (input == null || input.getLanes() == null || input.getLanes().isEmpty()
        || input.getTransporters() == null || input.getTransporters().isEmpty()) {
      throw new IllegalArgumentException("Lanes and transporters must not be empty.");
    }

    Set<Long> laneIds = new HashSet<>();
    for (LaneDto l : input.getLanes()) {
      if (!laneIds.add(l.getId())) {
        throw new IllegalArgumentException("Duplicate lane id: " + l.getId());
      }
    }
    for (TransporterDto t : input.getTransporters()) {
      if (t.getLaneQuotes() == null) continue;
      for (LaneQuoteDto q : t.getLaneQuotes()) {
        if (!laneIds.contains(q.getLaneId())) {
          throw new IllegalArgumentException("Quote refers to unknown lane id: " + q.getLaneId());
        }
        if (q.getQuote() == null || q.getQuote() < 0) {
          throw new IllegalArgumentException("Invalid quote for lane " + q.getLaneId());
        }
      }
    }

    this.data = input; // replaces any previous data
  }

  public AssignmentResponseDto assign(int maxTransporters) {
    if (maxTransporters < 1) {
      throw new IllegalArgumentException("maxTransporters must be at least 1.");
    }
    InputRequestDto d = this.data;
    if (d == null) {
      throw new IllegalArgumentException("No input data found. Call the input API first.");
    }

    List<Long> laneIds = d.getLanes().stream()
                          .map(LaneDto::getId).sorted().collect(Collectors.toList());

    Map<Long, Map<Long, Long>> quotes = new LinkedHashMap<>();
    for (TransporterDto t : d.getTransporters()) {
      Map<Long, Long> byLane = new HashMap<>();
      if (t.getLaneQuotes() != null) {
        for (LaneQuoteDto q : t.getLaneQuotes()) {
          byLane.merge(q.getLaneId(), q.getQuote(), Math::min);
        }
      }
      quotes.put(t.getId(), byLane);
    }

    AssignmentSolver.Result result = AssignmentSolver.solve(laneIds, quotes, maxTransporters);
    if (result == null) {
      throw new IllegalArgumentException(
          "No valid assignment: every lane cannot be covered with at most "
          + maxTransporters + " transporters.");
    }

    AssignmentResponseDto response = new AssignmentResponseDto();
    response.setStatus("success");
    response.setTotalCost(result.totalCost());
    response.setAssignments(result.laneToTransporter().entrySet().stream()
                                  .map(e -> new LaneAssignmentDto(e.getKey(), e.getValue()))
                                  .collect(Collectors.toList()));
    response.setSelectedTransporters(result.selected());
    return response;
  }
}