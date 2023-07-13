package fuzs.essentialpotions.mixin;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.config.ServerConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "travel", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    public double travel(double gravityStrength) {
        if (!EssentialPotions.CONFIG.get(ServerConfig.class).slowFallingQuickDescent) return gravityStrength;
        if (Math.abs(gravityStrength - 0.01) < 0.00000001 && this.isDescending()) return 0.08;
        return gravityStrength;
    }
}
