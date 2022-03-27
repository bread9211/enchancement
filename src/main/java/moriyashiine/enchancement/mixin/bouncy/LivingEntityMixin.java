package moriyashiine.enchancement.mixin.bouncy;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private Vec3d prevVelocity;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void enchancement$bouncy(CallbackInfo ci) {
		if (!world.isClient) {
			prevVelocity = getVelocity();
		}
	}

	@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/particle/BlockStateParticleEffect;<init>(Lnet/minecraft/particle/ParticleType;Lnet/minecraft/block/BlockState;)V"))
	private BlockState enchancement$bouncy(BlockState value) {
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.BOUNCY, LivingEntity.class.cast(this)) > 0) {
			return Blocks.SLIME_BLOCK.getDefaultState();
		}
		return value;
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void enchancement$bouncy(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (fallDistance > getSafeFallDistance() && EnchantmentHelper.getEquipmentLevel(ModEnchantments.BOUNCY, LivingEntity.class.cast(this)) > 0) {
			if (!world.isClient) {
				world.playSoundFromEntity(null, this, SoundEvents.BLOCK_SLIME_BLOCK_FALL, getSoundCategory(), 1, 1);
				if (!bypassesLandingEffects()) {
					setVelocity(getVelocity().getX(), -prevVelocity.getY() * 0.99, getVelocity().getZ());
					scheduleVelocityUpdate();
					velocityDirty = true;
				}
			}
			cir.setReturnValue(false);
		}
	}
}
