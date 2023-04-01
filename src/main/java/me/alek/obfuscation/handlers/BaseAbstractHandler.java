package me.alek.obfuscation.handlers;

import me.alek.model.AttributeStatus;
import me.alek.model.FeatureResponse;
import me.alek.obfuscation.impl.AbstractObfFeature;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;

public abstract class BaseAbstractHandler implements AbstractHandler {

    @Override
    public HashMap<String, FeatureResponse> check(ClassNode classNode) {
        HashMap<String, FeatureResponse> featureResponseModels = new HashMap<>();

        for (AbstractObfFeature abstractObfFeature : getImplementedFeatures()) {

            AttributeStatus attributeStatusModel = new AttributeStatus();

            affectAttributes(classNode, abstractObfFeature, attributeStatusModel);
            boolean feedback = abstractObfFeature.feedback(attributeStatusModel);

            FeatureResponse featureResponseModel = new FeatureResponse(feedback, attributeStatusModel, abstractObfFeature.getFeatureRisk());
            featureResponseModels.put(abstractObfFeature.getName(), featureResponseModel);
        }
        return featureResponseModels;
    }

    public abstract void affectAttributes(ClassNode classNode, AbstractObfFeature abstractObfFeature, AttributeStatus attributeStatusModel);
}


