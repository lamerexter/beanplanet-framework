package org.beanplanet.core.crypto;

import org.beanplanet.core.io.resource.OutputStreamResource;
import org.beanplanet.core.io.resource.StringResource;

import java.io.FileOutputStream;

public class Hex {
    public static void main(String ... args) throws Exception {
        boolean isEncode = args.length == 0 || "-e".equals(args[0]);
        if (isEncode) new org.beanplanet.core.codec.Hex().encode(System.in, System.out);
        else new org.beanplanet.core.codec.Hex().decode(System.in, System.out);
    }
}
