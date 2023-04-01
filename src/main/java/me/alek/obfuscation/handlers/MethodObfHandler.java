package me.alek.obfuscation.handlers;

import me.alek.model.AttributeStatus;
import me.alek.obfuscation.impl.AbstractMethodObfFeature;
import me.alek.obfuscation.impl.AbstractObfFeature;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodObfHandler extends BaseAbstractHandler {

    private final ArrayList<AbstractObfFeature> implementedAbstractMethodFeatures = new ArrayList<>();

    public MethodObfHandler(AbstractObfFeature... implementedAbstractMethodFeatures) {
        this.implementedAbstractMethodFeatures.addAll(Arrays.asList(implementedAbstractMethodFeatures));
    }

    @Override
    public List<AbstractObfFeature> getImplementedFeatures() {
        return implementedAbstractMethodFeatures;
    }

    @Override
    public void affectAttributes(ClassNode classNode, AbstractObfFeature abstractObfFeature, AttributeStatus attributeStatusModel) {
        AbstractMethodObfFeature feature = (AbstractMethodObfFeature) abstractObfFeature;
        for (MethodNode method : classNode.methods) {
            feature.affectAttributeStatus(attributeStatusModel, method);
        }
    }
}
