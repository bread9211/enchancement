/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TradeOffers.EnchantBookFactory.class)
public class TradeOffersEnchantBookFactoryMixin {
	@ModifyExpressionValue(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
	private int enchancement$singleLevelMode(int value) {
		if (Enchancement.getConfig().singleLevelMode) {
			return 1;
		}
		return value;
	}
}
