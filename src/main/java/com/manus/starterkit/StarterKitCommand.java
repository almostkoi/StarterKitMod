package com.manus.starterkit;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.literal;

public class StarterKitCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(literal("starterkit")
            .requires(source -> source.hasPermissionLevel(2)) // Requires OP or creative mode
            .executes(context -> execute(context, context.getSource().getPlayerOrThrow()))
            .then(CommandManager.argument("target", EntityArgumentType.players())
                .executes(context -> execute(context, EntityArgumentType.getPlayers(context, "target")))
            )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, PlayerEntity player) throws CommandSyntaxException {
        giveItems(player);
        context.getSource().sendFeedback(() -> Text.literal("Gave starter kit to " + player.getName().getString()), true);
        return 1;
    }

    private static int execute(CommandContext<ServerCommandSource> context, Collection<PlayerEntity> players) throws CommandSyntaxException {
        for (PlayerEntity player : players) {
            giveItems(player);
        }
        context.getSource().sendFeedback(() -> Text.literal("Gave starter kit to " + players.size() + " players."), true);
        return players.size();
    }

    private static void giveItems(PlayerEntity player) {
        // Helper function to create an ItemStack with optional NBT damage
        var giveItem = new Object() {
            public void apply(String itemId, int count, int damage) {
                ItemStack stack = new ItemStack(Registries.ITEM.get(new Identifier(itemId)), count);
                if (damage > 0) {
                    NbtCompound nbt = new NbtCompound();
                    nbt.putInt("Damage", damage);
                    stack.setNbt(nbt);
                }
                player.getInventory().insertStack(stack);
            }
            public void apply(String itemId, int count) {
                apply(itemId, count, 0);
            }
        };

        // The list of items from the user's request
        giveItem.apply("minecraft:oak_fence", 1);
        giveItem.apply("minecraft:oak_planks", 38);
        giveItem.apply("minecraft:cobblestone", 50);
        giveItem.apply("minecraft:emerald_block", 32);
        giveItem.apply("minecraft:torch", 1);
        giveItem.apply("minecraft:crafting_table", 1);
        giveItem.apply("minecraft:wooden_hoe", 1);
        giveItem.apply("minecraft:iron_pickaxe", 1);
        giveItem.apply("minecraft:goat_horn", 1);
        giveItem.apply("minecraft:wooden_pickaxe", 1);
        giveItem.apply("minecraft:arrow", 5);
        // Emeralds (97) - split into 64 and 33
        giveItem.apply("minecraft:emerald", 64);
        giveItem.apply("minecraft:emerald", 33);
        giveItem.apply("minecraft:book", 32);
        
        // Damaged tools
        giveItem.apply("minecraft:diamond_sword", 1, 275);
        giveItem.apply("minecraft:diamond_pickaxe", 1, 578);
        giveItem.apply("minecraft:iron_pickaxe", 1, 139);
        giveItem.apply("minecraft:diamond_shovel", 1, 125);

        giveItem.apply("minecraft:bucket", 1);
        giveItem.apply("minecraft:lectern", 1);
        giveItem.apply("minecraft:stick", 16);
        giveItem.apply("minecraft:cooked_beef", 32);
    }
}
