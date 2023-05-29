package me.alek.cleaning;

import org.bukkit.entity.Player;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class WindowsSystemCleaner implements SystemCleaner {

    private File compromisedFile;

    @Override
    public void clean(SystemInfectionType type, Player player) throws IOException {
        if (compromisedFile.canWrite()) {
            player.sendMessage("§8[§6AntiMalware§8] §cDit system er stadig smittet! Sørg for, at du har adgang til at skrive i filer: §7" +
                    type.getDirectory().getAbsolutePath());
            return;
        }
        if (type.equals(SystemInfectionType.STARTUP)) {
            Files.deleteIfExists(compromisedFile.toPath());
        }
        if (type.equals(SystemInfectionType.SCHEDULER)) {
            Process process = Runtime.getRuntime().exec("schtasks /Delete /TN \"MicrosoftEdgeUpdateTaskMachineVM\" /F");
            try {
                if (process.waitFor() == 0) {
                    this.killProcesses(compromisedFile);
                    Files.deleteIfExists(compromisedFile.toPath());
                    File kernelCertsLog = new File(System.getenv("APPDATA") + File.separator + ".." + File.separator + "Local" + File.separator + "Temp" + File.separator + "kernel-certs-debug4917.log");
                    if (this.isFileInfected(kernelCertsLog)) {
                        this.killProcesses(kernelCertsLog);
                        Files.deleteIfExists(kernelCertsLog.toPath());
                    }
                } else {
                    player.sendMessage("§8[§6AntiMalware§8] §cDit system er stadig smittet! Der opstod en fejl ved slettelse af scheduled task.");
                    player.sendMessage("§cPrøv at skriv følgende i en kommandoprompt (ikke mc): schtasks /Delete /TN \"MicrosoftEdgeUpdateTaskMachineVM\" /F");
                    player.sendMessage("§cDerefter gå ind i \"C:/Users/DINBRUGER/AppData/LocalLow/Microsoft/Internet Explorer/DOMStore\"");
                    player.sendMessage("§cOg se om der er en fil ved navn \"microsoft-vm-core\", hvis den eksisterer, så slet den.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void killProcesses(File jarFile) {
        try {
            List<String> pids = this.getProcessIdsForJar(jarFile);
            for (String pid : pids) {
                Runtime.getRuntime().exec("taskkill /F /PID " + pid).waitFor();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getProcessIdsForJar(File jarFile) throws IOException {
        List<String> processIds = new ArrayList<>();
        Process process = Runtime.getRuntime().exec("wmic process get CommandLine,ProcessId");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(jarFile.getName())) {
                    String[] values = line.trim().split("\\s+");
                    processIds.add(values[values.length - 1]);
                }
            }
        }
        return processIds;
    }

    @Override
    public SystemInfectionType getInfection() throws IOException {
        for (SystemInfectionType type : SystemInfectionType.values()) {
            if (type.getIsInfected().get()) {
                return type;
            }
        }
        return null;
    }

    public boolean isCheckedCompromised(File directory) throws IOException {
        List<File> files = SkyrageJarCleaner.findAllJars(directory);
        for (File file : files) {
            if (isFileInfected(file)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFileInfected(File file) throws IOException {
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<? extends ZipEntry> entries = jarFile.entries();
            String eventuallyMainClass = Optional.ofNullable(jarFile.getManifest()).map(m -> m.getMainAttributes().getValue("Main-Class")).map(s -> s.replace(".", "/")).orElse(null);

            while (entries.hasMoreElements()) {

                ZipEntry zipEntry = entries.nextElement();
                zipEntry.setCompressedSize(-1L);

                if (!zipEntry.getName().endsWith(".class")) continue;
                try (InputStream in = jarFile.getInputStream(zipEntry)) {

                    ClassReader classReader = new ClassReader(in);
                    ClassNode classNode = new ClassNode();
                    classReader.accept(classNode, 0);

                    if (classNode.name == null) continue;
                    if (eventuallyMainClass == null) continue;
                    if (!((classNode.name).equals(eventuallyMainClass))) continue;

                    for (FieldNode fieldNode : classNode.fields) {
                        if (!fieldNode.value.toString().contains("skyrage.de") || fieldNode.value.toString().contains("throwable.in")) continue;
                        compromisedFile = file;
                        return true;
                    }
                }

            }
            return false;
        }
    }
}
