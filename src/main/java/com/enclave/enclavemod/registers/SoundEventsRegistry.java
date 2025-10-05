package com.enclave.enclavemod.registers;

import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundEventsRegistry {
    public static final SoundEvent CLAYMORE_TRIGGER = new SoundEvent(new ResourceLocation("enclavemod", "claymore_trigger"));

    public static void register() {
        registerSoundEvent(CLAYMORE_TRIGGER);
    }

    private static void registerSoundEvent(SoundEvent soundEvent) {
        ForgeRegistries.SOUND_EVENTS.register(soundEvent);
    }
}
