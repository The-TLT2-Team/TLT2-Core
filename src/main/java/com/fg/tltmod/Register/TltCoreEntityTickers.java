package com.fg.tltmod.Register;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import com.c2h6s.etstlib.content.register.EtSTLibRegistries;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entityTicker.MagneticParticleTicker;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TltCoreEntityTickers {
    public static final DeferredRegister<EntityTicker> ENTITY_TICKERS = DeferredRegister.create(EtSTLibRegistries.ENTITY_TICKER, TltCore.MODID);

    public static final RegistryObject<EntityTicker> SCARLET_PARTICLE = ENTITY_TICKERS.register("scarlet_particle", MagneticParticleTicker::new);
    public static final RegistryObject<EntityTicker> AZURE_PARTICLE = ENTITY_TICKERS.register("azure_particle", MagneticParticleTicker::new);

}
