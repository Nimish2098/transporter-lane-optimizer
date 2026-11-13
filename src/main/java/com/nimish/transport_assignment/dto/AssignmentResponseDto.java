package com.nimish.transport_assignment.dto;

import lombok.Data;
import java.util.List;

@Data
public class AssignmentResponseDto {
  private String status;
  private Long totalCost;
  private List<LaneAssignmentDto> assignments;
  private List<Long> selectedTransporters;
}