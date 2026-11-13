package com.nimish.transport_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaneAssignmentDto {
  private Long laneId;
  private Long transporterId;
}