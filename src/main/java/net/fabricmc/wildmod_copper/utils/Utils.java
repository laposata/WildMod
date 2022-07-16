package net.fabricmc.wildmod_copper.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Utils {

  public static <A, B> Map<A, B> clone(Map<A, B> m){
    return m.entrySet().stream().map(Entry::copyOf).reduce(
      new HashMap<>(),
      ((map, e) ->  {
        Map<A,B> acc = Map.ofEntries(e);
        map.putAll(acc);
        return map;
      }),
      (a, b) -> {
        a.putAll(b);
        return a;
      });
  }

}
