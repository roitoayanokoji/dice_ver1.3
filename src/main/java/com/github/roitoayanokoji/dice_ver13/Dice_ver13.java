package com.github.roitoayanokoji.dice_ver13;

import org.bukkit.plugin.java.JavaPlugin;

public final class Dice_ver13 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("OnEnable");

        getCommand("dice").setExecutor(new DiceCommandExecutor(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("OnDisable");
    }
}
