//package com.enclave.enclavemod.mixin;
//
//import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
//import org.spongepowered.asm.launch.MixinBootstrap;
//import org.spongepowered.asm.mixin.Mixins;
//
//import java.util.Map;
//
//public abstract class MixinLoader implements IFMLLoadingPlugin {
//
//    public MixinLoader() {
//        MixinBootstrap.init();
//        Mixins.addConfiguration("mixins.enclavemod.json");
//    }
//
//    @Override
//    public String[] getASMTransformerClass() {
//        return new String[0];
//    }
//
//    @Override
//    public String getAccessTransformerClass() {
//        return null;
//    }
//
//    @Override
//    public String getModContainerClass() {
//        return null;
//    }
//
//    @Override
//    public String getSetupClass() {
//        return null;
//    }
//
//    @Override
//    public void injectData(Map<String, Object> data)
//    {
//        // don't care about this data
//    }
//}
