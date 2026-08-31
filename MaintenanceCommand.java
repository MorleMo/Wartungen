package de.devloper.pickelMaintenance.commands;

import de.devloper.pickelMaintenance.PickelMaintenance;
import de.devloper.pickelMaintenance.manager.MaintenanceManager;
import de.devloper.pickelMaintenance.util.HexColorUtil;
import de.devloper.pickelMaintenance.util.TimeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MaintenanceCommand implements CommandExecutor {

    private final PickelMaintenance plugin;
    private final MaintenanceManager manager;

    public MaintenanceCommand(PickelMaintenance plugin, MaintenanceManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("pickelmaintenance.admin")) {
            sender.sendMessage(HexColorUtil.colorize("&cDazu hast du keine Rechte!"));
            return true;
        }

        String prefix = HexColorUtil.colorize(plugin.getConfig().getString("messages.prefix", ""));

        if (args.length == 0) {
            sender.sendMessage(prefix + HexColorUtil.colorize(plugin.getConfig().getString("messages.usage", "")));
            return true;
        }

        if (args[0].equalsIgnoreCase("aus") || args[0].equalsIgnoreCase("off")) {
            manager.setMaintenance(false, 0, "");
            sender.sendMessage(prefix + HexColorUtil.colorize(plugin.getConfig().getString("messages.deactivated", "")));
            return true;
        }

        if (args[0].equalsIgnoreCase("an") || args[0].equalsIgnoreCase("on")) {
            long durationSeconds = 0;
            int reasonStartIndex = 1;

            if (args.length > 1) {
                long parsed = TimeUtil.parseDurationToMillis(args[1]);
                if (parsed > 0) {
                    durationSeconds = parsed;
                    reasonStartIndex = 2;
                }
            }

            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = reasonStartIndex; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }
            String reason = reasonBuilder.toString().trim();

            manager.setMaintenance(true, durationSeconds, reason);

            String timeFormatted = TimeUtil.formatRemainingTime(manager.getEndTime());
            String successMsg = plugin.getConfig().getString("messages.activated", "")
                    .replace("%time%", timeFormatted)
                    .replace("%reason%", manager.getReason());

            sender.sendMessage(prefix + HexColorUtil.colorize(successMsg));
            return true;
        }

        sender.sendMessage(prefix + HexColorUtil.colorize(plugin.getConfig().getString("messages.usage", "")));
        return true;
    }
}