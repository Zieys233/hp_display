package com.hp_display;

import net.fabricmc.api.ClientModInitializer;

public class HPDisplayClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MobInfoRenderer.init();
	}
}
