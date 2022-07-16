package net.fabricmc.wildmod_copper.utils.resource.generators;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.util.Identifier;

import static net.devtech.arrp.json.blockstate.JState.state;
import static net.devtech.arrp.json.blockstate.JState.variant;
import static net.devtech.arrp.json.models.JModel.model;
import static net.devtech.arrp.json.models.JModel.textures;

public class SimpleBlockResourceGenerator {
  public static Identifier prefixPath(Identifier identifier, String prefix) {
    return new Identifier(identifier.getNamespace(), prefix + '/' + identifier.getPath());
  }

  private final RuntimeResourcePack pack;
  private Identifier id;
  private Identifier model;

  public SimpleBlockResourceGenerator(RuntimeResourcePack pack) {
    this.pack = pack;
  }

  SimpleBlockResourceGenerator setId(Identifier id) {
    this.id = id;
    return this;
  }

  SimpleBlockResourceGenerator setModel(Identifier model) {
    this.model = model;
    return this;
  }

  public static SimpleBlockResourceGenerator client(RuntimeResourcePack pack, Identifier id, Identifier model) {
    return new SimpleBlockResourceGenerator(pack)
             .setId(id)
             .setModel(model)
             .generateBlockState()
             .generateBlockModel()
             .generateItemModel();
  }

  public static SimpleBlockResourceGenerator serverSimpleBlock(RuntimeResourcePack pack, Identifier id) {
    return new SimpleBlockResourceGenerator(pack)
             .setId(id)
             .generateSimpleBlockLootTable();
  }

  public SimpleBlockResourceGenerator generateItemModel() {
    return generateItemModel(model, id);
  }

  public SimpleBlockResourceGenerator generateBlockState() {
    return generateBlockState(model, id);
  }

  public SimpleBlockResourceGenerator generateBlockModel() {
    return generateBlockModel(model, id);
  }

  public SimpleBlockResourceGenerator generateSimpleBlockLootTable() {
    return generateSimpleBlockLootTable(id);
  }

  public SimpleBlockResourceGenerator generateItemModel(Identifier model, Identifier id) {
    pack.addModel(JModel.model(prefixPath(model, "block").toString()), prefixPath(id, "item"));
    return this;
  }

  public SimpleBlockResourceGenerator generateBlockState(Identifier model, Identifier id) {
    pack.addBlockState(state(variant(JState.model(prefixPath(model, "block").toString()))), id);
    return this;
  }

  public SimpleBlockResourceGenerator generateBlockModel(Identifier model, Identifier id) {
    Identifier prefixModel = prefixPath(model, "block");
    Identifier prefixTexture = prefixPath(id, "block");
    pack.addModel(model("block/cube_all").textures(textures().var("all", prefixModel.toString())), prefixTexture);
    return this;
  }

  public SimpleBlockResourceGenerator generateSimpleBlockLootTable(Identifier id) {
    pack.addLootTable(id,
      JLootTable.loot("minecraft:block")
        .pool(JLootTable.pool()
                .rolls(1)
                .entry(JLootTable.entry()
                         .type("minecraft:item")
                         .name(id.toString()))
                .condition(JLootTable.condition("minecraft:survives_explosion"))));
    return this;
  }
}

