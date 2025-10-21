package com.fg.tltmod.content.entityTicker;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerInstance;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerManager;
import com.fg.tltmod.Register.TltCoreEntityTickers;
import net.minecraft.world.entity.Entity;

public class ZeroGravity extends EntityTicker {
    @Override
    public void onTickerStart(int duration, int level, Entity entity) {
        entity.getPersistentData().putBoolean("tltmod_no_gravity",entity.isNoGravity());
        entity.setNoGravity(true);
        EntityTickerManager.getInstance(entity).setTicker(new EntityTickerInstance(TltCoreEntityTickers.ZERO_GRAVITY_CD.get(), 1,duration+300));
    }
    @Override
    public void onTickerEnd(int level, Entity entity) {
        entity.setNoGravity(entity.getPersistentData().getBoolean("tltmod_no_gravity"));
    }
}
