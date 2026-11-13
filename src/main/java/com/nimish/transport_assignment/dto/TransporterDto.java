package com.nimish.transport_assignment.dto;

import lombok.Data;
import java.util.List;

@Data
public class TransporterDto {
  private Long id;
  private String name;
  private List<LaneQuoteDto> laneQuotes;
}
