package me.alek.logging;

import lombok.Getter;
import me.alek.AntiMalwarePlugin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public abstract class AbstractLogger {

    @Getter private final Logger logger;

    public AbstractLogger() {
        final File file = new File(AntiMalwarePlugin.getInstance().getDataFolder(), getFileName());
        Formatter formatter = new Formatter() {

            @Override
            public String format(LogRecord record) {
                return new Date(record.getMillis()).toString() + ": " + record.getMessage() + "\n";
            }
        };
        this.logger = Logger.getLogger(this.getClass().getName());
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            FileHandler fileHandler = new FileHandler(file.getAbsolutePath(), 0, 1, true);
            fileHandler.setFormatter(formatter);
            this.logger.addHandler(fileHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.logger.setUseParentHandlers(false);
        this.logger.setLevel(Level.ALL);
        this.logger.setFilter(record -> true);
        Logger parent = this.logger.getParent();
        while (parent != null) {
            parent.setLevel(Level.ALL);
            parent.setFilter(record -> true);
            parent = parent.getParent();
        }
    }

    public void log(Level level, String str) {
        getLogger().log(level, str);
    }

    protected abstract String getFileName();

}
