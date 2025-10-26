package com.fg.tltmod.content.capability;

import com.fg.tltmod.content.capability.compat.botania.ManaCurioCapabilityProvider;

public class CapabilitiesRegister {

    public static void init() {
        ManaCurioCapabilityProvider.register();
    }
}
