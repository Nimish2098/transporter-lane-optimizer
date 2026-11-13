package com.nimish.transport_assignment.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Transporter {
  @Id
  private Long id;
  private String name;

  @ElementCollection
  @CollectionTable(name = "transporter_lane_quotes", joinColumns = @JoinColumn(name = "transporter_id"))
  private List<LaneQuote> laneQuotes;
}