package com.nimish.transport_assignment.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class LaneQuote {
  private Long laneId;
  private Long quote;
}