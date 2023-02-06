package com.github.exobite.mc.simplespawners.util;

import com.github.exobite.mc.simplespawners.PluginMaster;
import com.github.exobite.mc.simplespawners.util.Version;
import com.github.exobite.mc.simplespawners.util.VersionHelper;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;

public enum SpawnableEntity {

    ALLAY(),
    AXOLOTL(EntityType.AXOLOTL, ""),
    BAT(EntityType.BAT, ""),
    BEE(EntityType.BEE, ""),
    BLAZE(EntityType.BLAZE, ""),
    CAT(EntityType.CAT, ""),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, ""),
    CHICKEN(EntityType.CHICKEN, ""),
    COD(EntityType.COD, ""),
    COW(EntityType.COW, ""),
    CREEPER(EntityType.CREEPER, ""),
    DOLPHIN(EntityType.DOLPHIN, ""),
    DONKEY(EntityType.DONKEY, ""),
    DROWNED(EntityType.DROWNED, ""),
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, ""),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, ""),
    ENDERMAN(EntityType.ENDERMAN, ""),
    ENDERMITE(EntityType.ENDERMITE, ""),
    EVOKER(EntityType.EVOKER, ""),
    FIREBALL(EntityType.FIREBALL, ""),
    FIREWORK_ROCKET(EntityType.FIREWORK, ""),
    FOX(EntityType.FOX, ""),
    FROG(),
    GHAST(EntityType.GHAST, ""),
    GIANT(EntityType.GIANT, ""),
    GLOW_SQUID(EntityType.GLOW_SQUID, ""),
    GOAT(EntityType.GOAT, ""),
    GUARDIAN(EntityType.GUARDIAN, ""),
    HOGLIN(EntityType.HOGLIN, ""),
    HORSE(EntityType.HORSE, ""),
    HUSK(EntityType.HUSK, ""),
    ILLUSIONER(EntityType.ILLUSIONER, ""),
    IRON_GOLEM(EntityType.IRON_GOLEM, ""),
    LLAMA(EntityType.LLAMA, ""),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, ""),
    MOOSHROOM(EntityType.MUSHROOM_COW, ""),
    MULE(EntityType.MULE, ""),
    OCELOT(EntityType.OCELOT, ""),
    PANDA(EntityType.PANDA, ""),
    PARROT(EntityType.PARROT, ""),
    PHANTOM(EntityType.PHANTOM, ""),
    PIG(EntityType.PIG, ""),
    PIGLIN(EntityType.PIGLIN, ""),
    PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE, ""),
    PILLAGER(EntityType.PILLAGER, ""),
    POLAR_BEAR(EntityType.POLAR_BEAR, ""),
    PUFFERFISH(EntityType.PUFFERFISH, ""),
    RABBIT(EntityType.RABBIT, ""),
    RAVAGER(EntityType.RAVAGER, ""),
    SALMON(EntityType.SALMON, ""),
    SHEEP(EntityType.SHEEP, ""),
    SHULKER(EntityType.SHULKER, ""),
    SILVERFISH(EntityType.SILVERFISH, ""),
    SKELETON(EntityType.SKELETON, ""),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, ""),
    SLIME(EntityType.SLIME, ""),
    SNOW_GOLEM(EntityType.SNOWMAN, ""),
    SPIDER(EntityType.SPIDER, ""),
    SQUID(EntityType.SQUID, ""),
    STRAY(EntityType.STRAY, ""),
    TADPOLE(),
    TNT(EntityType.PRIMED_TNT, ""),
    TRADER_LLAMA(EntityType.TRADER_LLAMA, ""),
    TROPICAL_FISH(EntityType.TROPICAL_FISH, ""),
    TURTLE(EntityType.TURTLE, ""),
    VEX(EntityType.VEX, ""),
    VILLAGER(EntityType.VILLAGER, ""),
    VINDICATOR(EntityType.VINDICATOR, ""),
    WANDERING_TRADER(EntityType.WANDERING_TRADER, ""),
    WARDEN(),
    WITCH(EntityType.WITCH, ""),
    WITHER(EntityType.WITHER, ""),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, ""),
    WOLF(EntityType.WOLF, ""),
    ZOGLIN(EntityType.ZOGLIN, ""),
    ZOMBIE(EntityType.ZOMBIE, ""),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, ""),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, ""),
    ZOMBIEFIED_PIGLIN();

    private static int validAmount;

    public static void init() {
        Version currV = VersionHelper.getBukkitVersion();
        //1.19
        if(VersionHelper.isEqualOrLarger(currV, new Version(1, 19, 0))) {
            PluginMaster.sendConsoleMessage(Level.INFO, "Adding MC1.19 Mobs...");
            ALLAY.setData(EntityType.ALLAY, "");
            FROG.setData(EntityType.FROG, "");
            TADPOLE.setData(EntityType.TADPOLE, "");
            WARDEN.setData(EntityType.WARDEN, "");
            ZOMBIEFIED_PIGLIN.setData(EntityType.ZOMBIFIED_PIGLIN, "");
        }
    }

    private static void countValidUp() {
        validAmount++;
    }

    public static int getValidAmount() {
        return validAmount;
    }

    private String texture;
    private EntityType type;
    private boolean isValid;

    SpawnableEntity() {
        this.isValid = false;
    }

    SpawnableEntity(EntityType type, String texture) {
        this.texture = texture;
        this.type = type;
        this.isValid = true;
        countValidUp();
    }

    private void setData(EntityType type, String texture) {
        this.texture = texture;
        this.type = type;
        this.isValid = true;
        countValidUp();
    }

    public boolean isValid() {
        return isValid;
    }

    public String getTexture() {
        return texture;
    }

    public EntityType getType() {
        return type;
    }

    public ItemStack getItemStack() {
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        assert sm != null;
        GameProfile gp = new GameProfile(UUID.randomUUID(), null);
        gp.getProperties().put("textures", new Property("textures", texture));
        try {
            Field f = sm.getClass().getDeclaredField("profile");
            f.setAccessible(true);
            f.set(sm, gp);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        is.setItemMeta(sm);
        return is;
    }

}
