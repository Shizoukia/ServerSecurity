package me.alek.obfuscation.impl.fields;

import me.alek.model.AttributeStatus;
import me.alek.utils.Utils;
import me.alek.enums.Risk;
import me.alek.obfuscation.impl.AbstractFieldObfFeature;
import me.alek.obfuscation.impl.AbstractObfFeature;
import org.objectweb.asm.tree.FieldNode;

import java.util.Arrays;
import java.util.List;

public class StringBigramsFeature extends AbstractObfFeature implements AbstractFieldObfFeature {

    private final List<String> rareBigramsMap;

    public StringBigramsFeature() {
        rareBigramsMap = Arrays.asList(
                "BF", "BG", "BK", "BQ", "BW", "BX", "BZ",
                "CJ", "CV", "CW", "CX", "CZ",
                "DK", "DX", "DZ",
                "FB", "FD", "FH", "FJ", "FK", "FN", "FP", "FQ", "FV", "FW", "FX", "FZ",
                "GB", "GC", "GJ", "GK", "GP", "GQ", "GV", "GX", "GZ",
                "HG", "HJ", "HK", "HV", "HX", "HZ",
                "JB", "JC", "JD", "JF", "JK", "JL", "JM", "JN", "JP", "JQ", "JR", "JS", "JT", "JV", "JW", "JX", "JY", "JZ",
                "KC", "KJ", "KK", "KQ", "KV", "KX", "KZ",
                "LJ", "LQ", "LX", "LZ",
                "MJ", "MK", "MQ", "MV", "MX", "MZ",
                "PG", "PJ", "PQ", "PV", "PX", "PZ",
                "QA", "QB", "QC", "QD", "QE", "QF", "QG", "QH", "QI", "QJ", "QK", "QL", "QM", "QN", "QO", "QP", "QQ", "QR", "QS", "QT", "QV", "QW", "QX", "QY", "QZ",
                "SJ", "SX", "SZ",
                "TJ", "TK", "TQ", "TX", "TZ",
                "UQ", "UW",
                "VB", "VC", "VD", "VF", "VG", "VH", "VJ", "VK", "VL", "VM", "VN", "VP", "VQ", "VT", "VV", "VW", "VX", "VZ",
                "WG", "WJ", "WQ", "WV", "WW", "WX", "WZ",
                "XB", "XD", "XG", "XJ", "XK", "XM", "XN", "XQ", "XR", "XS", "XW", "XZ",
                "YJ", "YK", "YQ", "YV", "YX", "YY",
                "ZB", "ZC", "ZD", "ZF", "ZG", "ZJ", "ZK", "ZM", "ZN", "ZP", "ZQ", "ZR", "ZS", "ZT", "ZV", "ZW", "ZX"
        );
    }

    @Override
    public void affectAttributeStatus(AttributeStatus attributeStatusModel, FieldNode field) {
        Object valueObject = field.value;
        if (valueObject instanceof String) {
            String stringObject = (String) valueObject;
            for (String bigram : Utils.getBigrams(stringObject)) {
                affectAttributeStatusGlobally(attributeStatusModel, (rareBigramsMap.contains(bigram)));
            }
        }
    }

    @Override
    public Risk getFeatureRisk() {
        return Risk.HIGH;
    }

    @Override
    public String getName() {
        return "String Rare Bigrams";
    }

    @Override
    public boolean feedback(AttributeStatus attributeStatusModel) {
        return attributeStatusModel.generatePercentage() > 0.05;
    }
}
