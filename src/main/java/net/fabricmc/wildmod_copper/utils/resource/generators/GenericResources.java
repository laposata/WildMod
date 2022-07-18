package net.fabricmc.wildmod_copper.utils.resource.generators;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.recipe.JIngredients;
import net.devtech.arrp.json.recipe.JKeys;
import net.devtech.arrp.json.recipe.JPattern;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.wildmod_copper.utils.TagUtils;
import net.minecraft.item.Item;
import net.minecraft.tag.TagBuilder;
import net.minecraft.tag.TagEntry;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.devtech.arrp.json.recipe.JRecipe.shaped;
import static net.devtech.arrp.json.recipe.JRecipe.shapeless;
import static net.devtech.arrp.json.recipe.JResult.stackedResult;
import static net.fabricmc.wildmod_copper.WildModCopper.MINECRAFT_NAMESPACE;
import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;

public class GenericResources {
  public static final RuntimeResourcePack SERVER_PACK = RuntimeResourcePack.create(NAMESPACE + ":" + "data_pack");
  public static final RuntimeResourcePack CLIENT_PACK = RuntimeResourcePack.create(NAMESPACE + ":" + "resource_pack");

  public static void export(){
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(CLIENT_PACK));
    SERVER_PACK.dump();
    CLIENT_PACK.dump();
  }

  public static void addItemModel(String textureName, String blockName){
    CLIENT_PACK.addData(
      new Identifier(NAMESPACE, "models/item/" + blockName+".json"),
      JHelper.serialize(Map.of("parent", textureName))
    );
  }

  public static void addBlockModel(String parent, String textureName, String block){
    CLIENT_PACK.addData(
      new Identifier(MINECRAFT_NAMESPACE, "models/block/" + block+".json"),
      JHelper.serialize(Map.of("parent", parent, "textures", Map.of("texture", textureName)))
    );
  }

  public static JTag createIngredientTag(List<Item> ingredients, String name){
    return null;//TODO recipe fix here
  }
  public static void shaplessCrafting(Identifier item, JIngredients ingredients, int count){
    SERVER_PACK.addRecipe(item,
      shapeless(
        ingredients,
        stackedResult(item.toString(), count)));
  }

  public static void crafting(Identifier recipeName, int count, JKeys keys, JPattern pattern){
    SERVER_PACK.addRecipe(recipeName,
      shaped(
        pattern,
        keys,
        stackedResult(recipeName.toString(), count)));
  }

}
