package com.fg.tltmod.util.mixin;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;

public interface IManaBurstMixin{
    IntOpenHashSet tltmod$getHitEntityIdList();
    void tltmod$addToHitList(Entity entity);
    float tltmod$getBaseDamage();
    void tltmod$setBaseDamage(float baseDamage);
    int tltmod$getPerConsumption();
    void tltmod$setPerConsumption(int i);
    int tltmod$getPerBlockConsumption();
    void tltmod$setPerBlockConsumption(int i);
}
