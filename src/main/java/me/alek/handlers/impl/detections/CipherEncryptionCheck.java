package me.alek.handlers.impl.detections;

import me.alek.enums.Risk;
import me.alek.handlers.types.InsnInvokeHandler;
import me.alek.handlers.types.OnlySourceLibraryHandler;
import me.alek.handlers.types.nodes.DetectionNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.io.File;
import java.nio.file.Path;

public class CipherEncryptionCheck extends InsnInvokeHandler implements DetectionNode, OnlySourceLibraryHandler {

    public CipherEncryptionCheck() {
        super(MethodInsnNode.class);
    }

    @Override
    public String preProcessJAR(File file, Path rootFolder) {
        return null;
    }

    @Override
    public String processAbstractInsn(AbstractInsnNode abstractInsnNode) {
        MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
        if (!methodInsnNode.owner.equals("javax.crypto.Cipher")) return null;
        if (!methodInsnNode.name.equals("getInstance")) return null;
        return "";
    }

    @Override
    public String getType() {
        return "Cipher Encryption";
    }

    @Override
    public Risk getRisk() {
        return Risk.LOW;
    }
}
