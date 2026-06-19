package me.incend1um.noinputlagtickrate.network;

import me.incend1um.noinputlagtickrate.NoInputLagTickRateClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class NoInputLagTickRateNetwork {

    public void register() {
        PayloadTypeRegistry.playS2C().register(OptOutPayload.TYPE, OptOutPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(OptOutPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (payload.value() == OptOutPayload.OPT_OUT_BYTE) {
                    NoInputLagTickRateClient.setOptedOut(true);
                }
            });
        });
    }

    public record OptOutPayload(byte value) implements CustomPacketPayload {
        public static final byte OPT_OUT_BYTE = 1;

        public static final CustomPacketPayload.Type<OptOutPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("noinputlagtickrate", "opt_out"));

        public static final StreamCodec<FriendlyByteBuf, OptOutPayload> CODEC =
            CustomPacketPayload.codec(OptOutPayload::write, OptOutPayload::new);

        private OptOutPayload(FriendlyByteBuf buf) {
            this(buf.readByte());
        }

        private void write(FriendlyByteBuf buf) {
            buf.writeByte(value);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
