package me.alek.cache.registery;

import me.alek.cache.Container;
import me.alek.cache.Registery;
import me.alek.obfuscation.handlers.AbstractObfHandler;
import me.alek.obfuscation.handlers.ClassObfHandler;
import me.alek.obfuscation.handlers.FieldObfHandler;
import me.alek.obfuscation.handlers.MethodObfHandler;
import me.alek.obfuscation.impl.classes.ClassLengthFeature;
import me.alek.obfuscation.impl.classes.InterfaceLengthFeature;
import me.alek.obfuscation.impl.classes.RepetitiveWordFeature;
import me.alek.obfuscation.impl.classes.InnerClassLengthFeature;
import me.alek.obfuscation.impl.fields.*;
import me.alek.obfuscation.impl.methods.*;

import java.util.Arrays;
import java.util.List;

public class ObfuscationRegistery extends Registery<AbstractObfHandler> {

    public ObfuscationRegistery(Container<AbstractObfHandler> container) {
        super(container);
    }

    @Override
    public List<AbstractObfHandler> getElements() {
        return Arrays.asList(new AbstractObfHandler(
                new ClassObfHandler(
                        new ClassLengthFeature(),
                        new RepetitiveWordFeature(),
                        new InterfaceLengthFeature(),
                        new InnerClassLengthFeature()
                ),
                new MethodObfHandler(
                        new MethodCharOccurenceFeature(),
                        new MethodCharSpecialFeature(),
                        new MethodNoLetterCharFeature(),
                        new MethodLengthFeature(),
                        new AllatoriMethodFeature()
                ),
                new FieldObfHandler(
                        new FieldCharOccurenceFeature(),
                        new FieldCharSpecialFeature(),
                        new FieldLengthFeature(),
                        new NumberSizeFeature(),
                        new FieldByteArrayFeature(),
                        new StringNonAsciiFeature(),
                        new StringVowelFeature(),
                        new StringBigramsFeature(),
                        new StringWordLengthFeature()
                )
        ));
    }
}
