package me.alek.obfuscation.handlers;

import me.alek.model.AttributeStatus;
import me.alek.obfuscation.impl.AbstractClassObfFeature;
import me.alek.obfuscation.impl.AbstractObfFeature;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassObfHandler extends BaseAbstractHandler {

    private final ArrayList<AbstractObfFeature> implementedAbstractClassFeatures = new ArrayList<>();

    public ClassObfHandler(AbstractObfFeature... implementedAbstractClassFeatures) {
        this.implementedAbstractClassFeatures.addAll(Arrays.asList(implementedAbstractClassFeatures));
    }

    @Override
    public List<AbstractObfFeature> getImplementedFeatures() {
        return implementedAbstractClassFeatures;
    }

    @Override
    public void affectAttributes(ClassNode classNode, AbstractObfFeature abstractObfFeature, AttributeStatus attributeStatusModel) {
        AbstractClassObfFeature feature = (AbstractClassObfFeature) abstractObfFeature;
        feature.affectAttributeStatus(attributeStatusModel, classNode);
    }
}
