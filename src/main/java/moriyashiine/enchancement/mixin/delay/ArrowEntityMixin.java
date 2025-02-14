/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.delay;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends PersistentProjectileEntity {
	protected ArrowEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V", at = @At("TAIL"))
	private void enchancement$delay(World world, LivingEntity owner, CallbackInfo ci) {
		boolean offHand = EnchancementUtil.hasEnchantment(ModEnchantments.DELAY, owner.getOffHandStack());
		if (offHand || EnchancementUtil.hasEnchantment(ModEnchantments.DELAY, owner.getMainHandStack())) {
			ModEntityComponents.DELAY.maybeGet(this).ifPresent(delayComponent -> {
				delayComponent.setHasDelay(true);
				delayComponent.setStackShotFrom(offHand ? owner.getOffHandStack() : owner.getMainHandStack());
				delayComponent.sync();
				setCritical(true);
			});
		}
	}
}
