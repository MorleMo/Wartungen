package de.devloper.pickelMaintenance.listener;

import de.devloper.pickelMaintenance.PickelMaintenance;
import de.devloper.pickelMaintenance.manager.MaintenanceManager;
import de.devloper.pickelMaintenance.util.HexColorUtil;
import de.devloper.pickelMaintenance.util.TimeUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class MaintenanceListener implements Listener {

    private final PickelMaintenance plugin;
    private final MaintenanceManager manager;

    public MaintenanceListener(PickelMaintenance plugin, MaintenanceManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(PlayerLoginEvent event) {
        if (!manager.isMaintenanceActive()) return;

        // Nur noch prüfen, ob der Spieler Operator (OP) ist
        if (!event.getPlayer().isOp()) {
            event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    manager.getFormattedKickMessage()
            );
        }
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        if (manager.isMaintenanceActive()) {
            String motdRaw = plugin.getConfig().getString("messages.motd", "")
                    .replace("%time%", TimeUtil.formatRemainingTime(manager.getEndTime()))
                    .replace("%reason%", manager.getReason());
            event.motd(Component.text(HexColorUtil.colorize(motdRaw)));
        }
    }
}