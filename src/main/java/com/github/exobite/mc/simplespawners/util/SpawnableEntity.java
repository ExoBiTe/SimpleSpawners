package com.github.exobite.mc.simplespawners.util;

import com.github.exobite.mc.simplespawners.PluginMaster;
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
    AXOLOTL(EntityType.AXOLOTL,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI5MWIxM2Q" +
                    "wYjMzNjA3YTk1ODdlOGJhMTdkODYwMzU2MjVjYjM2MjdjNWRiZWY5Y2YwMGU4YmFlOWYwZjUyYSJ9fX0="),
    BAT(EntityType.BAT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY4MWE3MmR" +
                    "hNzI2M2NhOWFlZjA2NjU0MmVjY2E3YTE4MGM0MGUzMjhjMDQ2M2ZjYjExNGNiM2I4MzA1NzU1MiJ9fX0="),
    BEE(EntityType.BEE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcxMGY5YWE" +
                    "zMjZjNzIwNTAzODM5MmFlYjEyZjQ1Y2UyMmNmNmExMzM2YjgyMTI5NjY4YmVhMWZjNDZkNDI4NSJ9fX0="),
    BLAZE(EntityType.BLAZE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIwNjU3ZTI" +
                    "0YjU2ZTFiMmY4ZmMyMTlkYTFkZTc4OGMwYzI0ZjM2Mzg4YjFhNDA5ZDBjZDJkOGRiYTQ0YWEzYiJ9fX0="),
    CAT(EntityType.CAT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkyYWQ2ZmE" +
                    "0MzhlYWMxYWI5ZDQ0NzAxOGIxZGQ2MmRjNjRhYjIyZWMxNjU0NWNmNmYwZjc3YTVlOTk3OGNkOCJ9fX0="),
    CAVE_SPIDER(EntityType.CAVE_SPIDER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWNjYzRhMzJ" +
                    "kNDVkNzRlOGIxNGVmMWZmZDU1Y2Q1ZjM4MWEwNmQ0OTk5MDgxZDUyZWFlYTEyZTEzMjkzZTIwOSJ9fX0="),
    CHICKEN(EntityType.CHICKEN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2EzNTgyY2U" +
                    "0ODg5MzMzZGFkMzI5ZTRlMjQzNzJhMDNhNWRhYTJjMzQyODBjNTYyNTZhZjUyODNlZGIwNDNmOCJ9fX0="),
    COD(EntityType.COD,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg5MmQ3ZGQ" +
                    "2YWFkZjM1Zjg2ZGEyN2ZiNjNkYTRlZGRhMjExZGY5NmQyODI5ZjY5MTQ2MmE0ZmIxY2FiMCJ9fX0="),
    COW(EntityType.COW,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY1NTE4NDA" +
                    "5NTVmNTI0MzY3NTgwZjExYjM1MjI4OTM4YjY3ODYzOTdhOGYyZThjOGNjNmIwZWIwMWI1ZGIzZCJ9fX0="),
    CREEPER(EntityType.CREEPER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJmZDc2M2V" +
                    "hY2NiZjk3OGJmYWYxOTVkNjVlNzU2NjcwOTc1NTliOTIzOTM5MmNlOTE0MDZmOTU0YzA3ZWVlNCJ9fX0="),
    DOLPHIN(EntityType.DOLPHIN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk" +
                    "1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ=="),
    DONKEY(EntityType.DONKEY,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NGJkYWQ" +
                    "2YmMxOGEzNzE2YjE5NmRjNGE0YmQ2OTUyNjVlY2NhYWRkMGQ5NDViZWI5NzY0NDNmODI2OTNiIn19fQ=="),
    DROWNED(EntityType.DROWNED,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg0ZGY3OWM" +
                    "0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0="),
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDM0MGEyNjh" +
                    "mMjVmZDVjYzI3NmNhMTQ3YTg0NDZiMjYzMGE1NTg2N2EyMzQ5ZjdjYTEwN2MyNmViNTg5OTEifX19"),
    ENDER_DRAGON(EntityType.ENDER_DRAGON,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjY4YzFjMDc" +
                    "5YTdmZmIzNmY0OGRkNzE1MDM1NWUzZTBiN2Y2OGRkNjA1ZTZmODg0NzMxM2MzNjBjZjYxZTBjIn19fQ=="),
    ENDERMAN(EntityType.ENDERMAN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTZjMGIzNmQ" +
                    "1M2ZmZjY5YTQ5YzdkNmYzOTMyZjJiMGZlOTQ4ZTAzMjIyNmQ1ZTgwNDVlYzU4NDA4YTM2ZTk1MSJ9fX0="),
    ENDERMITE(EntityType.ENDERMITE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJjN2I5ZDM" +
                    "2ZmI5MmI2YmYyOTJiZTczZDMyYzZjNWIwZWNjMjViNDQzMjNhNTQxZmFlMWYxZTY3ZTM5M2EzZSJ9fX0="),
    EVOKER(EntityType.EVOKER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc5ZjEzM2E" +
                    "4NWZlMDBkM2NmMjUyYTA0ZDZmMmViMjUyMWZlMjk5YzA4ZTBkOGI3ZWRiZjk2Mjc0MGEyMzkwOSJ9fX0="),
    //TODO: Add Fireball Texture
    FIREBALL(EntityType.FIREBALL, ""),
    FIREWORK(EntityType.FIREWORK,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAyZjQ4ZjM" +
                    "0ZDIyZGVkNzQwNGY3NmU4YTEzMmFmNWQ3OTE5YzhkY2Q1MWRmNmU3YTg1ZGRmYWM4NWFiIn19fQ=="),
    FOX(EntityType.FOX,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc0NjRjYTU" +
                    "xOGIwMDJmYTk5Mjc1ZDNjOTgxZmIxOWI0MWI0NDVhZGIxOWU3NTg0MzBjYjI4ZDUxNTYwMDAzNyJ9fX0="),
    FROG(),
    GHAST(EntityType.GHAST,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGU4YTM4ZTl" +
                    "hZmJkM2RhMTBkMTliNTc3YzU1YzdiZmQ2YjRmMmU0MDdlNDRkNDAxN2IyM2JlOTE2N2FiZmYwMiJ9fX0="),
    GIANT(EntityType.GIANT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ1MjhiMzI" +
                    "yOTY2MGYzZGZhYjQyNDE0ZjU5ZWU4ZmQwMWU4MDA4MWRkM2RmMzA4Njk1MzZiYTliNDE0ZTA4OSJ9fX0="),
    GLOW_SQUID(EntityType.GLOW_SQUID,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDMxYmJlY2R" +
                    "hNTgyMDEzMTQ0YWFiOGMwOWFiZTI5YTIxYTEyNDNiOTE4MzI3YTRjNWNkNDAyYzJhOTU0MTgwZiJ9fX0="),
    GOAT(EntityType.GOAT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmZiMWRhYzU" +
                    "yM2FkMTUzNTFkNzA3MmNkN2EwZGI1Mzg5Njk2ZDY3MWU0ZGNjMGJmOGExN2MzMWM3NTRlMDE5MyJ9fX0="),
    GUARDIAN(EntityType.GUARDIAN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk1MjkwZTA" +
                    "5MGMyMzg4MzJiZDc4NjBmYzAzMzk0OGM0ZDAzMTM1MzUzM2FjOGY2NzA5ODgyM2I3ZjY2N2YxYyJ9fX0="),
    HOGLIN(EntityType.HOGLIN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWJiOWJjMGY" +
                    "wMWRiZDc2MmEwOGQ5ZTc3YzA4MDY5ZWQ3Yzk1MzY0YWEzMGNhMTA3MjIwODU2MWI3MzBlOGQ3NSJ9fX0="),
    HORSE(EntityType.HORSE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQ1YzI1OWV" +
                    "iMjEzNTIxZmU2MTM1NjBkZGFlNzRlOWU2MmFhZGE3NTZhZTRjNjVhYTcyY2Y5MjY4M2EwNjI4MCJ9fX0="),
    HUSK(EntityType.HUSK,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ4YTVhNWU" +
                    "0ZGY5MDUyOGRiYTM1ZTA2NjdjZGMwYTdkZGMwMjU3NDBhMmIxOWJmMzU1YTY4YWI4OTlhMmZlNyJ9fX0="),
    ILLUSIONER(EntityType.ILLUSIONER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEyNTEyZTd" +
                    "kMDE2YTIzNDNhN2JmZjFhNGNkMTUzNTdhYjg1MTU3OWYxMzg5YmQ0ZTNhMjRjYmViODhiIn19fQ=="),
    IRON_GOLEM(EntityType.IRON_GOLEM,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTEzZjM0MjI" +
                    "3MjgzNzk2YmMwMTcyNDRjYjQ2NTU3ZDY0YmQ1NjJmYTlkYWIwZTEyYWY1ZDIzYWQ2OTljZjY5NyJ9fX0="),
    LLAMA(EntityType.LLAMA,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFlMjVkZGM" +
                    "yZDI1MzljNTY1ZGZmMmFhNTAwNjAzM2YxNGNjMDYzNzlmZTI4YjA3MzFjN2JkYzY1YmEwZTAxNiJ9fX0="),
    MAGMA_CUBE(EntityType.MAGMA_CUBE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFjOTdhMDZ" +
                    "lZmRlMDRkMDAyODdiZjIwNDE2NDA0YWIyMTAzZTEwZjA4NjIzMDg3ZTFiMGMxMjY0YTFjMGYwYyJ9fX0="),
    MUSHROOM_COW(EntityType.MUSHROOM_COW,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE2ZGI0MWV" +
                    "kZjUzZjU4ZWMwNWIzZmE4NmYwNTE3Y2RlMjI4NDJiYjA3MWI2YjU2ZDE2MDdmYzdkZTcwZGEyYiJ9fX0="),
    MULE(EntityType.MULE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZkY2RhMjY" +
                    "1ZTU3ZTRmNTFiMTQ1YWFjYmY1YjU5YmRjNjA5OWZmZDNjY2UwYTY2MWIyYzAwNjVkODA5MzBkOCJ9fX0="),
    OCELOT(EntityType.OCELOT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTE4Zjc3MzM" +
                    "xYjJjYWI2NGUyYjQzMGZhOGU0MjczZTRkYjdmNzhmY2RmYTRiMWE5YTQxOGFmNDczNzUwNTZlYiJ9fX0="),
    PANDA(EntityType.PANDA,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI2ZjJmYTh" +
                    "iNWI0YjRkNzBjNDEzMmE3MTMzNmQ4ZjRkYTU0MTkyNThkZmNhZDE4Y2NhODQ1OWNmNTYyMzkwMyJ9fX0="),
    PARROT(EntityType.PARROT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRmNGIzNDA" +
                    "xYTRkMDZhZDY2YWM4YjVjNGQxODk2MThhZTYxN2Y5YzE0MzA3MWM4YWMzOWE1NjNjZjRlNDIwOCJ9fX0="),
    PHANTOM(EntityType.PHANTOM,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWRmZTUxODA" +
                    "xNzYxNjYwZWJmNmRhZTcwZTljYWQ1ODhiMmVmNWU2Y2IyYjMxOTRkMDI4YTQwYWMwZWViY2RmNSJ9fX0="),
    PIG(EntityType.PIG,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVlODUxNDg" +
                    "5MmYzZDc4YTMyZTg0NTZmY2JiOGM2MDgxZTIxYjI0NmQ4MmYzOThiZDk2OWZlYzE5ZDNjMjdiMyJ9fX0="),
    PIGLIN(EntityType.PIGLIN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcxYjNhZWU" +
                    "xODJiOWE5OWVkMjZjYmY1ZWNiNDdhZTkwYzJjM2FkYzA5MjdkZGUxMDJjN2IzMGZkZjdmNDU0NSJ9fX0="),
    PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzMDBlOTA" +
                    "yNzM0OWM0OTA3NDk3NDM4YmFjMjllM2E0Yzg3YTg0OGM1MGIzNGMyMTI0MjcyN2I1N2Y0ZTFjZiJ9fX0="),
    PILLAGER(EntityType.PILLAGER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJmYjgwYTZ" +
                    "iNjgzM2UzMWQ5Y2U4MzEzYTU0Nzc3NjQ1ZjljMWU1NWI4MTA5MThhNzA2ZTdiY2M4ZDM1YTVhMiJ9fX0="),
    PRIMED_TNT(EntityType.PRIMED_TNT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU3M2Q3MDQ" +
                    "2ZDZlMDgxOTgzOTBhYTU2YzhmODY3OGMxNmQ0NDA3YWY5ZjIxNGJmMDI5MWYzYzdkYjFmMzc5YSJ9fX0="),
    POLAR_BEAR(EntityType.POLAR_BEAR,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRmZTkyNjk" +
                    "yMmZiYjQwNmYzNDNiMzRhMTBiYjk4OTkyY2VlNDQxMDEzN2QzZjg4MDk5NDI3YjIyZGUzYWI5MCJ9fX0="),
    PUFFERFISH(EntityType.PUFFERFISH,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRkNGI5Njc" +
                    "yNmNmZDM2MDE1YWYzNzc4MzM2YzUyMjZhZTEyZmU4MGNhOGFmZWVlNzYzZjQ1NDJhODgzYzI4MiJ9fX0="),
    RABBIT(EntityType.RABBIT,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGM0ZmI2YTg" +
                    "3Mzk0NDc0N2IwZTdmMTM1YmVkNjMzYzdjOTAyN2E4OGM5MGQxNzk4NDNjMGQyMGI0ZTllNzZhMCJ9fX0="),
    RAVAGER(EntityType.RAVAGER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWM3M2UxNmZ" +
                    "hMjkyNjg5OWNmMTg0MzQzNjBlMjE0NGY4NGVmMWViOTgxZjk5NjE0ODkxMjE0OGRkODdlMGIyYSJ9fX0="),
    SALMON(EntityType.SALMON,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc3MGQ5MTd" +
                    "kMWNjYzEyZjNhMWNmNzNmZTdkZThjNjU0OGY0YTg0MjA4NjkyM2M3YmI0NDQ2YmNjN2FhZWJmYSJ9fX0="),
    SHEEP(EntityType.SHEEP,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBmNTAzOTR" +
                    "jNmQ3ZGJjMDNlYTU5ZmRmNTA0MDIwZGM1ZDY1NDhmOWQzYmM5ZGNhYzg5NmJiNWNhMDg1ODdhIn19fQ=="),
    SHULKER(EntityType.SHULKER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUzM2ViZDE" +
                    "yYWU2ZGJmYTIzNDRkZjE2ZGE4ZmM2ZjM1OTdmZjQ4MDE3ZmJlMzgzYWJkMTY2OWNiZjU0NTYyZCJ9fX0="),
    SILVERFISH(EntityType.SILVERFISH,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTJlYzJjM2N" +
                    "iOTVhYjc3ZjdhNjBmYjRkMTYwYmNlZDRiODc5MzI5YjYyNjYzZDdhOTg2MDY0MmU1ODhhYjIxMCJ9fX0="),
    SKELETON(EntityType.SKELETON,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVhNTgzOWY" +
                    "xNzk3OThjZDNlN2IwOWMzNzEwNTc2NjVmYTI2ZjkzNjllMjY3ZmZkNDcxZjBlNzhkNGE2NTYyNCJ9fX0="),
    SKELETON_HORSE(EntityType.SKELETON_HORSE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMyMjNmNzc" +
                    "2MWFiMmY0ZjlkMjA4YzE1ZWQzNDk1YmY5NDg0NzkzNTY3M2YyMjEzNWRlODRiZGEzNWI3NDM1YiJ9fX0="),
    SLIME(EntityType.SLIME,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTMwYWFlYjN" +
                    "jMmI2NmI5Yjg3OTA2OGVlNWMzZjY5YzhhZjg1YWUwOWI1OTk3NzJlY2U1YjM0MzdjMzA1YTdmYSJ9fX0="),
    SNOWMAN(EntityType.SNOWMAN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTc3N2QzZGE" +
                    "0MzI5YmRmNjBlOWQyNjc4YWI3OTA4MzRkZTkwOTZmOWQ4NmEwZTk5YTFhYjhhNDNhYTVlYzFlNCJ9fX0="),
    SPIDER(EntityType.SPIDER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzVlMjQ4ZGE" +
                    "yZTEwOGYwOTgxM2E2Yjg0OGEwZmNlZjExMTMwMDk3ODE4MGVkYTQxZDNkMWE3YThlNGRiYTNjMyJ9fX0="),
    SQUID(EntityType.SQUID,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY0YmRjNmY" +
                    "2MDA2NTY1MTFiZWY1OTZjMWExNmFhYjFkM2Y1ZGJhYWU4YmVlMTlkNWMwNGRlMGRiMjFjZTkyYyJ9fX0="),
    STRAY(EntityType.STRAY,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM1MDk3OTE" +
                    "2YmMwNTY1ZDMwNjAxYzBlZWJmZWIyODcyNzdhMzRlODY3YjRlYTQzYzYzODE5ZDUzZTg5ZWRlNyJ9fX0="),
    TADPOLE(),
    TRADER_LLAMA(EntityType.TRADER_LLAMA,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA4N2E1NTZ" +
                    "kNGZmYTk1ZWNkMjg0NGYzNTBkYzQzZTI1NGU1ZDUzNWZhNTk2ZjU0MGQ3ZTc3ZmE2N2RmNDY5NiJ9fX0="),
    TROPICAL_FISH(EntityType.TROPICAL_FISH,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYzODlhY2Q" +
                    "3YzgyODBkMmM4MDg1ZTZhNmE5MWUxODI0NjUzNDdjYzg5OGRiOGMyZDliYjE0OGUwMjcxYzNlNSJ9fX0="),
    TURTLE(EntityType.TURTLE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGE0MDUwZTd" +
                    "hYWNjNDUzOTIwMjY1OGZkYzMzOWRkMTgyZDdlMzIyZjlmYmNjNGQ1Zjk5YjU3MThhIn19fQ=="),
    VEX(EntityType.VEX,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJlYzVhNTE" +
                    "2NjE3ZmYxNTczY2QyZjlkNWYzOTY5ZjU2ZDU1NzVjNGZmNGVmZWZhYmQyYTE4ZGM3YWI5OGNkIn19fQ=="),
    VILLAGER(EntityType.VILLAGER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhOGVmMjQ" +
                    "1OGEyYjEwMjYwYjg3NTY1NThmNzY3OWJjYjdlZjY5MWQ0MWY1MzRlZmVhMmJhNzUxMDczMTVjYyJ9fX0="),
    VINDICATOR(EntityType.VINDICATOR,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFlZWQ5ZDh" +
                    "lZDE3NjllNzdlM2NmZTExZGMxNzk2NjhlZDBkYjFkZTZjZTI5ZjFjOGUwZDVmZTVlNjU3M2I2MCJ9fX0="),
    WANDERING_TRADER(EntityType.WANDERING_TRADER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDk5ZDU4NWE" +
                    "5YWJmNTlmYWUyNzdiYjY4NGQyNDA3MGNlZjIxZTM1NjA5YTNlMThhOWJkNWRjZjczYTQ2YWI5MyJ9fX0="),
    WARDEN(),
    WITCH(EntityType.WITCH,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U3MWE2ZWI" +
                    "zMDNhYjdlNmY3MGVkNTRkZjkxNDZhODBlYWRmMzk2NDE3Y2VlOTQ5NTc3M2ZmYmViZmFkODg3YyJ9fX0="),
    WITHER(EntityType.WITHER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyODBjZWZ" +
                    "lOTQ2OTExZWE5MGU4N2RlZDFiM2UxODMzMGM2M2EyM2FmNTEyOWRmY2ZlOWE4ZTE2NjU4ODA0MSJ9fX0="),
    WITHER_SKELETON(EntityType.WITHER_SKELETON,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjVlYzk2NDY" +
                    "0NWE4ZWZhYzc2YmUyZjE2MGQ3Yzk5NTYzNjJmMzJiNjUxNzM5MGM1OWMzMDg1MDM0ZjA1MGNmZiJ9fX0="),
    WOLF(EntityType.WOLF,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjhkNDA4ODQ" +
                    "yZTc2YTVhNDU0ZGMxYzdlOWFjNWMxYThhYzNmNGFkMzRkNjk3M2I1Mjc1NDkxZGZmOGM1YzI1MSJ9fX0="),
    ZOGLIN(EntityType.ZOGLIN,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE5YjdiNWU" +
                    "5ZmZkNGUyMmI4OTBhYjc3OGI0Nzk1YjY2MmZhZmYyYjQ5NzhiZjgxNTU3NGU0OGIwZTUyYjMwMSJ9fX0="),
    ZOMBIE(EntityType.ZOMBIE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ1MjhiMzI" +
                    "yOTY2MGYzZGZhYjQyNDE0ZjU5ZWU4ZmQwMWU4MDA4MWRkM2RmMzA4Njk1MzZiYTliNDE0ZTA4OSJ9fX0="),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQ1MjhiMzI" +
                    "yOTY2MGYzZGZhYjQyNDE0ZjU5ZWU4ZmQwMWU4MDA4MWRkM2RmMzA4Njk1MzZiYTliNDE0ZTA4OSJ9fX0="),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM3NTA1ZjI" +
                    "yNGQ1MTY0YTExN2Q4YzY5ZjAxNWY5OWVmZjQzNDQ3MWM4YTJkZjkwNzA5NmM0MjQyYzM1MjRlOCJ9fX0="),
    ZOMBIEFIED_PIGLIN();

    private static int validAmount;

    public static void init() {
        Version currV = VersionHelper.getBukkitVersion();
        //1.19
        if(VersionHelper.isEqualOrLarger(currV, new Version(1, 19, 0))) {
            PluginMaster.sendConsoleMessage(Level.INFO, "Adding MC1.19 Mobs...");
            ALLAY.setData(EntityType.ALLAY,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmV" +
                            "lYTg0NWNjMGI1OGZmNzYzZGVjZmZlMTFjZDFjODQ1YzVkMDljM2IwNGZlODBiMDY2M2RhNWM3YzY5OWViMyJ9fX0=");
            FROG.setData(EntityType.FROG,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI" +
                            "1YzRiNGI4MjQyMjVmYjE5NTI3ZDQyZWZmZDRkYjQxNTM4YTY4ZTc4ZTdlNjI0MWI4MjUyOWI5MjJiMmU5NSJ9fX0=");
            TADPOLE.setData(EntityType.TADPOLE,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmN" +
                            "jOWI5NzQwYmQzYWRlYmE1MmUwY2UwYTc3YjNkZmRlZjhkM2E0MDU1NWE0ZThiYjY3ZDIwMGNkNjI3NzBkMCJ9fX0=");
            WARDEN.setData(EntityType.WARDEN,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y" +
                            "2NDgxYzdjNDM1YzM0ZjIxZGZmMTA0M2E0YzcwMzRjNDQ1YTM4M2E1NDM1ZmExZjJhNTAzYTM0OGFmZDYyZiJ9fX0=");
            ZOMBIEFIED_PIGLIN.setData(EntityType.ZOMBIFIED_PIGLIN,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk" +
                            "yMzdhMDY3MjEyMDEzMmY2MWE2ZGIyZjQ1ZDE3N2Y4YWQ4Y2UxYTlmYWQ5MTMyNzRjNzUyYTUxNGQ4Y2I1NSJ9fX0=");
        }

        //Blacklist Entities
        for(String s:Config.getInstance().getEntityBlacklist()) {
            try {
                SpawnableEntity.valueOf(s).isBlacklisted = true;
            }catch (IllegalArgumentException ignored) {
                PluginMaster.sendConsoleMessage(Level.WARNING, "Unknown Entity '"+s+"' in Blacklist found!");
            }
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
    private boolean isBlacklisted;

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

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public ItemStack getItemStack() {
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        assert sm != null;
        GameProfile gp = new GameProfile(UUID.randomUUID(), null);
        gp.getProperties().put("textures", new Property("textures", texture));
        sm.setDisplayName(type.toString());
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
