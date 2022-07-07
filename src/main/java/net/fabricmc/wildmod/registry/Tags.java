package net.fabricmc.wildmod.registry;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.tags.JTag;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.wildmod.WildModCopper.NAMESPACE;
import static net.fabricmc.wildmod.registry.BlockRegistry.WAXED_WILD_COPPER;
import static net.fabricmc.wildmod.registry.BlockRegistry.WAXED_WILD_COPPER_EXPOSED;
import static net.fabricmc.wildmod.registry.BlockRegistry.WAXED_WILD_COPPER_OXIDIZED;
import static net.fabricmc.wildmod.registry.BlockRegistry.WAXED_WILD_COPPER_WEATHERED;
import static net.fabricmc.wildmod.registry.BlockRegistry.WILD_COPPER;
import static net.fabricmc.wildmod.registry.BlockRegistry.WILD_COPPER_EXPOSED;
import static net.fabricmc.wildmod.registry.BlockRegistry.WILD_COPPER_OXIDIZED;
import static net.fabricmc.wildmod.registry.BlockRegistry.WILD_COPPER_WEATHERED;
import static net.fabricmc.wildmod.utils.TagUtils.createTag;
import static net.minecraft.block.Blocks.*;


public class Tags {
    private static final String chargeable_copper = "chargeable_copper";
    private static final String conducts_copper = "conducts_copper";
    public static TagKey<Block> CHARGEABLE_COPPER = createTag(new Identifier(NAMESPACE, chargeable_copper));
    public static TagKey<Block> CONDUCTS_COPPER = createTag(new Identifier(NAMESPACE, conducts_copper));

    public static void generateTagData(RuntimeResourcePack pack){
        setChargeableCopper(pack);
        setConductsCopper(pack);
    }

    private static void setChargeableCopper(RuntimeResourcePack pack){
        pack.addTag(new Identifier(NAMESPACE, "blocks/" + chargeable_copper),
              new JTag()
                    .add(Registry.BLOCK.getId(WILD_COPPER))
                    .add(Registry.BLOCK.getId(WILD_COPPER_EXPOSED))
                    .add(Registry.BLOCK.getId(WILD_COPPER_WEATHERED))
                    .add(Registry.BLOCK.getId(WILD_COPPER_OXIDIZED))
                    .add(Registry.BLOCK.getId(WAXED_WILD_COPPER))
                    .add(Registry.BLOCK.getId(WAXED_WILD_COPPER_EXPOSED))
                    .add(Registry.BLOCK.getId(WAXED_WILD_COPPER_WEATHERED))
                    .add(Registry.BLOCK.getId(WAXED_WILD_COPPER_OXIDIZED)));

    }

    private static void setConductsCopper(RuntimeResourcePack pack){
        pack.addTag(new Identifier(NAMESPACE, "blocks/" + conducts_copper),
              new JTag()
                    .add(Registry.BLOCK.getId(REPEATER))
                    .add(Registry.BLOCK.getId(COMPARATOR))
                    .add(Registry.BLOCK.getId(REDSTONE_WALL_TORCH))
                    .add(Registry.BLOCK.getId(REDSTONE_TORCH))
                    .add(Registry.BLOCK.getId(REDSTONE_LAMP))
                    .add(Registry.BLOCK.getId(IRON_DOOR))
                    .add(Registry.BLOCK.getId(IRON_TRAPDOOR))
                    .add(Registry.BLOCK.getId(HOPPER))
                    .add(Registry.BLOCK.getId(NOTE_BLOCK))
                    .add(Registry.BLOCK.getId(DISPENSER))
                    .add(Registry.BLOCK.getId(DROPPER))
                    .add(Registry.BLOCK.getId(DRAGON_HEAD))
                    .add(Registry.BLOCK.getId(BELL))
                    .add(Registry.BLOCK.getId(PISTON))
                    .add(Registry.BLOCK.getId(STICKY_PISTON))
                    .add(Registry.BLOCK.getId(ACTIVATOR_RAIL))
                    .add(Registry.BLOCK.getId(POWERED_RAIL))
                    .add(Registry.BLOCK.getId(TNT)));
    }
}
