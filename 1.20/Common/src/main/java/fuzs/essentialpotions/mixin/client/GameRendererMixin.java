package fuzs.essentialpotions.mixin.client;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.config.ServerConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void getNightVisionScale(LivingEntity livingEntity, float tickDelta, CallbackInfoReturnable<Float> callback) {
        if (!EssentialPotions.CONFIG.get(ServerConfig.class).noNightVisionFlashing) return;
        MobEffectInstance effect = livingEntity.getEffect(MobEffects.NIGHT_VISION);
        callback.setReturnValue(Mth.clamp((effect.getDuration() - tickDelta) / 20.0F, 0.0F, 1.0F));
    }
}
