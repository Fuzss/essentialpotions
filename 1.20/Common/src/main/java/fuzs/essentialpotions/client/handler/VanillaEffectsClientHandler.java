package fuzs.essentialpotions.client.handler;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.config.ServerConfig;
import fuzs.essentialpotions.handler.VanillaEffectsHandler;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;

public class VanillaEffectsClientHandler {

    public static void onRenderFog$1(GameRenderer gameRenderer, Camera camera, float partialTicks, FogRenderer.FogMode fogMode, FogType fogType, MutableFloat fogStart, MutableFloat fogEnd, MutableValue<FogShape> fogShape) {
        if (!EssentialPotions.CONFIG.get(ServerConfig.class).strongerBlindness) return;
        if (fogType != FogType.LAVA && fogType != FogType.POWDER_SNOW && camera.getEntity() instanceof LocalPlayer player && player.hasEffect(MobEffects.BLINDNESS)) {
            MobEffectInstance effect = player.getEffect(MobEffects.BLINDNESS);
            float multiplier = VanillaEffectsHandler.getVisibilityMultiplier(effect.getAmplifier());
            fogStart.mapFloat(t -> t * multiplier);
            fogEnd.mapFloat(t -> t * multiplier);
        }
    }

    public static void onRenderFog$2(GameRenderer gameRenderer, Camera camera, float partialTicks, FogRenderer.FogMode fogMode, FogType fogType, MutableFloat fogStart, MutableFloat fogEnd, MutableValue<FogShape> fogShape) {
        if (!EssentialPotions.CONFIG.get(ServerConfig.class).betterFireResistanceVision) return;
        if (fogType == FogType.LAVA && camera.getEntity() instanceof LocalPlayer player && applyFireResistanceEffects(player)) {
            MobEffectInstance effect = player.getEffect(MobEffects.FIRE_RESISTANCE);
            float fogDistance;
            if (player.isCreative() || effect == null || effect.isInfiniteDuration()) {
                fogDistance = 1.0F;
            } else {
                fogDistance = Mth.clamp((effect.getDuration() - partialTicks) / 20.0F, 0.0F, 1.0F);
            }
            fogStart.accept(Mth.lerp(fogDistance, 0.25F, -4.0F));
            fogEnd.accept(Mth.lerp(fogDistance, 1.0F, gameRenderer.getRenderDistance() * 0.25F));
        }
    }

    public static EventResult onRenderBlockOverlay(LocalPlayer player, PoseStack poseStack, @Nullable BlockState blockState) {
        if (!EssentialPotions.CONFIG.get(ServerConfig.class).betterFireResistanceVision) return EventResult.PASS;
        if (blockState == Blocks.FIRE.defaultBlockState() && applyFireResistanceEffects(player)) {
            return EventResult.INTERRUPT;
        }
        return EventResult.PASS;
    }

    private static boolean applyFireResistanceEffects(Player player) {
        return player.isCreative() || !player.isSpectator() && player.hasEffect(MobEffects.FIRE_RESISTANCE);
    }
}
