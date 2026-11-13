package com.nimish.transport_assignment.service;

import java.util.*;

public class AssignmentSolver {

  public record Result(long totalCost, Map<Long, Long> laneToTransporter, List<Long> selected) {}

  // quotes: transporterId -> (laneId -> quote)
  public static Result solve(List<Long> laneIds,
                             Map<Long, Map<Long, Long>> quotes,
                             int maxTransporters) {
    List<Long> transporterIds = new ArrayList<>(quotes.keySet());
    int size = Math.min(maxTransporters, transporterIds.size());
    Result[] best = new Result[1];
    search(0, new ArrayList<>(), size, transporterIds, laneIds, quotes, best);
    return best[0]; // null means no valid assignment exists
  }

  private static void search(int idx, List<Long> chosen, int size,
                             List<Long> transporterIds, List<Long> laneIds,
                             Map<Long, Map<Long, Long>> quotes, Result[] best) {
    if (chosen.size() == size) {
      Result r = price(laneIds, quotes, chosen);
      if (r != null && (best[0] == null || r.totalCost() < best[0].totalCost())) {
        best[0] = r;
      }
      return;
    }
    if (idx == transporterIds.size()) return;
    if (chosen.size() + (transporterIds.size() - idx) < size) return;

    chosen.add(transporterIds.get(idx));
    search(idx + 1, chosen, size, transporterIds, laneIds, quotes, best);
    chosen.remove(chosen.size() - 1);
    search(idx + 1, chosen, size, transporterIds, laneIds, quotes, best);
  }

  private static Result price(List<Long> laneIds,
                              Map<Long, Map<Long, Long>> quotes,
                              List<Long> chosen) {
    long total = 0;
    Map<Long, Long> assignment = new LinkedHashMap<>();
    for (Long laneId : laneIds) {
      long bestQuote = Long.MAX_VALUE;
      Long bestTransporter = null;
      for (Long t : chosen) {
        Long q = quotes.get(t).get(laneId);
        if (q != null && q < bestQuote) {
          bestQuote = q;
          bestTransporter = t;
        }
      }
      if (bestTransporter == null) return null; // lane not covered
      total += bestQuote;
      assignment.put(laneId, bestTransporter);
    }
    List<Long> used = new ArrayList<>(new LinkedHashSet<>(assignment.values()));
    return new Result(total, assignment, used);
  }
}