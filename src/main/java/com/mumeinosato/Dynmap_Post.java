package com.mumeinosato;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public final class Dynmap_Post extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Dynmap_Post has been enabled!");

        int deleteAfterHours = 1;
        try (InputStream in = getResource("config.yaml")) {
            if (in != null) {
                Yaml yaml = new Yaml();
                Map<String, Object> config = yaml.load(in);
                if (config.containsKey("delete_after_hours")) {
                    deleteAfterHours = (int) config.get("delete_after_hours");
                }
            }
        } catch (Exception e) {
            getLogger().warning("config.yamlの読み込みに失敗: " + e.getMessage());
        }

        DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {
            @Override
            public void apiEnabled(DynmapCommonAPI api) {
                MarkerAPI markerAPI = api.getMarkerAPI();

                MarkerSet set = markerAPI.createMarkerSet("f0ea9bcc-815e-a831-a231-141da67b1003", "みんなの投稿", null, false);

                set.deleteMarkerSet();
            }
        });

        getCommand("comment").setExecutor(new CommandClass(deleteAfterHours));
    }

    @Override
    public void onDisable() {
        getLogger().info("Dynmap_Post has been disabled!");
    }
}
