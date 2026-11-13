package com.nimish.transport_assignment.dto;

import lombok.Data;
import java.util.List;

@Data
public class InputRequestDto {
  private List<LaneDto> lanes;
  private List<TransporterDto> transporters;
}