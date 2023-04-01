package me.alek.obfuscation.handlers;

import me.alek.model.FeatureResponse;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AbstractObfHandler {
    private final ArrayList<AbstractHandler> implementedAbstractHandlers = new ArrayList<>();

    public AbstractObfHandler(AbstractHandler... implementedAbstractHandlers) {
        this.implementedAbstractHandlers.addAll(Arrays.asList(implementedAbstractHandlers));
    }

    public HashMap<String, FeatureResponse> check(ClassNode classNode) {

        HashMap<String, FeatureResponse> featureResponseModels = new HashMap<>();
        for (AbstractHandler abstractHandler : implementedAbstractHandlers) {

            featureResponseModels.putAll(abstractHandler.check(classNode));
        }

        return featureResponseModels;
    }

}
