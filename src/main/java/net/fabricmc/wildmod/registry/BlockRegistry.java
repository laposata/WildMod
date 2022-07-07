package net.fabricmc.wildmod.registry;

import com.mojang.serialization.Lifecycle;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.wildmod.blocks.WildCopper;
import net.fabricmc.wildmod.resource.generators.SimpleBlockResourceGenerator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.OptionalInt;

import static net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry.registerOxidizableBlockPair;
import static net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry.registerWaxableBlockPair;
import static net.fabricmc.wildmod.WildModCopper.MINECRAFT_NAMESPACE;
import static net.fabricmc.wildmod.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod.registry.Tags.generateTagData;
import static net.minecraft.block.Blocks.*;

public class BlockRegistry {
    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(NAMESPACE+":"+"pack");
    public static final RuntimeResourcePack CLIENT_PACK = RuntimeResourcePack.create(NAMESPACE+":"+"datapack");

    public static final Block WILD_COPPER = new WildCopper(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.copy(COPPER_BLOCK));
    public static final Block WILD_COPPER_EXPOSED = new WildCopper(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.copy(EXPOSED_COPPER));
    public static final Block WILD_COPPER_WEATHERED = new WildCopper(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.copy(WEATHERED_COPPER));
    public static final Block WILD_COPPER_OXIDIZED = new WildCopper(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.copy(OXIDIZED_COPPER));
    public static final Block WAXED_WILD_COPPER = new WildCopper(Oxidizable.OxidationLevel.UNAFFECTED,AbstractBlock.Settings.copy(WILD_COPPER));
    public static final Block WAXED_WILD_COPPER_EXPOSED = new WildCopper(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.copy(WILD_COPPER_EXPOSED));
    public static final Block WAXED_WILD_COPPER_WEATHERED = new WildCopper(Oxidizable.OxidationLevel.WEATHERED,AbstractBlock.Settings.copy(WILD_COPPER_WEATHERED));
    public static final Block WAXED_WILD_COPPER_OXIDIZED = new WildCopper(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.copy(WILD_COPPER_OXIDIZED));

    private static void register(Block block, String identifier, ItemGroup group){
        Registry.register(Registry.BLOCK, new Identifier(NAMESPACE, identifier), block );
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, identifier), new BlockItem(block, new FabricItemSettings().group(group)) );
    }

    private static void registerOverride(Block block, String identifier, Block replacing){
        registerOverride(block, identifier, replacing, identifier);
    }

    private static void registerOverride(Block block, String identifier, Block replacing, String model){
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
    }

    private static void genClientResource(String identifier, Identifier model){
        Identifier registering =  new Identifier(NAMESPACE, identifier);
        SimpleBlockResourceGenerator.client(RESOURCE_PACK, registering, model);
    }

    private static void genLootTable(String identifier){
        SimpleBlockResourceGenerator.serverSimpleBlock(CLIENT_PACK, new Identifier(NAMESPACE, identifier));
    }

    public static void registerBlocks(){
        registerOverride(WILD_COPPER, "copper_block", COPPER_BLOCK);
        registerOverride(WILD_COPPER_EXPOSED, "exposed_copper", EXPOSED_COPPER);
        registerOverride(WILD_COPPER_WEATHERED, "weathered_copper", WEATHERED_COPPER);
        registerOverride(WILD_COPPER_OXIDIZED, "oxidized_copper", OXIDIZED_COPPER);
        registerOverride(WAXED_WILD_COPPER, "waxed_copper_block",  WAXED_COPPER_BLOCK, "copper_block");
        registerOverride(WAXED_WILD_COPPER_EXPOSED, "waxed_exposed_copper",  WAXED_EXPOSED_COPPER, "exposed_copper");
        registerOverride(WAXED_WILD_COPPER_WEATHERED, "waxed_weathered_copper",  WAXED_WEATHERED_COPPER, "weathered_copper");
        registerOverride(WAXED_WILD_COPPER_OXIDIZED, "waxed_oxidized_copper",  WAXED_OXIDIZED_COPPER, "oxidized_copper");

        registerOxidizableBlockPair(WILD_COPPER, WILD_COPPER_EXPOSED);
        registerOxidizableBlockPair(WILD_COPPER_EXPOSED, WILD_COPPER_WEATHERED);
        registerOxidizableBlockPair(WILD_COPPER_WEATHERED, WILD_COPPER_OXIDIZED);
        registerWaxableBlockPair(WILD_COPPER, WAXED_WILD_COPPER);
        registerWaxableBlockPair(WILD_COPPER_EXPOSED, WAXED_WILD_COPPER_EXPOSED);
        registerWaxableBlockPair(WILD_COPPER_WEATHERED, WAXED_WILD_COPPER_WEATHERED);
        registerWaxableBlockPair(WILD_COPPER_OXIDIZED, WAXED_WILD_COPPER_OXIDIZED);


        generateTagData(CLIENT_PACK);
        RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));
        RRPCallback.BEFORE_VANILLA.register(a -> a.add(CLIENT_PACK));
        CLIENT_PACK.dump();
    }
}
