package me.alek.obfuscation.impl.classes;

import me.alek.model.AttributeStatus;
import me.alek.enums.Risk;
import me.alek.obfuscation.impl.AbstractClassObfFeature;
import me.alek.obfuscation.impl.AbstractLengthBasedObfFeature;
import org.objectweb.asm.tree.ClassNode;

public class InterfaceLengthFeature extends AbstractLengthBasedObfFeature implements AbstractClassObfFeature {
    @Override
    public void affectAttributeStatus(AttributeStatus attributeStatusModel, ClassNode classNode) {
        for (String interfacePath : classNode.interfaces) {
            affectAttributeStatus(attributeStatusModel, fixClass(interfacePath));
        }
    }
    @Override
    public Risk getFeatureRisk() {
        return Risk.HIGH;
    }


    @Override
    public String getName() {
        return "Interface Name Length";
    }

}
