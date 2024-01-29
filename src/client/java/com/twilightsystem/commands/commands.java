package com.twilightsystem.commands;


import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.twilightsystem.config.config;
import com.twilightsystem.config.configManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class commands {
    public static void registerCommands() {
    ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("autoreconnect")
                    .executes(context -> getMSG(context))
                       .then(ClientCommandManager.literal("set")
                               .then(ClientCommandManager.argument("value", StringArgumentType.string())
                                        .executes(context -> setMSG(context, StringArgumentType.getString(context, "value")))))));
    }
    ;

    private static int getMSG(CommandContext<FabricClientCommandSource> context) {
        config config = configManager.loadConfig();
        String value = config.getautoreconnectstring();
        context.getSource().getPlayer().sendMessage(Text.literal("Current Auto-Reconnect message: " + value), false);

        return 1;
    }

    private static int setMSG(CommandContext<FabricClientCommandSource> context, String value) {
        config config = configManager.loadConfig();
        config.setautoreconnectstring(value);
        configManager.saveConfig(config);

        context.getSource().getPlayer().sendMessage(Text.literal("Auto-Reconnect message set to: " + value), true);

        return 1;
    }
}