package com.fg.tltmod.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class TltCoreEntityTagProvider extends EntityTypeTagsProvider {
    public TltCoreEntityTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider) {
        super(pOutput, pProvider);
    }
    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("ad_astra", name));
    }

    public static final TagKey<EntityType<?>> LIVES_WITHOUT_OXYGEN = tag("lives_without_oxygen");
    public static final TagKey<EntityType<?>> CAN_SURVIVE_IN_SPACE = tag("can_survive_in_space");

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        ForgeRegistries.ENTITY_TYPES.getKeys().forEach(key->{
            var entityType = ForgeRegistries.ENTITY_TYPES.getValue(key);
            if (key.getNamespace().equals("alexscaves")&&entityType!=null){
                if (!entityType.getCategory().equals(MobCategory.MISC)) {
                    tag(LIVES_WITHOUT_OXYGEN).add(entityType);
                    tag(CAN_SURVIVE_IN_SPACE).add(entityType);
                }
            }
        });
    }
}
