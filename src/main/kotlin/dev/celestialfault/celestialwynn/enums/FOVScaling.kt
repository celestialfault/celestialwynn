package dev.celestialfault.celestialwynn.enums

import net.minecraft.client.network.AbstractClientPlayerEntity
import kotlin.math.atan

enum class FOVScaling {
	VANILLA,
	ARCTANGENT,
	SPRINT_ONLY;

	fun calculate(fovMultiplier: Float, player: AbstractClientPlayerEntity): Float = when(this) {
		ARCTANGENT -> 1f + (atan(2.0 * Math.PI * (fovMultiplier - 1.0)) / (2.0 * Math.PI)).toFloat()
		SPRINT_ONLY -> 1f + (if(player.isSprinting) 0.15f else 0f)
		VANILLA -> fovMultiplier
	}
}
