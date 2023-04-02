package me.alek.handlers.impl;

import me.alek.cache.containers.SkyRageContainer;
import me.alek.enums.MalwareType;
import me.alek.handlers.types.EncryptedKeyHandler;
import me.alek.handlers.types.nodes.MalwareNode;
import me.alek.handlers.types.ParseHandler;
import me.alek.model.PluginProperties;

import java.io.File;
import java.nio.file.Path;

public class SkyRageCheck extends EncryptedKeyHandler implements ParseHandler, MalwareNode {

    private SkyRageContainer container;

    @Override
    public void parse() {
        if (container != null) return;
        container = new SkyRageContainer();
    }

    @Override
    public MalwareType getType() {
        return MalwareType.SKYRAGE;
    }

    @Override
    public String[] getURLKeys() {
        return new String[]{"http://files.skyrage.de/update", "http://files.skyrage.de/mvd"};
    }

    @Override
    public String preProcessJAR(File file, Path rootFolder, PluginProperties pluginProperties) {
        /*if (resolve(rootFolder, "plugin-config.bin")) {
            return "Config-bin";
        }*/
        for (String metaInfString : container.getList()) {
            if (resolve(rootFolder, metaInfString)) {
                return "META-INF library";
            }
        }
        return null;
    }

}
