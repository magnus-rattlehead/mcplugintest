package me.magnus.test_one;

import me.magnus.test_one.Events.Events;
import org.bukkit.plugin.java.JavaPlugin;

public final class Test_one extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("TreeKiller Starting...");
        getServer().getPluginManager().registerEvents(new Events(), this);
        System.out.println("TreeKiller Started!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("TreeKiller stopping");
    }
}
