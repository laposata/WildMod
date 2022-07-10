package net.fabricmc.wildmod_copper.registry;

import com.mojang.serialization.Lifecycle;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.wildmod_copper.imixin.IOverrideVanilla;
import net.fabricmc.wildmod_copper.resource.generators.SimpleBlockResourceGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;

import java.util.OptionalInt;

import static net.fabricmc.wildmod_copper.WildModCopper.MINECRAFT_NAMESPACE;
import static net.fabricmc.wildmod_copper.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod_copper.blocks.WaxedCopper.wax;
import static net.fabricmc.wildmod_copper.registry.Tags.generateTagData;
import static net.minecraft.block.Blocks.*;

public class BlockRegistry {
    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(NAMESPACE+":"+"pack");
    public static final RuntimeResourcePack CLIENT_PACK = RuntimeResourcePack.create(NAMESPACE+":"+"datapack");
    private static void register(Block block, String identifier, ItemGroup group){
        Registry.register(Registry.BLOCK, new Identifier(NAMESPACE, identifier), block );
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, identifier), new BlockItem(block, new FabricItemSettings().group(group)) );
    }

    private static Block registerOverride(Block replacing, String identifier, Block block){
        return registerOverride(replacing, identifier, block, identifier);
    }

    private static Block registerOverride(Block replacing, String identifier, Block block, String model){
        Identifier registering =  new Identifier(NAMESPACE, identifier);
        int rawId = Registry.BLOCK.getRawId(replacing);
        RegistryKey<Block> rkb = RegistryKey.of(Registry.BLOCK.getKey(), registering);
        Registry.BLOCK.replace(OptionalInt.of(rawId), rkb, block, Lifecycle.stable());

        int rawIdItem = Registry.ITEM.getRawId(replacing.asItem());
        RegistryKey<Item> rki = RegistryKey.of(Registry.ITEM.getKey(), registering);
        Registry.ITEM.replace(OptionalInt.of(rawIdItem),
              rki,
              new  BlockItem(block, new FabricItemSettings().group(replacing.asItem().getGroup())),
              Lifecycle.stable());

        genLootTable(identifier);
        genClientResource(identifier, new Identifier(MINECRAFT_NAMESPACE, model));
        return block;
    }

    private static void genClientResource(String identifier, Identifier model){
        Identifier registering =  new Identifier(NAMESPACE, identifier);
        SimpleBlockResourceGenerator.client(RESOURCE_PACK, registering, model);
    }

    private static void genLootTable(String identifier){
        SimpleBlockResourceGenerator.serverSimpleBlock(CLIENT_PACK, new Identifier(NAMESPACE, identifier));
    }

    public static void registerBlocks(){
        generateTagData(CLIENT_PACK);
        RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));
        RRPCallback.BEFORE_VANILLA.register(a -> a.add(CLIENT_PACK));
        CLIENT_PACK.dump();
    }
}
