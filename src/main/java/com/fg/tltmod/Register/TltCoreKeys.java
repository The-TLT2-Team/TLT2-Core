package com.fg.tltmod.Register;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class TltCoreKeys {
    public static final Map<String, KeyMapping> KEY_MAPPINGS = new HashMap<>();
    public static final String CATEGORY = "key.categories.tlt";
    public static final String SWITCH_KEY_ID = "key.tltmod.switch";

    public static void registerKeyBindings() {
        registerKey(SWITCH_KEY_ID, GLFW.GLFW_KEY_H, CATEGORY);
    }

    private static void registerKey(String id, int defaultKey, String category) {
        KEY_MAPPINGS.put(id, new KeyMapping(id, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, defaultKey, category));
    }

    public static @Nullable KeyMapping getKeyMappingById(String id) {
        return (KeyMapping)KEY_MAPPINGS.get(id);
    }

    public static KeyMapping getSwitchKey() {
        return KEY_MAPPINGS.get(SWITCH_KEY_ID);
    }
}
