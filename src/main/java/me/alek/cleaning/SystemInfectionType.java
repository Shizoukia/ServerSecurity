package me.alek.cleaning;

import lombok.Getter;

import java.io.*;
import java.util.function.Supplier;

public enum SystemInfectionType {

    STARTUP(new File(System.getenv("APPDATA") + File.separator + "Microsoft" + File.separator + "Windows"
            + File.separator + "Start Menu" + File.separator + "Programs" + File.separator + "Startup"),
            new Supplier<Boolean>() {

                @Override
                public Boolean get() {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("schtasks").getInputStream()));
                        String line;
                        while (true) {
                            line = bufferedReader.readLine();
                            if (line == null) return false;

                            if (!line.contains("javaw")) {
                                SystemCleaner cleaner = OperatingSystem.WINDOWS.getCleaner();
                                return ((WindowsSystemCleaner)cleaner).isCheckedCompromised(SystemInfectionType.STARTUP.getDirectory());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }),
    SCHEDULER(new File(System.getenv("APPDATA") + File.separator + ".." + File.separator + "LocalLow"
            + File.separator + "Microsoft" + File.separator + "Internet Explorer" + File.separator + "DOMStore"),
            new Supplier<Boolean>() {

                @Override
                public Boolean get() {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("schtasks").getInputStream()));
                        String line;
                        while (true) {
                            line = bufferedReader.readLine();
                            if (line == null) return false;

                            if (!line.contains("MicrosoftEdgeUpdateTaskMachineVM")) {
                                SystemCleaner cleaner = OperatingSystem.WINDOWS.getCleaner();
                                return ((WindowsSystemCleaner)cleaner).isCheckedCompromised(SystemInfectionType.SCHEDULER.getDirectory());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

    @Getter private final File directory;
    @Getter private final Supplier<Boolean> isInfected;

    SystemInfectionType(File directory, Supplier<Boolean> isInfected) {
        this.directory = directory;
        this.isInfected = isInfected;
    }
}
