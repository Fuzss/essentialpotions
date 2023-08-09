package fuzs.essentialpotions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class EssentialPotionsCapabilityImpl implements EssentialPotionsCapability {
    private static final String TAG_HEALTH = "Health";

    private final LivingEntity entity;
    private float health = -1.0F;

    public EssentialPotionsCapabilityImpl(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void tryLoadHealth() {
        if (this.health != -1.0F) {
            this.entity.setHealth(this.health);
            this.health = -1.0F;
        }
    }

    @Override
    public void write(CompoundTag tag) {
        tag.putFloat(TAG_HEALTH, this.entity.getHealth());
    }

    @Override
    public void read(CompoundTag tag) {
        if (tag.contains(TAG_HEALTH, Tag.TAG_ANY_NUMERIC)) {
            this.health = tag.getFloat(TAG_HEALTH);
        }
    }
}
