package de.devloper.pickelMaintenance;

import de.devloper.pickelMaintenance.commands.MaintenanceCommand;
import de.devloper.pickelMaintenance.listener.MaintenanceListener;
import de.devloper.pickelMaintenance.manager.MaintenanceManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PickelMaintenance extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        MaintenanceManager manager = new MaintenanceManager(this);

        PluginCommand command = getCommand("wartung");
        if (command != null) {
            command.setExecutor(new MaintenanceCommand(this, manager));
        }

        getServer().getPluginManager().registerEvents(new MaintenanceListener(this, manager), this);

        getLogger().info("PickelMaintenance wurde erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PickelMaintenance wurde deaktiviert!");
    }
}