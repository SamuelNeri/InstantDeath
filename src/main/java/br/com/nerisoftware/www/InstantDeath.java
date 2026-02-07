package br.com.nerisoftware.www;

import org.bukkit.plugin.java.JavaPlugin;

public final class InstantDeath extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
