package me.alek.cache.containers;

import org.bukkit.Bukkit;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CacheContainer {

    private final CacheMap<ClassNode> cachedClasses = new CacheMap<>();

    public ClassNode fetchClass(Path filePath, Path path) {
        try {
            if (!cachedClasses.contains(filePath, path)) {
                return loadClass(filePath, path);
            }
            return cachedClasses.get(filePath, path);
        } catch (Exception ex) {
        }
        return null;
    }

    private ClassNode loadClass(Path filePath, Path path) {
        try {
            ClassReader classReader = new ClassReader(Files.newInputStream(path));
            ClassNode classNode = new ClassNode();

            classReader.accept(classNode, 0);
            cachedClasses.put(filePath, path, classNode);
            return classNode;
        } catch (Exception e) {
            cachedClasses.put(filePath, path, null);
            return null;
        }
    }

    public void clearCache(Path filePath) {
        cachedClasses.remove(filePath.toAbsolutePath());
    }

    private static class CacheMap<T> {

        private final Function<? super Path, ? extends Map<Path, T>> nestedCreator = p -> new HashMap<>();
        private final Map<Path, Map<Path, T>> map = new ConcurrentHashMap<>();

        public T put(Path filePath, Path path, T value) {
            return map.computeIfAbsent(filePath.toAbsolutePath(), nestedCreator).put(path, value);
        }

        public T get(Path filePath, Path path) {
            Map<Path, T> m = map.get(filePath.toAbsolutePath());
            if (m != null) {
                return m.get(path);
            }
            return null;
        }

        public Map<Path, T> remove(Path filePath) {
            return map.remove(filePath.toAbsolutePath());
        }

        public boolean contains(Path filePath, Path path) {
            Map<Path, T> m = map.get(filePath.toAbsolutePath());
            return m != null && m.containsKey(path);
        }
    }
}
