package me.alek.security;

import com.google.common.base.Charsets;
import me.alek.AntiMalwarePlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class SecurityConfig {

    private FileConfiguration yamlConfiguration;
    private final SecurityManager manager;

    public SecurityConfig(SecurityManager manager) {
        this.manager = manager;
        File dataFolder = AntiMalwarePlugin.getInstance().getDataFolder();
        dataFolder.mkdirs();
        this.yamlConfiguration = AntiMalwarePlugin.getInstance().getConfig();

        this.yamlConfiguration.options().copyDefaults(true);
        AntiMalwarePlugin.getInstance().saveDefaultConfig();

        Bukkit.getLogger().info("REEEEEEEEEEEEEEEEEEEEEEE");
    }

    public void reload() {
        if (this.yamlConfiguration == null) return;
        AntiMalwarePlugin.getInstance().reloadConfig();
    }

    public YamlConfiguration load(final File file) {
        if (file == null) {
            return null;
        }
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (final IOException | InvalidConfigurationException ex) {
        }
        return config;
    }

    public FileConfiguration getFileConfiguration() {
        return yamlConfiguration;
    }

}
