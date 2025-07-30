package com.mumeinosato;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

public class CommandClass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("comment")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage("プレイヤーのみ実行できます。");
            return true;
        }
        if (args.length < 4) {
            sender.sendMessage("/comment <タイトル> <x> <y> <z>");
            return true;
        }

        String title = args[0];
        double x, y, z;
        x = ((Player) sender).getX();
        y = ((Player) sender).getY();
        z = ((Player) sender).getZ();

        // Dynmap API取得
        DynmapCommonAPI api = (DynmapCommonAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if (api == null) {
            sender.sendMessage("Dynmapが見つかりません。");
            return true;
        }
        MarkerAPI markerAPI = api.getMarkerAPI();
        MarkerSet set = markerAPI.getMarkerSet("f0ea9bcc-815e-a831-a231-141da67b1003");
        if (set == null) {
            set = markerAPI.createMarkerSet("f0ea9bcc-815e-a831-a231-141da67b1003", "みんなの投稿", null, false);
        }
        MarkerIcon icon = markerAPI.getMarkerIcon("comment");

        String markerId = "comment_" + System.currentTimeMillis();
        set.createMarker(
                markerId,
                title,
                ((Player) sender).getWorld().getName(),
                x, y, z,
                icon,
                false
        );
        sender.sendMessage("マーカーを作成しました！");
        return true;
    }
}