package fuzs.essentialpotions.handler;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.capability.EssentialPotionsCapability;
import fuzs.essentialpotions.config.ServerConfig;
import fuzs.essentialpotions.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableDouble;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

public class VanillaEffectsHandler {

    public static EventResult onLivingTick$0(LivingEntity entity) {
        if (!EssentialPotions.CONFIG.get(ServerConfig.class).betterFireResistanceVision) return EventResult.PASS;
        if (entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            entity.setRemainingFireTicks(Math.min(1, entity.getRemainingFireTicks()));
        }
        return EventResult.PASS;
    }

    public static EventResult onLivingTick$1(LivingEntity entity) {
        if (entity.tickCount > 1) ModRegistry.ESSENTIAL_POTIONS_CAPABILITY.maybeGet(entity).ifPresent(EssentialPotionsCapability::tryLoadHealth);
        return EventResult.PASS;
    }

    public static void onLivingVisibility(LivingEntity entity, @Nullable Entity lookingEntity, MutableDouble visibilityPercentage) {
        if (!EssentialPotions.CONFIG.get(ServerConfig.class).strongerBlindness) return;
        if (lookingEntity instanceof Mob mob && mob.hasEffect(MobEffects.BLINDNESS)) {
            visibilityPercentage.mapDouble(t -> getVisibilityMultiplier(mob.getEffect(MobEffects.BLINDNESS).getAmplifier()) * 0.5F);
        }
    }

    public static float getVisibilityMultiplier(int amplifier) {
        return Mth.clamp(1.0F / (float) Math.sqrt(Math.max(1.0F, amplifier + 1.0F)), 0.0F, 1.0F);
    }
}
