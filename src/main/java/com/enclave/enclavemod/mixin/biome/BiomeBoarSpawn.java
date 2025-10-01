//package com.enclave.enclavemod.mixin.biome;
//
//import com.enclave.enclavemod.entity.EntityBoar;
//import com.google.common.collect.Lists;
//import net.minecraft.entity.passive.EntityPig;
//import net.minecraft.world.biome.Biome;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.List;
//
//@Mixin(Biome.class)
//public abstract class BiomeBoarSpawn extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Biome> {
//    @Shadow
//    protected List<Biome.SpawnListEntry> spawnableCreatureList = Lists.<Biome.SpawnListEntry>newArrayList();
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void onInit(Biome.BiomeProperties properties, CallbackInfo ci) {
//        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityBoar.class, 10, 2, 4));
//    }
//}
