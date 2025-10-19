package org.projectpersistence.anticrystal;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.literal;

public class Anticrystal implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        // Block End Crystal placement
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient() && player.getStackInHand(hand).getItem() == Items.END_CRYSTAL) {
                RegistryKey<World> dimension = world.getRegistryKey();

                if (dimension == World.OVERWORLD || dimension == World.NETHER) {
                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        serverPlayer.sendMessage(
                                Text.of("You cannot place End Crystals in this dimension!"),
                                false
                        );
                    }
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });

        // Register /anticrystal command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("anticrystal")
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();
                        source.sendFeedback(
                                () -> Text.of("ProjectPersistence GitHub: https://github.com/ProjectPersistence"),
                                false
                        );
                        return 1;
                    })
            );
        });
    }
}
