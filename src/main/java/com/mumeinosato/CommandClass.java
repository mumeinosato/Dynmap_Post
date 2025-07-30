package com.mumeinosato;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

public class CommandClass implements CommandExecutor {
    private final int deleteAfterHours;

    public CommandClass(int deleteAfterHours) {
        this.deleteAfterHours = deleteAfterHours;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("comment")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage("プレイヤーのみ実行できます。");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("/comment <content>");
            return true;
        }

        String comment = args[0];
        double x, y, z;
        x = ((Player) sender).getX();
        y = ((Player) sender).getY();
        z = ((Player) sender).getZ();

        DynmapCommonAPI api = (DynmapCommonAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if (api == null) {
            return true;
        }

        MarkerAPI markerAPI = api.getMarkerAPI();
        MarkerSet tempSet = markerAPI.getMarkerSet("f0ea9bcc-815e-a831-a231-141da67b1003");
        if (tempSet == null) {
            tempSet = markerAPI.createMarkerSet("f0ea9bcc-815e-a831-a231-141da67b1003", "みんなの投稿", null, false);
        }
        final MarkerSet set = tempSet;

        final MarkerIcon icon = markerAPI.getMarkerIcon("comment");

        final String markerId = "comment_" + System.currentTimeMillis();
        set.createMarker(
                markerId,
                comment,
                ((Player) sender).getWorld().getName(),
                x, y, z,
                icon,
                false
        );

        long delay = deleteAfterHours * 60L * 60L * 20L;
        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () -> {
            if (set.findMarker(markerId) != null) {
                set.findMarker(markerId).deleteMarker();
            }
        }, delay);

        return true;
    }
}