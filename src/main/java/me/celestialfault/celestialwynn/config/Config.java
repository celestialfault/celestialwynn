package me.celestialfault.celestialwynn.config;

import me.celestialfault.celestialconfig.AbstractConfig;
import me.celestialfault.celestialconfig.properties.*;
import net.fabricmc.loader.api.FabricLoader;

public final class Config extends AbstractConfig {
	private Config() {
		super(FabricLoader.getInstance().getConfigDir().resolve("celestial-wynn.json"));
	}

	public static final Config INSTANCE = new Config();

	public final NoNullProperty<Boolean> fixSilverbullCapes = new BooleanProperty("fix_silverbull_capes", true).notNullable();
	public final NoNullProperty<Boolean> hideTerritoryBar = new BooleanProperty("hide_territory_bar", false).notNullable();
	public final NoNullProperty<FOVScaling> fovScaling = new EnumProperty<>("fov_scaling", FOVScaling.VANILLA, FOVScaling.class).notNullable();

	public final ItemScaling itemScaling = new ItemScaling();

	public static class ItemScaling extends ObjectProperty<ItemScaling> {
		private ItemScaling() {
			super("item_scaling");
		}

		public final NoNullProperty<Float> scale = new FloatProperty("scale", 1f, 0.1f, 2f).notNullable();

		public final NoNullProperty<Integer> x = new IntegerProperty("x", 0, -150, 150).notNullable();
		public final NoNullProperty<Integer> y = new IntegerProperty("y", 0, -150, 150).notNullable();
		public final NoNullProperty<Integer> z = new IntegerProperty("z", 0, -150, 50).notNullable();
	}
}
