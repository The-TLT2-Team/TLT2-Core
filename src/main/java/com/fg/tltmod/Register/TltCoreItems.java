package com.fg.tltmod.Register;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.item.DummyToolManaLens;
import com.fg.tltmod.content.item.IonizedArrowItem;
import com.fg.tltmod.content.item.SevenCurseRemovalItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TltCoreItems {
    public static final DeferredRegister<Item> BASIC_ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, TltCore.MODID);

    public static final RegistryObject<Item> DUMMY_TOOL_MANA_LENS = BASIC_ITEM.register("dummy_tool_mana_lens", DummyToolManaLens::new);
    public static final RegistryObject<Item> SEVEN_CURSE_REMOVAL_ITEM = BASIC_ITEM.register("seven_curse_removal", SevenCurseRemovalItem::new);
    public static final RegistryObject<Item> IONIZED_ARROW = BASIC_ITEM.register("ionized_arrow", IonizedArrowItem::new);

}
