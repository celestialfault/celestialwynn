package me.celestialfault.celestialwynn.config;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public enum FOVScaling {
	VANILLA,
	ARCTANGENT,
	SPRINT_ONLY;

	public float calculate(float fovMultiplier, AbstractClientPlayerEntity player) {
		return switch(this) {
			case ARCTANGENT -> 1f + (float) (Math.atan(2d * Math.PI * (fovMultiplier - 1d)) / (2d * Math.PI));
			case SPRINT_ONLY -> 1f + (player.isSprinting() ? 0.15f : 0);
			default -> fovMultiplier;
		};
	}
}
