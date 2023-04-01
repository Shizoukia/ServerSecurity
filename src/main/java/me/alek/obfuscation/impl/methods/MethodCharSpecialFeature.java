package me.alek.obfuscation.impl.methods;

import me.alek.model.AttributeStatus;
import me.alek.enums.Risk;
import me.alek.obfuscation.impl.AbstractMethodObfFeature;
import me.alek.obfuscation.impl.AbstractObfFeature;
import org.objectweb.asm.tree.MethodNode;

import java.util.regex.Pattern;

public class MethodCharSpecialFeature extends AbstractObfFeature implements AbstractMethodObfFeature {

    private final Pattern regexPattern;

    public MethodCharSpecialFeature() {
        this.regexPattern = Pattern.compile("[A-Za-z0-9_\\$]");
    }

    @Override
    public void affectAttributeStatus(AttributeStatus attributeStatusModel, MethodNode method) {
        String methodName = method.name;

        affectAttributeStatusGlobally(attributeStatusModel, !regexPattern.matcher(methodName).find());
    }

    @Override
    public Risk getFeatureRisk() {
        return Risk.HIGH;
    }

    @Override
    public String getName() {
        return "Method Special Char";
    }

    @Override
    public boolean feedback(AttributeStatus attributeStatusModel) {
        return attributeStatusModel.getAbnormalCount() >= 1;
    }
}
