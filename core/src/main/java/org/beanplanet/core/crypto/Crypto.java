package org.beanplanet.core.crypto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.beanplanet.core.codec.Hex;
import org.beanplanet.core.io.resource.InputStreamResource;
import org.beanplanet.core.io.resource.OutputStreamResource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;

public class Crypto {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String... args) throws Exception {
        final String keysFiele = args[0];
        final boolean isEncrypt = "-e".endsWith(args[1]);
        final int keyIndex = args.length >= 3 ? Integer.parseInt(args[2]) : 0;
        Keys keys = objectMapper.readValue(new File(keysFiele), Keys.class);

        if (isEncrypt) encrypt(keys, keyIndex);
        else decrypt(keys, keyIndex);
    }

    private static void encrypt(final Keys keys, final int keyIndex) {
        CryptoUtil.encrypt("AES", loadSymmetricKey(keys.privateKeys[keyIndex]), new InputStreamResource(System.in), new OutputStreamResource(System.out));
    }

    private static void decrypt(final Keys keys, final int keyIndex) {
        CryptoUtil.decrypt("AES", loadSymmetricKey(keys.privateKeys[keyIndex]), new InputStreamResource(System.in), new OutputStreamResource(System.out));
    }

    private static SecretKey loadSymmetricKey(final String keyEntry) {
        String[] keySpec = keyEntry.split(":");
        return new SecretKeySpec(Hex.decodeFromHexString(keySpec[2]), "AES");
    }

    private static class Keys {
        @JsonProperty("private_keys")
        public String[] privateKeys;
    }
}
