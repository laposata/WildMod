package net.fabricmc.wildmod_copper.utils.resource.generators;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.fabricmc.wildmod_copper.utils.Utils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToxBlockState{

  List<State> states;
  public ToxBlockState(){
    states = new ArrayList<>();
    states.add(new State());
  }

  public ToxBlockState set(State properties, String key, Object value){
    states.stream().filter(s -> s.similar(properties)).forEach(s -> s.set(key, value));
    return this;
  }
  public ToxBlockState setUnless(State properties, String key, Object value){
    states.stream().filter(s -> !s.similar(properties)).forEach(s -> s.set(key, value));
    return this;
  }

  public ToxBlockState addState(String name, String ... values){
    states = states.stream()
               .flatMap(s -> s.addState(name, values).stream())
               .collect(Collectors.toList());
    return this;
  }

  /**
   * adds states for values of integers inclusive
   * @param name name of property
   * @param s start inclusive
   * @param e end inclusive
   * @return
   */
  public ToxBlockState addState(String name, int s, int  e){
    String[] values = Stream.iterate(s, i -> i <= e, i -> ++i).map(i -> Integer.toString(i)).toArray(String[]::new);
    states = states.stream()
               .flatMap(state -> state.addState(name, values).stream())
               .collect(Collectors.toList());
    return this;
  }

  private Map<String, Map<String, Object>> compress(){
    return states.stream().collect(Collectors.toMap(State::getStates, s-> s.body));
  }

  public record State(Map<String, String> states,  Map<String, Object> body){
    public State(Map<String, String> states){
      this(states, new HashMap<>());
    }
    public State(){
      this(new HashMap<>());
    }
    @Override
    public State clone(){
      return new State(Utils.clone(states), Utils.clone(body));
    }

    public State addState(String name, String value){
      states.put(name, value);
      return this;
    }

    public List<State> addState(String name, String ... values){
      return Arrays.stream(values).map( v -> this.clone().addState(name, v)).collect(Collectors.toList());
    }

    public boolean similar(State other){
      return other.states.entrySet().stream().allMatch( e -> {
        if(!states.containsKey(e.getKey())){
          throw new IllegalArgumentException(e.getKey()+" is not a valid state key");
        }
        return states.get(e.getKey()).equals(e.getValue());
      });
    }

    public void set(String key, Object value){
      body.put(key, value);
    }

    public String getStates(){
      return states.entrySet()
               .stream()
               .map(e -> e.getKey()+"="+e.getValue())
               .collect(Collectors.joining(","));
    }
  }

  public static class Serializer implements JsonSerializer<ToxBlockState> {
    @Override
    public JsonElement serialize(final ToxBlockState src, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
      return context.serialize(Map.of("variants", src.compress()));
    }
  }


}
