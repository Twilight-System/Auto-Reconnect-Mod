package com.twilightsystem;

import com.twilightsystem.commands.commands;
import com.twilightsystem.config.configManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import com.twilightsystem.config.config;

public class AutoReconnectClient implements ClientModInitializer {

	private static Text disconnectMessage;

	private static ServerInfo lastestServerEntry;
	private static int disconnectTick = 0;

	private static final int MAX_TICK = 20 * 2;

	private static final config config = configManager.loadConfig();

	@Environment(EnvType.CLIENT)
	public static void clientTick() {
		MinecraftClient mc = MinecraftClient.getInstance();

		if (mc.world != null && mc.getCurrentServerEntry() != null) {
			lastestServerEntry = mc.getCurrentServerEntry();
		}

		if (mc.currentScreen instanceof DisconnectedScreen) {
			// Directly check against the current config value
			if (disconnectMessage.getString().equals(config.getautoreconnectstring())) {
				disconnectTick++;
				if (disconnectTick == MAX_TICK && lastestServerEntry != null) {
					System.out.println(disconnectTick);
					mc.disconnect();
					ConnectScreen.connect(new TitleScreen(), mc, ServerAddress.parse(lastestServerEntry.address), lastestServerEntry, false);
					disconnectTick = 0;
					System.out.println("Reconnect");
				}
			} else {
				disconnectTick = 0;
			}
		} else {
			disconnectTick = 0;
		}
	}

	@Override
	public void onInitializeClient() {
		configManager.loadConfig();
		commands.registerCommands();
		ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> clientTick());
		LogManager.getLogger().info("Loading Auto Reconnect Mod");

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			disconnectMessage = handler.getConnection().getDisconnectReason();
			System.out.println("Disconnect Message: " + disconnectMessage.getString());
		});
	}
}