package com.fg.tltmod.util.tinker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.UUID;

public class ModDataNBTExtra extends ModDataNBT {
    /** 存 long */
    public static void putLong(ModDataNBT data, ResourceLocation key, long value) {
        getTag(data).putLong(key.toString(), value);
    }

    /** 取 long */
    public static long getLong(ModDataNBT data, ResourceLocation key) {
        return getTag(data).getLong(key.toString());
    }

    /** 存 double */
    public static void putDouble(ModDataNBT data, ResourceLocation key, double value) {
        getTag(data).putDouble(key.toString(), value);
    }

    /** 取 double */
    public static double getDouble(ModDataNBT data, ResourceLocation key) {
        return getTag(data).getDouble(key.toString());
    }

    /** 存 UUID */
    public static void putUUID(ModDataNBT data, ResourceLocation key, UUID uuid) {
        getTag(data).putUUID(key.toString(), uuid);
    }

    /** 取 UUID */
    public static UUID getUUID(ModDataNBT data, ResourceLocation key) {
        CompoundTag tag = getTag(data);
        if (!tag.contains(key.toString())) return null;
        try {
            return tag.getUUID(key.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /** 删除 */
    public static void remove(ModDataNBT data, ResourceLocation key) {
        getTag(data).remove(key.toString());
    }

    /** 内部方法，拿到真正的 CompoundTag */
    private static CompoundTag getTag(ModDataNBT data) {
        try {
            var field = ModDataNBT.class.getDeclaredField("data");
            field.setAccessible(true);
            return (CompoundTag) field.get(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access ModDataNBT tag", e);
        }
    }
}
