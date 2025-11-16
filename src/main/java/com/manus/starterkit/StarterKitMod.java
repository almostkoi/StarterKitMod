package com.manus.starterkit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StarterKitMod implements ModInitializer {
    public static final String MOD_ID = "starterkitmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("StarterKitMod Initializing!");
        
        // Register the command
        CommandRegistrationCallback.EVENT.register(StarterKitCommand::register);
    }
}
