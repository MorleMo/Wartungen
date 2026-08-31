package de.devloper.pickelMaintenance.manager;

import de.devloper.pickelMaintenance.PickelMaintenance;
import de.devloper.pickelMaintenance.util.HexColorUtil;
import de.devloper.pickelMaintenance.util.TimeUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MaintenanceManager {

    private final PickelMaintenance plugin;

    public MaintenanceManager(PickelMaintenance plugin) {
        this.plugin = plugin;
    }

    public boolean isMaintenanceActive() {
        long endTime = plugin.getConfig().getLong("maintenance.end-time", 0);
        if (endTime > 0 && System.currentTimeMillis() >= endTime) {
            setMaintenance(false, 0, "");
            return false;
        }
        return plugin.getConfig().getBoolean("maintenance.enabled", false);
    }

    public void setMaintenance(boolean active, long durationSeconds, String reason) {
        long endTime = active && durationSeconds > 0 ? System.currentTimeMillis() + (durationSeconds * 1000) : 0;

        plugin.getConfig().set("maintenance.enabled", active);
        plugin.getConfig().set("maintenance.reason", reason.isEmpty() ? "Wartungsarbeiten" : reason);
        plugin.getConfig().set("maintenance.end-time", endTime);
        plugin.saveConfig();

        if (active) {
            Component kickMsg = getFormattedKickMessage();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission("pickelmaintenance.bypass")) {
                    player.kick(kickMsg);
                }
            }
        }
    }

    public Component getFormattedKickMessage() {
        String raw = plugin.getConfig().getString("messages.kick-message", "");
        raw = raw.replace("%reason%", getReason())
                .replace("%time%", TimeUtil.formatRemainingTime(getEndTime()));
        return Component.text(HexColorUtil.colorize(raw));
    }

    public String getReason() {
        return plugin.getConfig().getString("maintenance.reason", "Wartungsarbeiten");
    }

    public long getEndTime() {
        return plugin.getConfig().getLong("maintenance.end-time", 0);
    }
}