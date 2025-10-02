package com.enclave.enclavemod.registers;
import com.enclave.enclavemod.packets.MessageSyncSliderPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class NetworkPacketsRegistry {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("enclavemod");
    private static int id = 0;

    public static void registerRaw(Class handlerClass, Class messageClass, Side side) {
        INSTANCE.registerMessage(handlerClass, messageClass, id++, side);
    }

    public static void registerMessages() {
        registerRaw(MessageSyncSliderPos.Handler.class, MessageSyncSliderPos.class, Side.SERVER);
    }
}
