/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.util;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableText.class)
public class TranslatableTextMixin {
	@ModifyVariable(method = "<init>*", at = @At("HEAD"), argsOnly = true)
	private static String enchancement$redirectKey(String value) {
		if (shouldRedirect(value)) {
			return value + "_redirect";
		}
		return value;
	}

	@Unique
	private static boolean shouldRedirect(String key) {
		return switch (key) {
			case "enchantment.minecraft.fire_aspect.desc" -> Enchancement.getConfig().fireAspectWorksAsFlintAndSteel;
			case "enchantment.minecraft.infinity.desc" -> Enchancement.getConfig().allowInfinityOnCrossbows;
			case "enchantment.minecraft.channeling.desc" -> Enchancement.getConfig().channelingWorksWhenNotThundering;
			case "enchantment.minecraft.luck_of_the_sea.desc" -> Enchancement.getConfig().luckOfTheSeaHasLure;
			case "enchantment.minecraft.unbreaking.desc" -> Enchancement.getConfig().unbreakingChangesFlag > 0;
			default -> false;
		};
	}
}
