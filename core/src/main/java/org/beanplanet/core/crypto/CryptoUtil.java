/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beanplanet.core.crypto;

import org.beanplanet.core.codec.Base64;
import org.beanplanet.core.codec.Hex;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.util.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A static utility class for all things related to cypher suites.
 *
 * @author Gary Watson
 * @since 20th March, 2004
 */
public class CryptoUtil {
    /**
     * The default symmetric algorithm used to generated keys when no algorithm is explicilty specified.
     */
    public static final String DEFAULT_SYMMETRIC_ALGORITHM = "Blowfish";

    /**
     * The default asymmetric algorithm used to generated keys when no algorithm is explicilty specified.
     */
    public static final String DEFAULT_ASYMMETRIC_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * The default size of asymmetric keys generated when no key size is explicilty specified.
     */
    public static final int DEFAULT_GENERATED_ASYMMETRIC_KEY_SIZE = 1024;

    /**
     * The default size of symmetric keys generated when no key size is explicilty specified.
     */
    public static final int DEFAULT_GENERATED_SYMMETRIC_KEY_SIZE = 128;

    // private static final KeyPair embeddedKeyPair;
    private static final SecretKey embeddedSecretKey;

    // The default key sizes to use, per algorithm, if not provided by clients
    private static Map<String, Integer> defaultKeySizeMap = new HashMap<String, Integer>();

    static {
        embeddedSecretKey = generateSymmetricKey(DEFAULT_SYMMETRIC_ALGORITHM,
                                                 Base64.decode("74TqjEjfgrmqCfvpd2vU7A=="));

        // Uppercase algorithms to default key sizes map
        defaultKeySizeMap.put("AES", new Integer(128));
        defaultKeySizeMap.put("BLOWFISH", new Integer(128));
        defaultKeySizeMap.put("DES", new Integer(56));
        defaultKeySizeMap.put("DESEDE", new Integer(128));
        defaultKeySizeMap.put("RC2", new Integer(128));
        defaultKeySizeMap.put("RC4", new Integer(128));
        defaultKeySizeMap.put("RC5", new Integer(128));
        defaultKeySizeMap.put("RSA", new Integer(2048));
    }

    public static int getDefaultSymmetricKeySize(String algorithm) {
        Integer keySize = (Integer) defaultKeySizeMap.get(algorithm.toUpperCase());
        return keySize != null ? keySize.intValue() : DEFAULT_GENERATED_SYMMETRIC_KEY_SIZE;
    }

    public static int getDefaultASymmetricKeySize(String algorithm) {
        Integer keySize = (Integer) defaultKeySizeMap.get(algorithm.toUpperCase());
        return keySize != null ? keySize.intValue() : DEFAULT_GENERATED_ASYMMETRIC_KEY_SIZE;
    }

    /**
     * Gets a list of the supported security providers.
     *
     * @return a list of the supported security providers.
     */
    public static List<Provider> getProviders() {
        return Arrays.asList(Security.getProviders());
    }

    /**
     * Gets a list of the supported cryptographic cipher algorithms.
     *
     * @return a list of the supported cryptographic cipher algorithms.
     */
    public static List<String> getCipherAlgorithms() {
        return Arrays.stream(Security.getProviders())
                .flatMap(p -> p.getServices().stream())
                .filter(s -> "Cipher".equals(s.getType()))
                .map(Provider.Service::getAlgorithm)
                .collect(Collectors.toList());
    }

    /**
     * Generates a public/private keypair using the specified algorithm and key size.
     *
     * @param algorithm the algorithm used to generated the keys, such as "DSA" or "RSA"
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keySize the size of the keys to generated, such as 512 or 1024
     * @return the key pair generated
     * @throws SecurityException if an error occurs generating the key pair
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static KeyPair generateKeyPair(String algorithm, String providerName, int keySize) throws SecurityException {
        try {
            KeyPairGenerator keyGen = providerName != null ? KeyPairGenerator.getInstance(getKeyTypeFromAlgorithm(algorithm),
                                                                                          providerName)
                    : KeyPairGenerator.getInstance(getKeyTypeFromAlgorithm(algorithm));
            keyGen.initialize(keySize);
            KeyPair keypair = keyGen.genKeyPair();
            return keypair;
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured encrypting the data: ", th);
        }
    }

    /**
     * Generates a public/private keypair using the specified algorithm and key size.
     *
     * @param algorithm the algorithm used to generated the keys, such as "DSA" or "RSA"
     * @param keySize the size of the keys to generated, such as 512 or 1024
     * @return the key pair generated
     * @throws SecurityException if an error occurs generating the key pair
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize) throws SecurityException {
        return generateKeyPair(algorithm, null, keySize);
    }

    /**
     * Generates a public/private keypair using the specified algorithm and default key size.
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @return the key pair generated
     * @throws SecurityException if an error occurs generating the key pair
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #getDefaultASymmetricKeySize(String)
     */
    public static KeyPair generateKeyPair(String algorithm, String providerName) throws SecurityException {
        return generateKeyPair(algorithm, providerName, getDefaultASymmetricKeySize(algorithm));
    }

    /**
     * Generates a public/private keypair using the specified algorithm and default key size.
     *
     * @param algorithm the algorithm used to generated the keys, such as "DSA" or "RSA"
     * @return the key pair generated
     * @throws SecurityException if an error occurs generating the key pair
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #getDefaultASymmetricKeySize(String)
     */
    public static KeyPair generateKeyPair(String algorithm) throws SecurityException {
        return generateKeyPair(algorithm, getDefaultASymmetricKeySize(algorithm));
    }

    /**
     * Generates a public/private keypair using a default algorithm and with the specified key size.
     *
     * @param keySize the size of the keys to generated, such as 512 or 1024
     * @return the key pair generated
     * @throws SecurityException if an error occurs generating the key pair
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static KeyPair generateKeyPair(int keySize) throws SecurityException {
        return generateKeyPair(DEFAULT_ASYMMETRIC_ALGORITHM, keySize);
    }

    /**
     * Generates a public/private keypair using defaults for the algorithm and sizes of the keys generated.
     *
     * @return the key pair generated
     * @throws SecurityException if an error occurs generating the key pair
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #getDefaultASymmetricKeySize(String)
     */
    public static KeyPair generateKeyPair() throws SecurityException {
        return generateKeyPair(DEFAULT_ASYMMETRIC_ALGORITHM, getDefaultASymmetricKeySize(DEFAULT_ASYMMETRIC_ALGORITHM));
    }

    /**
     * Generates a secret key using the specified algorithm and key size.
     *
     * @param algorithm the algorithm used to generated the key, such as "DES" or "Blowfish"
     * @param keySize the size of the key to generated, such as 512 or 1024
     * @return the key generated
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     */
    public static SecretKey generateSymmetricKey(String algorithm, int keySize) throws SecurityException {
        return generateSymmetricKey(algorithm, null, keySize);
    }

    /**
     * Generates a secret key using the specified algorithm, provider and key size.
     *
     * @param algorithm the algorithm used to generated the key, such as "DES" or "Blowfish"
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keySize the size of the key to generated, such as 512 or 1024
     * @return the key generated
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     */
    public static SecretKey generateSymmetricKey(String algorithm, String providerName, int keySize) throws SecurityException {
        try {
            KeyGenerator keyGen = providerName != null ? KeyGenerator.getInstance(getKeyTypeFromAlgorithm(algorithm),
                                                                                  providerName)
                    : KeyGenerator.getInstance(getKeyTypeFromAlgorithm(algorithm));
            keyGen.init(keySize);
            SecretKey key = keyGen.generateKey();
            return key;
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured generating the secret key: ", th);
        }
    }

    /**
     * Generates a secret key using the specified algorithm, provider and a default key size.
     *
     * @param algorithm the algorithm used to generated the key, such as "DES" or "Blowfish"
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @return the key generated
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #getDefaultSymmetricKeySize(String)
     */
    public static SecretKey generateSymmetricKey(String algorithm, String providerName) throws SecurityException {
        return generateSymmetricKey(algorithm, providerName, getDefaultSymmetricKeySize(algorithm));
    }

    /**
     * Generates a secret key using the specified algorithm and default provider and default key size.
     *
     * @param algorithm the algorithm used to generated the key, such as "DES" or "Blowfish"
     * @return the key generated
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #getDefaultSymmetricKeySize(String)
     */
    public static SecretKey generateSymmetricKey(String algorithm) throws SecurityException {
        return generateSymmetricKey(algorithm, null, getDefaultSymmetricKeySize(algorithm));
    }

    /**
     * Generates a secret key using a default algorithm, default provider and the specified key size.
     *
     * @param keySize the size of the key to generated, such as 512 or 1024
     * @return the key generated
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     */
    public static SecretKey generateSymmetricKey(int keySize) throws SecurityException {
        return generateSymmetricKey(DEFAULT_SYMMETRIC_ALGORITHM, null, keySize);
    }

    /**
     * Generates a secret key using a default algorithm, default provider and a default key size.
     *
     * @return the key generated
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #getDefaultASymmetricKeySize(String)
     */
    public static SecretKey generateSymmetricKey() throws SecurityException {
        return generateSymmetricKey(DEFAULT_SYMMETRIC_ALGORITHM,
                                    null,
                                    getDefaultSymmetricKeySize(DEFAULT_SYMMETRIC_ALGORITHM));
    }

    /**
     * Reconstructs a secret key from its encoded, persisted representations which was originally generated using the
     * specified algorithm.
     *
     * @param algorithm the algorithm that was used to originally generate the key pair
     * @param encodedKeySpec the encoded secret key specification (as that returned by a call to
     *        <code>Key.getEncoded</code>)
     * @return the reconstructed secret key
     * @throws SecurityException if an error occurs generating the key
     */
    public static SecretKey generateSymmetricKey(String algorithm, byte encodedKeySpec[]) throws SecurityException {
        try {
            SecretKey secretKey = new SecretKeySpec(encodedKeySpec, algorithm);
            return secretKey;
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured reconstructing the symmetric key from its encoded specification: ",
                                        th);
        }
    }

    /**
     * Generates the encoded, persistable, representation of the public and private keys from the specified key pair.
     *
     * @param keyPair the key pair whose keys are to be encoded.
     * @return an array of encodings, of size 2, with the first array element representing the encoded public key and the
     *         second the encoded private key.
     */
    public static byte[][] generateEncodedPublicPrivateKeySpecs(KeyPair keyPair) {
        try {
            byte publicKeyEncoding[] = keyPair.getPublic().getEncoded();
            byte privateKeyEncoding[] = keyPair.getPrivate().getEncoded();
            return new byte[][] { publicKeyEncoding, privateKeyEncoding };
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured generating the encoded public and private key specifications: ",
                                        th);
        }
    }

    /**
     * Generates the encoded, persistable, representation of the public and private keys from the specified key pair, in
     * Base64 encoded format with standard PEM headers and footer delimiters.
     *
     * <p>
     * For example, with header/footer delimiters, a private key might be returned as:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param keyPair the key pair whose keys are to be encoded.
     * @return an array of encodings, of size 2, with the first array element representing the encoded public key and the
     *         second the encoded private key in PEM format.
     * @throws SecurityException if an error occurs during encoding
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #decodePEMStringsToKeyPair(String, String)
     */
    public static String[] encodeKeyPairToPEMStrings(KeyPair keyPair, boolean includeHeadersAndFooters) throws SecurityException {
        return new String[] { encodePublicOrPrivateKeyToPEMString(keyPair.getPublic(), includeHeadersAndFooters),
                encodePublicOrPrivateKeyToPEMString(keyPair.getPrivate(), includeHeadersAndFooters) };
    }

    /**
     * Generates the encoded, persistable, representation of the public and private keys from the specified key pair, in
     * Base64 encoded format with or without standard PEM header/footer delimiters.
     *
     * <p>
     * For example, with header/footer delimiters, a private key might be returned as:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param keyPair the key pair whose keys are to be encoded.
     * @return an array of encodings, of size 2, with the first array element representing the encoded public key and the
     *         second the encoded private key in PEM format.
     * @throws SecurityException if an error occurs during encoding
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #decodePEMStringsToKeyPair(String, String)
     */
    public static String[] encodeKeyPairToPEMStrings(KeyPair keyPair) throws SecurityException {
        return encodeKeyPairToPEMStrings(keyPair, true);
    }

    /**
     * Generates the encoded, persistable, representation of a public / private key, in PEM format.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a private key might be returned as:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param key the key to be encoded; must be either a public or private key.
     * @param includeHeadersAndFooter true if a PEM header and footer is to be used to delimited the encoding, false if
     *        not.
     * @return the encoded key in PEM format.
     * @throws SecurityException if an error occurs during encoding
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static String encodePublicOrPrivateKeyToPEMString(Key key, boolean includeHeadersAndFooter) throws SecurityException {
        Assert.isTrue((key instanceof PublicKey) || (key instanceof PrivateKey),
                      "key specified must either be a public or private key");

        try {
            StringBuffer sBuf = new StringBuffer();
            byte keyEncoding[] = key.getEncoded();
            if (includeHeadersAndFooter) {
                sBuf.append("-----BEGIN ").append(key instanceof PublicKey ? "PUBLIC" : "PRIVATE").append(" KEY-----\n");
            }
            sBuf.append(Base64.encode(keyEncoding)).append("\n");
            if (includeHeadersAndFooter) {
                sBuf.append("-----END ").append(key instanceof PublicKey ? "PUBLIC" : "PRIVATE").append(" KEY-----\n");
            }
            String encodedKeyStr = sBuf.toString();
            return encodedKeyStr;
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured generating the encoded key specification: ", th);
        }
    }

    /**
     * Decodes a PEM encoded public key specification and returns the <code>{@link PublicKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param keyPEMSpec the encoded key PEM specification, which may be delimited by a PEM header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PublicKey decodePublicKeyPEMString(String keyPEMSpec) throws SecurityException {
        return decodePublicKeyPEMString(DEFAULT_ASYMMETRIC_ALGORITHM, null, keyPEMSpec);
    }

    /**
     * Decodes a PEM encoded public key specification using the specified algorithm (which was originally used to
     * generate the key) and returns the <code>{@link PublicKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param keyPEMSpec the encoded key PEM specification, which may be delimited by a PEM header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PublicKey decodePublicKeyPEMString(String algorithm, String keyPEMSpec) throws SecurityException {
        return decodePublicKeyPEMString(algorithm, null, keyPEMSpec);
    }

    /**
     * Decodes a PEM encoded public key specification using the specified algorithm (which was originally used to
     * generate the key) and provider and returns the <code>{@link PublicKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyPEMSpec the encoded key PEM specification, which may be delimited by a PEM header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PublicKey decodePublicKeyPEMString(String algorithm, String providerName, String keyPEMSpec) throws SecurityException {
        String lines[] = StringUtil.toLinesArray(keyPEMSpec);
        if (lines.length > 2) {
            if (lines[0].indexOf("--BEGIN ") > 0 && lines[0].indexOf(" KEY--") > 0
                    && lines[lines.length - 1].indexOf("--END ") > 0 && lines[lines.length - 1].indexOf(" KEY--") > 0) {
                keyPEMSpec = keyPEMSpec.substring(keyPEMSpec.indexOf(lines[1]), keyPEMSpec.length()
                        - lines[lines.length - 1].length() - 1);
            }
        }
        return generatePublicKey(algorithm, providerName, new X509EncodedKeySpec(Base64.decode(keyPEMSpec)));
    }

    /**
     * Decodes a PEM encoded public key specification using default algorithm employed by this utility (which was
     * originally used to generate the key)and returns the <code>{@link PublicKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param keyResource the encoded key PEM specification resource, within which the key may be delimited by a PEM
     *        header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PublicKey decodePublicKeyPEMString(Resource keyResource) throws SecurityException {
        return decodePublicKeyPEMString(null, null, keyResource);
    }

    /**
     * Decodes a PEM encoded public key specification using the specified algorithm (which was originally used to
     * generate the key) and provider and returns the <code>{@link PublicKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param keyResource the encoded key PEM specification resource, within which the key may be delimited by a PEM
     *        header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PublicKey decodePublicKeyPEMString(String algorithm, Resource keyResource) throws SecurityException {
        return decodePublicKeyPEMString(algorithm, null, keyResource);
    }

    /**
     * Decodes a PEM encoded public key specification using the specified algorithm (which was originally used to
     * generate the key) and provider and returns the <code>{@link PublicKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyResource the encoded key PEM specification resource, within which the key may be delimited by a PEM
     *        header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PublicKey decodePublicKeyPEMString(String algorithm, String providerName, Resource keyResource) throws SecurityException {
        // No charset encoding should be necessary: encoding should be US-ASCII
        StringWriter sw = new StringWriter();

        IoUtil.transferAndClose(keyResource.getReader(), sw);

        return decodePublicKeyPEMString(algorithm, providerName, sw.toString());
    }

    /**
     * Decodes a PEM encoded public key specification and returns the <code>{@link PrivateKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param keyPEMSpec the encoded key PEM specification, which may be delimited by a PEM header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PrivateKey decodePrivateKeyPEMString(String keyPEMSpec) throws SecurityException {
        return decodePrivateKeyPEMString(DEFAULT_ASYMMETRIC_ALGORITHM, null, keyPEMSpec);
    }

    /**
     * Decodes a PEM encoded private key specification using the specified algorithm (which was originally used to
     * generate the key) and returns the <code>{@link PrivateKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param keyPEMSpec the encoded key PEM specification, which may be delimited by a PEM header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PrivateKey decodePrivateKeyPEMString(String algorithm, String keyPEMSpec) throws SecurityException {
        return decodePrivateKeyPEMString(algorithm, null, keyPEMSpec);
    }

    /**
     * Decodes a PEM encoded private key specification using the specified algorithm (which was originally used to
     * generate the key) and provider and returns the <code>{@link PrivateKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyPEMSpec the encoded key PEM specification, which may be delimited by a PEM header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PrivateKey decodePrivateKeyPEMString(String algorithm, String providerName, String keyPEMSpec) throws SecurityException {
        String lines[] = StringUtil.toLinesArray(keyPEMSpec);
        if (lines.length > 2) {
            if (lines[0].indexOf("--BEGIN ") > 0 && lines[0].indexOf(" KEY--") > 0
                    && lines[lines.length - 1].indexOf("--END ") > 0 && lines[lines.length - 1].indexOf(" KEY--") > 0) {
                keyPEMSpec = keyPEMSpec.substring(keyPEMSpec.indexOf(lines[1]), keyPEMSpec.length()
                        - lines[lines.length - 1].length() - 1);
            }
        }
        return generatePrivateKey(algorithm, providerName, new PKCS8EncodedKeySpec(Base64.decode(keyPEMSpec)));
    }

    /**
     * Decodes a PEM encoded private key specification using default algorithm employed by this utility (which was
     * originally used to generate the key)and returns the <code>{@link PrivateKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param keyResource the encoded key PEM specification resource, within which the key may be delimited by a PEM
     *        header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PrivateKey decodePrivateKeyPEMString(Resource keyResource) throws SecurityException {
        return decodePrivateKeyPEMString(null, null, keyResource);
    }

    /**
     * Decodes a PEM encoded private key specification using the specified algorithm (which was originally used to
     * generate the key) and returns the <code>{@link PrivateKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param keyResource the encoded key PEM specification resource, within which the key may be delimited by a PEM
     *        header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PrivateKey decodePrivateKeyPEMString(String algorithm, Resource keyResource) throws SecurityException {
        return decodePrivateKeyPEMString(algorithm, null, keyResource);
    }

    /**
     * Decodes a PEM encoded private key specification using the specified algorithm (which was originally used to
     * generate the key) and provider and returns the <code>{@link PrivateKey}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyResource the encoded key PEM specification resource, within which the key may be delimited by a PEM
     *        header/footer.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static PrivateKey decodePrivateKeyPEMString(String algorithm,
                                                       String providerName,
                                                       Resource keyResource) throws SecurityException {
        // No charset encoding should be necessary: encoding should be US-ASCII
        StringWriter sw = new StringWriter();

        IoUtil.transferAndClose(keyResource.getReader(), sw);

        return decodePrivateKeyPEMString(algorithm, providerName, sw.toString());
    }

    /**
     * Decodes PEM encoded public and private key specifications and produces the <code>{@link KeyPair}</code> of the
     * two.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param publicKeyPEMSpec the encoded public key PEM specification, which may be delimited by a PEM header/footer.
     * @param privateKeyPEMSpec the encoded private key PEM specification, which may be delimited by a PEM header/footer.
     * @return a key pair, consisting of the public and private keys decoded from their PEM encoded formats
     * @throws SecurityException if an error occurs decoding the key specifications
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static KeyPair decodePEMStringsToKeyPair(String publicKeyPEMSpec, String privateKeyPEMSpec) throws SecurityException {
        return decodePEMStringsToKeyPair(DEFAULT_ASYMMETRIC_ALGORITHM, null, publicKeyPEMSpec, privateKeyPEMSpec);
    }

    /**
     * Decodes PEM encoded public and private key specifications and produces the <code>{@link KeyPair}</code> of the
     * two.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param publicKeyPEMSpec the encoded public key PEM specification, which may be delimited by a PEM header/footer.
     * @param privateKeyPEMSpec the encoded private key PEM specification, which may be delimited by a PEM header/footer.
     * @return a key pair, consisting of the public and private keys decoded from their PEM encoded formats
     * @throws SecurityException if an error occurs decoding the key specifications
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static KeyPair decodePEMStringsToKeyPair(String algorithm, String publicKeyPEMSpec, String privateKeyPEMSpec) throws SecurityException {
        return decodePEMStringsToKeyPair(algorithm, null, publicKeyPEMSpec, privateKeyPEMSpec);
    }

    /**
     * Decodes PEM encoded public and private key specifications and produces the <code>{@link KeyPair}</code> of the
     * two.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded private key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PRIVATE KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PRIVATE KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param publicKeyPEMSpec the encoded public key PEM specification, which may be delimited by a PEM header/footer.
     * @param privateKeyPEMSpec the encoded private key PEM specification, which may be delimited by a PEM header/footer.
     * @return a key pair, consisting of the public and private keys decoded from their PEM encoded formats
     * @throws SecurityException if an error occurs decoding the key specifications
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static KeyPair decodePEMStringsToKeyPair(String algorithm,
                                                    String providerName,
                                                    String publicKeyPEMSpec,
                                                    String privateKeyPEMSpec) throws SecurityException {
        return new KeyPair(decodePublicKeyPEMString(algorithm, providerName, publicKeyPEMSpec),
                           decodePrivateKeyPEMString(algorithm, providerName, privateKeyPEMSpec));
    }

    /**
     * Decodes a PEM encoded public or private key specification using the specified algorithm (which was originally used
     * to generate the key) and returns the <code>{@link Key}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The resource must contain a valid PEM encoded key with or
     *        without PEM key headers.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static Key decodePublicOrPrvateKeyPEMString(String algorithm,
                                                       String providerName,
                                                       Resource keyResource) throws SecurityException {
        Key key = null;
        try {
            key = decodePublicKeyPEMString(algorithm, providerName, keyResource);
        } catch (SecurityException ignoreIfNotPublicKeyEx) {
        }

        if (key == null) {
            try {
                key = decodePrivateKeyPEMString(algorithm, providerName, keyResource);
            } catch (SecurityException ignoreIfNotPublicKeyEx) {
            }
        }

        if (key == null) {
            throw new SecurityException("The specified PEM encoded key file [" + keyResource
                                                + "] does not appear to contain a valid public or private PEM encoded key.");
        }

        return key;
    }

    /**
     * Decodes a PEM encoded public or private key specification using the specified algorithm (which was originally used
     * to generate the key) and returns the <code>{@link Key}</code>.
     *
     * <p>
     * The PEM format represents DER-encoded data in a printable format. Traditionally, PEM encoding simply
     * base64-encodes DER-encoded data and adds a simple header and footer.
     * </p>
     *
     * <p>
     * For example, with header/footer delimiters, a PEM encoded public key might be:
     * </p>
     *
     * <pre>
     * -----BEGIN PUBLIC KEY-----
     * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKAMsHzpziR5n+UhalvyX1w1cC2w
     * oCtqqQAX1lamrofMwy0qTz+W2g4g6DMiAD0PwmqaTvJ7UsxW9jmTAebSe6oKOYOQxUTXI+WG3c9B
     * hx2hQj36ClhFRF5Zc0oY8y75RzEH63qUKsekQjXbjRnzGcQ9K2i+USemJPWjW63tuxixAgMBAAEC
     * gYAZaQpYOrKs3daCBWUihf+X3zAZQPKdEgkU57Py+/G3w821DQOZ//RMy/Kxs5NAHpFqZWdlXikO
     * IjxjdbCWmhJh81Gvnwz1BArGaYwrQO0uTH0qbEN9SyBcuBAD8nNfVpgqKdK7Ye4lyewdRB8C+T5J
     * Uc89MCR4kQt09j0mzUw1MQJBANvDJcR64tOQDNPu0ehF6fRZhwSYD+LDv0Qih4owqL9R00hXuPDm
     * PRyn25TMN91WTeFTirmJspWHHgy63f45jdUCQQC6cOGbEbggXmvFZYKGfW0ChJrgJFMx99RW6kLn
     * Jw9E3rylAgZD8MXfK1yCb1HtL5TEMqE9DkxPCQPbkIYQjGFtAkAjxcRkE0zQ+2XbKcjpclf++oPL
     * 76TGWO7NfIFrsTgGzJ8D66OjMxdHjttjgUqmsOHEiADQ6uUzCeeOUuzH8T5xAkEAjudg9ZQiVqUo
     * 4/fHkUBoIsrzTyRopF86YZhTyYuV14sGe0/O75qIgGNjGBMtb6jN1YidMAhakyXs0Am5yMthDQJA
     * MVW2SBPHqRfHe7wTJxOxqNM/2dMwQD0eKPUHaviios+n33Dnfqk7uPFFYVKmxeBq5E3Gsq7qv9sl
     * O67Ms20ZBg==
     * -----END PUBLIC KEY-----
     * </pre>
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyPEMSpec a string containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The resource must contain a valid PEM encoded key with or
     *        without PEM key headers.
     * @return the reconsituted key
     * @throws SecurityException if an error occurs decoding the key specification
     * @see #encodePublicOrPrivateKeyToPEMString(Key, boolean)
     * @see #encodeKeyPairToPEMStrings(KeyPair, boolean)
     */
    public static Key decodePublicOrPrvateKeyPEMString(String algorithm, String providerName, String keyPEMSpec) throws SecurityException {
        Key key = null;
        try {
            key = decodePublicKeyPEMString(algorithm, providerName, keyPEMSpec);
        } catch (SecurityException ignoreIfNotPublicKeyEx) {
        }

        if (key == null) {
            try {
                key = decodePrivateKeyPEMString(algorithm, providerName, keyPEMSpec);
            } catch (SecurityException ignoreIfNotPublicKeyEx) {
            }
        }

        if (key == null) {
            throw new SecurityException("The specified PEM encoded key [" + key
                                                + "] does not appear to contain a valid public or private PEM encoded key.");
        }

        return key;
    }

    /**
     * Reconstructs a public/private key pair from their encoded, persisted representations which were originally
     * generated using the specified algorithm.
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param publicKeySpec the public key specification (as that returned by a call to <code>PublicKey.getEncoded</code>
     *        )
     * @param privateKeySpec the private key specification (as that returned by a call to
     *        <code>PrivateKey.getEncoded</code>)
     * @return a key pair that comprises the reconstructed public and private keys.
     * @throws SecurityException if an error occurs generating the key pair
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static KeyPair generateKeyPair(String algorithm,
                                          String providerName,
                                          EncodedKeySpec publicKeySpec,
                                          EncodedKeySpec privateKeySpec) throws SecurityException {
        try {
            if (algorithm == null) {
                algorithm = DEFAULT_ASYMMETRIC_ALGORITHM;
            }
            KeyFactory keyFactory = providerName != null ? KeyFactory.getInstance(getKeyTypeFromAlgorithm(algorithm),
                                                                                  providerName)
                    : KeyFactory.getInstance(getKeyTypeFromAlgorithm(algorithm));
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            return new KeyPair(publicKey, privateKey);
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured reconstructing the public/private keys from their encoded specification [algorithm="
                                                + algorithm + ", " + "providerName=" + providerName + "]: ",
                                        th);
        }
    }

    /**
     * Reconstructs a public key from its encoded key specification. The key was originally generated using the specified
     * key generation algorithm, <code>{@link #DEFAULT_ASYMMETRIC_ALGORITHM}</code>.
     *
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param publicKeySpec the public key specification (as that returned by a call to <code>PublicKey.getEncoded</code>
     *        )
     * @return the public key, reconstructed from its encoded key specification.
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static PublicKey generatePublicKey(String providerName, EncodedKeySpec publicKeySpec) throws SecurityException {
        return generatePublicKey(DEFAULT_ASYMMETRIC_ALGORITHM, providerName, publicKeySpec);
    }

    /**
     * Reconstructs a public key from its encoded key specification. The key was originally generated using the specified
     * key generation algorithm, <code>DEFAULT_ASYMMETRIC_ALGORITHM</code>.
     *
     * <p>
     * The default provider will be used to reconstruct the key.
     * </p>
     *
     * @param publicKeySpec the public key specification (as that returned by a call to <code>PublicKey.getEncoded</code>
     *        )
     * @return the public key, reconstructed from its encoded key specification.
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static PublicKey generatePublicKey(EncodedKeySpec publicKeySpec) throws SecurityException {
        return generatePublicKey(DEFAULT_ASYMMETRIC_ALGORITHM, null, publicKeySpec);
    }

    /**
     * Reconstructs a public key from its encoded key specification. The key was originally generated using the specified
     * key generation algorithm and the specified preferred provider will be used in the reconstruction.
     *
     * @param algorithm the algorithm that was used to originally generate the key
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param publicKeySpec the public key specification (as that returned by a call to <code>PublicKey.getEncoded</code>
     *        )
     * @return the public key, reconstructed from its encoded key specification.
     * @throws SecurityException if an error occurs generating the key
     */
    public static PublicKey generatePublicKey(String algorithm, String providerName, EncodedKeySpec publicKeySpec) throws SecurityException {
        try {
            if (algorithm == null) {
                algorithm = DEFAULT_ASYMMETRIC_ALGORITHM;
            }
            KeyFactory keyFactory = providerName != null ? KeyFactory.getInstance(getKeyTypeFromAlgorithm(algorithm),
                                                                                  providerName)
                    : KeyFactory.getInstance(getKeyTypeFromAlgorithm(algorithm));
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured reconstructing the public key from its encoded specification "
                                                + "[algorithm="
                                                + algorithm
                                                + ", providerName="
                                                + providerName
                                                + ", encodedKeyBase64="
                                                + Base64.encode(publicKeySpec.getEncoded())
                                                + "]: ",
                                        th);
        }
    }

    /**
     * Reconstructs a private key from its encoded key specification. The key was originally generated using the
     * specified key generation algorithm, <code>DEFAULT_ASYMMETRIC_ALGORITHM</code>.
     *
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param privateKeySpec the private key specification (as that returned by a call to
     *        <code>PrivateKey.getEncoded</code>)
     * @return the public key, reconstructed from its encoded key specification.
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static PrivateKey generatePrivateKey(String providerName, EncodedKeySpec privateKeySpec) throws SecurityException {
        return generatePrivateKey(DEFAULT_ASYMMETRIC_ALGORITHM, providerName, privateKeySpec);
    }

    /**
     * Reconstructs a private key from its encoded key specification. The key was originally generated using the
     * specified key generation algorithm, <code>DEFAULT_ASYMMETRIC_ALGORITHM</code>.
     *
     * <p>
     * The default provider will be used to reconstruct the key.
     * </p>
     *
     * @param privateKeySpec the private key specification (as that returned by a call to
     *        <code>PrivateKey.getEncoded</code>)
     * @return the private key, reconstructed from its encoded key specification.
     * @throws SecurityException if an error occurs generating the key
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     */
    public static PrivateKey generatePrivateKey(EncodedKeySpec privateKeySpec) throws SecurityException {
        return generatePrivateKey(DEFAULT_ASYMMETRIC_ALGORITHM, null, privateKeySpec);
    }

    /**
     * Reconstructs a private key from its encoded key specification. The key was originally generated using the
     * specified key generation algorithm and the specified preferred provider will be used in the reconstruction.
     *
     * @param algorithm the algorithm that was used to originally generate the key, or null if the default algorithm,
     *        employed by this utility, is to be used.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param privateKeySpec the private key specification (as that returned by a call to
     *        <code>PrivateKey.getEncoded</code>)
     * @return the private key, reconstructed from its encoded key specification.
     * @throws SecurityException if an error occurs generating the key
     */
    public static PrivateKey generatePrivateKey(String algorithm, String providerName, EncodedKeySpec privateKeySpec) throws SecurityException {
        try {
            if (algorithm == null) {
                algorithm = DEFAULT_ASYMMETRIC_ALGORITHM;
            }
            KeyFactory keyFactory = providerName != null ? KeyFactory.getInstance(getKeyTypeFromAlgorithm(algorithm),
                                                                                  providerName)
                    : KeyFactory.getInstance(getKeyTypeFromAlgorithm(algorithm));
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            return privateKey;
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured reconstructing the private key from its encoded specification "
                                                + "[algorithm="
                                                + algorithm
                                                + ", providerName="
                                                + providerName
                                                + ", encodedKeyBase64="
                                                + Base64.encode(privateKeySpec.getEncoded())
                                                + "]: ",
                                        th);
        }
    }

    /**
     * Reconstructs a public/private key pair from their encoded, persisted representations which were originally
     * generated using the default algorithm employed by this utility ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}) and the
     * default provider.
     *
     * @param publicKeySpec the public key specification (as that returned by a call to <code>PublicKey.getEncoded</code>
     *        )
     * @param privateKeySpec the private key specification (as that returned by a call to
     *        <code>PrivateKey.getEncoded</code>)
     * @return a key pair that comprises the reconstructed public and private keys.
     * @throws SecurityException if an error occurs generating the key pair
     */
    public static KeyPair generateKeyPair(EncodedKeySpec publicKeySpec, EncodedKeySpec privateKeySpec) throws SecurityException {
        return generateKeyPair(DEFAULT_ASYMMETRIC_ALGORITHM, null, publicKeySpec, privateKeySpec);
    }

    /**
     * Reconstructs a public/private key pair from their encoded, persisted representations which were originally
     * generated using the specified algorithm.
     *
     * @param algorithm the algorithm that was used to originally generate the key pair. If null, the default algorithm
     *        employed by this utility will be assumed ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}).
     * @param publicKeySpec the public key specification (as that returned by a call to <code>PublicKey.getEncoded</code>
     *        )
     * @param privateKeySpec the private key specification (as that returned by a call to
     *        <code>PrivateKey.getEncoded</code>)
     * @return a key pair that comprises the reconstructed public and private keys.
     * @throws SecurityException if an error occurs generating the key pair
     */
    public static KeyPair generateKeyPair(String algorithm, EncodedKeySpec publicKeySpec, EncodedKeySpec privateKeySpec) throws SecurityException {
        return generateKeyPair(algorithm, null, publicKeySpec, privateKeySpec);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the encryption algorithm to use
     * @param key the key to use
     * @param data the binary content to be encrypted
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, Key, byte[])
     */
    public static byte[] encrypt(String algorithm, Key key, byte[] data) throws SecurityException {
        return encrypt(algorithm, null, data, key);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the decryption algorithm to use
     * @param key the key to use
     * @param data the binary content to be decrypted
     * @return the decrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, Key, byte[])
     */
    public static byte[] decrypt(String algorithm, Key key, byte[] data) throws SecurityException {
        return decrypt(algorithm, null, data, key);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param data the binary content to be encrypted
     * @param key the key to use
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, String, byte[], Key)
     */
    public static byte[] encrypt(String algorithm, String providerName, byte[] data, Key key) throws SecurityException {
        try {
            // Get the cipher object for the specified algorithm
            Cipher cipher = providerName != null ? Cipher.getInstance(algorithm, providerName)
                    : Cipher.getInstance(algorithm);

            // Now encrypt the data using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ByteArrayOutputStream outputBAOS = new ByteArrayOutputStream(data.length * 2);
            crypt(cipher, key, new ByteArrayInputStream(data), outputBAOS, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
            return outputBAOS.toByteArray();
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured encrypting the data: ", th);
        }
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, String, byte[], Key)
     */
    public static byte[] decrypt(String algorithm, String providerName, byte[] data, Key key) throws SecurityException {
        try {
            // Get the cipher object for the specified algorithm
            Cipher cipher = providerName != null ? Cipher.getInstance(algorithm, providerName)
                    : Cipher.getInstance(algorithm);

            // Now encrypt the data using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);
            ByteArrayOutputStream outputBAOS = new ByteArrayOutputStream(data.length * 2);
            crypt(cipher, key, new ByteArrayInputStream(data), outputBAOS, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
            return outputBAOS.toByteArray();
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured decrypting the data: ", th);
        }
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param key the key to use
     * @param file the file containing the binary content to be encrypted
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #decrypt(String, Key, File)
     */
    public static byte[] encrypt(String algorithm, Key key, File file) throws SecurityException {
        return encrypt(algorithm, null, key, file, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param key the key to use
     * @param file the file containing the binary content to be decrypted
     * @return the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, Key, File)
     */
    public static byte[] decrypt(String algorithm, Key key, File file) throws SecurityException {
        return decrypt(algorithm, null, key, file, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param key the key to use
     * @param file the file containing the binary content to be encrypted
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, Key, File, int)
     */
    public static byte[] encrypt(String algorithm, Key key, File file, int bufferSize) throws SecurityException {
        return encrypt(algorithm, null, key, file, bufferSize);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param key the key to use
     * @param file the file containing the binary content to be decrypted
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, Key, File, int)
     */
    public static byte[] decrypt(String algorithm, Key key, File file, int bufferSize) throws SecurityException {
        return decrypt(algorithm, null, key, file, bufferSize);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param file the file containing the binary content to be encrypted
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, String, Key, File)
     */
    public static byte[] encrypt(String algorithm, String providerName, Key key, File file) throws SecurityException {
        return encrypt(algorithm, providerName, key, file, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param file the file containing the binary content to be decrypted
     * @return the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, String, Key, File)
     */
    public static byte[] decrypt(String algorithm, String providerName, Key key, File file) throws SecurityException {
        return decrypt(algorithm, providerName, key, file, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param file the file containing the binary content to be encrypted
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, String, Key, File, int)
     */
    public static byte[] encrypt(String algorithm, String providerName, Key key, File file, int bufferSize) throws SecurityException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) file.length());

            encrypt(algorithm, providerName, key, fis, baos, bufferSize);

            return baos.toByteArray();
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured encrypting the data: ", th);
        } finally {
            IoUtil.closeIgnoringErrors(fis);
        }
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param file the file containing the binary content to be decrypted
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, String, Key, File, int)
     */
    public static byte[] decrypt(String algorithm, String providerName, Key key, File file, int bufferSize) throws SecurityException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) file.length());

            decrypt(algorithm, providerName, key, fis, baos, bufferSize);

            return baos.toByteArray();
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured decrypting the data: ", th);
        } finally {
            IoUtil.closeIgnoringErrors(fis);
        }
    }

    /**
     * Encrypts binary content using a default algorithm, applicable for the type of key specified: asymmetric or
     * symmetric.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param key the key to use
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(Key, File, File)
     */
    public static void encrypt(Key key, File inputFile, File outputFile) throws SecurityException {
        encrypt(null, null, key, inputFile, outputFile, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a default algorithm, applicable for the type of key specified: asymmetric or
     * symmetric.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param key the key to use
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the decrypted content
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(Key, File, File)
     */
    public static void decrypt(Key key, File inputFile, File outputFile) throws SecurityException {
        decrypt(null, null, key, inputFile, outputFile, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a default algorithm, applicable for the type of key specified: asymmetric or
     * symmetric.
     *
     * @param key the key to use
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(Key, File, File, int)
     */
    public static void encrypt(Key key, File inputFile, File outputFile, int bufferSize) throws SecurityException {
        encrypt(null, null, key, inputFile, outputFile, bufferSize);
    }

    /**
     * Decrypts binary content using a default algorithm, applicable for the type of key specified: asymmetric or
     * symmetric.
     *
     * @param key the key to use
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the decrypted content
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(Key, File, File, int)
     */
    public static void decrypt(Key key, File inputFile, File outputFile, int bufferSize) throws SecurityException {
        decrypt(null, null, key, inputFile, outputFile, bufferSize);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param key the key to use
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, Key, File, File)
     */
    public static void encrypt(String algorithm, Key key, File inputFile, File outputFile) throws SecurityException {
        encrypt(algorithm, null, key, inputFile, outputFile, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a default algorithm, applicable for the type of key specified: asymmetric or
     * symmetric.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param key the key to use
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the decrypted content
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, Key, File, File)
     */
    public static void decrypt(String algorithm, Key key, File inputFile, File outputFile) throws SecurityException {
        decrypt(algorithm, null, key, inputFile, outputFile, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param key the key to use
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, Key, File, File, int)
     */
    public static void encrypt(String algorithm, Key key, File inputFile, File outputFile, int bufferSize) throws SecurityException {
        encrypt(algorithm, null, key, inputFile, outputFile, bufferSize);
    }

    /**
     * Decrypts binary content using a default algorithm, applicable for the type of key specified: asymmetric or
     * symmetric.
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param key the key to use
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the decrypted content
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, Key, File, File, int)
     */
    public static void decrypt(String algorithm, Key key, File inputFile, File outputFile, int bufferSize) throws SecurityException {
        decrypt(algorithm, null, key, inputFile, outputFile, bufferSize);
    }

    /**
     * Encrypts binary content using the default algorithm employed by this utility and using the specified file
     * containing a PEM encoded key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(File, File, File)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void encryptUsingPEMEncodedKeyFile(File keyFile, File inputFile, File outputFile) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile));
    }

    /**
     * Decrypts binary content using the default algorithm employed by this utility and using the specified file
     * containing a PEM encoded key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the decoded data
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(File, File, File)
     * @see #decodePublicKeyPEMString(String) for the method used to decode a PEM encoded public key
     * @see #decodePrivateKeyPEMString(String) for the method used to decode a PEM encoded private key
     */
    public static void decryptUsingPEMEncodedKeyFile(File keyFile, File inputFile, File outputFile) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile));
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(String, File, File, File)
     * @see #decodePublicKeyPEMString(String) for the method used to decode a PEM encoded public key
     * @see #decodePrivateKeyPEMString(String) for the method used to decode a PEM encoded private key
     */
    public static void encryptUsingPEMEncodedKeyFile(String algorithm, File keyFile, File inputFile, File outputFile) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(algorithm,
                                      new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile));
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric decryption algorithm to use.
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the decoded data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(String, File, File, File)
     * @see #decodePublicKeyPEMString(String) for the method used to decode a PEM encoded public key
     * @see #decodePrivateKeyPEMString(String) for the method used to decode a PEM encoded private key
     */
    public static void decryptUsingPEMEncodedKeyFile(String algorithm, File keyFile, File inputFile, File outputFile) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(algorithm,
                                      new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile));
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(String, File, File, File, int)
     * @see #decodePublicKeyPEMString(String) for the method used to decode a PEM encoded public key
     * @see #decodePrivateKeyPEMString(String) for the method used to decode a PEM encoded private key
     */
    public static void encryptUsingPEMEncodedKeyFile(String algorithm,
                                                     File keyFile,
                                                     File inputFile,
                                                     File outputFile,
                                                     int bufferSize) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(algorithm,
                                      new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile),
                                      bufferSize);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(String, File, File, File, int)
     * @see #decodePublicKeyPEMString(String) for the method used to decode a PEM encoded public key
     * @see #decodePrivateKeyPEMString(String) for the method used to decode a PEM encoded private key
     */
    public static void decryptUsingPEMEncodedKeyFile(String algorithm,
                                                     File keyFile,
                                                     File inputFile,
                                                     File outputFile,
                                                     int bufferSize) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(algorithm,
                                      new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile),
                                      bufferSize);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(String, String, File, File, File, int)
     * @see #decodePublicKeyPEMString(String) for the method used to decode a PEM encoded public key
     * @see #decodePrivateKeyPEMString(String) for the method used to decode a PEM encoded private key
     */
    public static void encryptUsingPEMEncodedKeyFile(String algorithm,
                                                     String providerName,
                                                     File keyFile,
                                                     File inputFile,
                                                     File outputFile,
                                                     int bufferSize) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(algorithm,
                                      providerName,
                                      new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile),
                                      bufferSize);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyFile a file containing a PEM encoded public or private key; an attempt will be made to decode a public
     *        then a private key from the file. The file must contain a valid PEM encoded key with or without PEM key
     *        headers.
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(String, String, File, File, File, int)
     * @see #decodePublicKeyPEMString(String) for the method used to decode a PEM encoded public key
     * @see #decodePrivateKeyPEMString(String) for the method used to decode a PEM encoded private key
     */
    public static void decryptUsingPEMEncodedKeyFile(String algorithm,
                                                     String providerName,
                                                     File keyFile,
                                                     File inputFile,
                                                     File outputFile,
                                                     int bufferSize) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(algorithm,
                                      providerName,
                                      new FileResource(keyFile),
                                      new FileResource(inputFile),
                                      new FileResource(outputFile),
                                      bufferSize);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * <p>
     * The default algorithm employed by this utility, ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}), is used and the key is
     * assumed to be compatible with this algorithm.
     * </p>
     *
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be encrypted
     * @param toResource the detination resource to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(Resource, Resource, Resource)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void encryptUsingPEMEncodedKeyFile(Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(DEFAULT_ASYMMETRIC_ALGORITHM,
                                      null,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * <p>
     * The default algorithm employed by this utility, ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}), is used and the key is
     * assumed to be compatible with this algorithm.
     * </p>
     *
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be decrypted
     * @param toResource the detination resource to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #decryptUsingPEMEncodedKeyFile(Resource, Resource, Resource)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void decryptUsingPEMEncodedKeyFile(Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(DEFAULT_ASYMMETRIC_ALGORITHM,
                                      null,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * The default algorithm employed by this utility, ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}), is used and the key is
     * assumed to be compatible with this algorithm.
     * </p>
     *
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be encrypted
     * @param toResource the detination resource to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(Resource, Resource, Resource, int)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void encryptUsingPEMEncodedKeyFile(Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource,
                                                     int bufferSize) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(DEFAULT_ASYMMETRIC_ALGORITHM,
                                      null,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      bufferSize);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * <p>
     * The default algorithm employed by this utility, ({@link #DEFAULT_ASYMMETRIC_ALGORITHM}), is used and the key is
     * assumed to be compatible with this algorithm.
     * </p>
     *
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be decrypted
     * @param toResource the detination resource to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(Resource, Resource, Resource, int)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void decryptUsingPEMEncodedKeyFile(Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource,
                                                     int bufferSize) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(DEFAULT_ASYMMETRIC_ALGORITHM,
                                      null,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      bufferSize);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be encrypted
     * @param toResource the detination resource to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(String, Resource, Resource, Resource)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void encryptUsingPEMEncodedKeyFile(String algorithm,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(algorithm,
                                      null,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the asymmetric decryption algorithm to use.
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be decrypted
     * @param toResource the detination resource to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(String, Resource, Resource, Resource)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void decryptUsingPEMEncodedKeyFile(String algorithm,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(algorithm,
                                      null,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be encrypted
     * @param toResource the detination resource to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(String, Resource, Resource, Resource, int)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void encryptUsingPEMEncodedKeyFile(String algorithm,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource,
                                                     int bufferSize) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(algorithm, null, keyResource, fromResource, toResource, bufferSize);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric decryption algorithm to use.
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be decrypted
     * @param toResource the detination resource to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(String, Resource, Resource, Resource, int)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void decryptUsingPEMEncodedKeyFile(String algorithm,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource,
                                                     int bufferSize) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(algorithm, null, keyResource, fromResource, toResource, bufferSize);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be encrypted
     * @param toResource the detination resource to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(String, String, Resource, Resource, Resource)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void encryptUsingPEMEncodedKeyFile(String algorithm,
                                                     String providerName,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource) throws SecurityException {
        encryptUsingPEMEncodedKeyFile(algorithm,
                                      providerName,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the asymmetric decryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be decrypted
     * @param toResource the detination resource to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(String, String, Resource, Resource, Resource)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void decryptUsingPEMEncodedKeyFile(String algorithm,
                                                     String providerName,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource) throws SecurityException {
        decryptUsingPEMEncodedKeyFile(algorithm,
                                      providerName,
                                      keyResource,
                                      fromResource,
                                      toResource,
                                      IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be encrypted
     * @param toResource the detination resource to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decryptUsingPEMEncodedKeyFile(String, String, Resource, Resource, Resource, int)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void encryptUsingPEMEncodedKeyFile(String algorithm,
                                                     String providerName,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource,
                                                     int bufferSize) throws SecurityException {
        Key key = null;
        try {
            key = decodePublicKeyPEMString(algorithm, keyResource);
        } catch (SecurityException ignoreIfNotPublicKeyEx) {
        }

        if (key == null) {
            try {
                key = decodePrivateKeyPEMString(algorithm, keyResource);
            } catch (SecurityException ignoreIfNotPublicKeyEx) {
            }
        }

        if (key == null) {
            throw new SecurityException("The specified PEM encoded key file [" + keyResource
                                                + "] does not appear to contain a valid public or private PEM encoded key.");
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            encrypt(algorithm, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the asymmetric encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param keyResource a resource containing a PEM encoded public or private key; an attempt will be made to decode a
     *        public then a private key from the resource. The file must contain a valid PEM encoded key with or without
     *        PEM key headers.
     * @param fromResource the resource containing the binary content to be decrypted
     * @param toResource the detination resource to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encryptUsingPEMEncodedKeyFile(String, String, Resource, Resource, Resource, int)
     * @see #decodePublicKeyPEMString(String)
     * @see #decodePrivateKeyPEMString(String)
     */
    public static void decryptUsingPEMEncodedKeyFile(String algorithm,
                                                     String providerName,
                                                     Resource keyResource,
                                                     Resource fromResource,
                                                     Resource toResource,
                                                     int bufferSize) throws SecurityException {
        Key key = null;
        try {
            key = decodePublicKeyPEMString(algorithm, keyResource);
        } catch (SecurityException ignoreIfNotPublicKeyEx) {
        }

        if (key == null) {
            try {
                key = decodePrivateKeyPEMString(algorithm, keyResource);
            } catch (SecurityException ignoreIfNotPublicKeyEx) {
            }
        }

        if (key == null) {
            throw new SecurityException("The specified PEM encoded key file [" + keyResource
                                                + "] does not appear to contain a valid public or private PEM encoded key.");
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            decrypt(algorithm, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, String, Key, File, File)
     */
    public static void encrypt(String algorithm, String providerName, Key key, File inputFile, File outputFile) throws SecurityException {
        encrypt(algorithm, providerName, key, inputFile, outputFile, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * <p>
     * A default transfer buffer size of <code>{@link IoUtil#DEFAULT_TRANSFER_BUF_SIZE}</code> is used for the input
     * stream block transfer size.
     * </p>
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the output cipher text
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, String, Key, File, File)
     */
    public static void decrypt(String algorithm, String providerName, Key key, File inputFile, File outputFile) throws SecurityException {
        decrypt(algorithm, providerName, key, inputFile, outputFile, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the encryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputFile the file containing the binary content to be encrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String, String, Key, File, File, int)
     */
    public static void encrypt(String algorithm,
                               String providerName,
                               Key key,
                               File inputFile,
                               File outputFile,
                               int bufferSize) throws SecurityException {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new FileInputStream(inputFile);
            fos = new FileOutputStream(outputFile);

            encrypt(algorithm, providerName, key, fis, fos, bufferSize);
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured encrypting the data: ", th);
        } finally {
            IoUtil.closeIgnoringErrors(fis);
            IoUtil.closeIgnoringErrors(fos);
        }
    }

    /**
     * Decrypts binary content using a specified algorithm and key.
     *
     * @param algorithm the name of the decryption algorithm to use.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputFile the file containing the binary content to be decrypted
     * @param outputFile the detination file to contain the output cipher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String, String, Key, File, File, int)
     */
    public static void decrypt(String algorithm,
                               String providerName,
                               Key key,
                               File inputFile,
                               File outputFile,
                               int bufferSize) throws SecurityException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(inputFile);
            fos = new FileOutputStream(outputFile);

            decrypt(algorithm, providerName, key, fis, fos, bufferSize);
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured decrypting the data: ", th);
        } finally {
            IoUtil.closeIgnoringErrors(fis);
            IoUtil.closeIgnoringErrors(fos);
        }
    }

    /**
     * Encrypts binary input stream content to the specified outputStream using the default algorithm employed by this
     * utility and the specified key. The streams are <u>not</u> closed by this operation.
     *
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be encrypted
     * @param outputStream the destination output stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(Key, InputStream inputStream, OutputStream outputStream, int)
     */
    public static void encrypt(Key key, InputStream inputStream, OutputStream outputStream, int transferBufferSize) throws SecurityException {
        encrypt(null, null, key, inputStream, outputStream, transferBufferSize);
    }

    /**
     * Encrypts binary input stream content to the specified outputStream using the default algorithm employed by this
     * utility and the specified key. The streams are <u>not</u> closed by this operation.
     *
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be encrypted
     * @param outputStream the destination output stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(Key, InputStream inputStream, OutputStream outputStream, int)
     */
    public static void decrypt(Key key, InputStream inputStream, OutputStream outputStream, int transferBufferSize) throws SecurityException {
        decrypt(null, null, key, inputStream, outputStream, transferBufferSize);
    }

    /**
     * Encrypts binary input stream content to the specified outputStream using the specified algorithm and specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be encrypted
     * @param outputStream the destination output stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, Key, InputStream inputStream, OutputStream outputStream, int)
     */
    public static void encrypt(String algorithm,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int transferBufferSize) throws SecurityException {
        encrypt(algorithm, null, key, inputStream, outputStream, transferBufferSize);
    }

    /**
     * Decrypts binary input stream content to the specified outputStream using the specified algorithm and specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the decryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be decrypted
     * @param outputStream the destination output stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, Key, InputStream inputStream, OutputStream outputStream, int)
     */
    public static void decrypt(String algorithm,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int transferBufferSize) throws SecurityException {
        decrypt(algorithm, null, key, inputStream, outputStream, transferBufferSize);
    }

    /**
     * Encrypts binary input stream content to the specified outputStream using the specified algorithm and the specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be encrypted
     * @param outputStream the destination output stream to contain the cypher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key,InputStream, OutputStream, int)
     */
    public static void encrypt(String algorithm,
                               String providerName,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int bufferSize) throws SecurityException {
        encrypt(algorithm, (AlgorithmParameters) null, null, providerName, key, inputStream, outputStream, bufferSize);
    }

    /**
     * Encrypts binary input stream content to the specified outputStream using the specified algorithm and the specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param random any randomness the requested cipher should use during initialisation, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be encrypted
     * @param outputStream the destination output stream to contain the cypher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key,InputStream, OutputStream, int)
     */
    public static void encrypt(String algorithm,
                               AlgorithmParameters algorithmParameters,
                               SecureRandom random,
                               String providerName,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int bufferSize) throws SecurityException {
        boolean isSymmetricAlgorithm = key instanceof SecretKey;
        if (algorithm == null) {
            algorithm = (isSymmetricAlgorithm ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        try {
            // Get the cipher object for the specified algorithm
            Cipher cipher = providerName != null ? Cipher.getInstance(algorithm, providerName)
                    : Cipher.getInstance(algorithm);
            // Initialise the cipher using the specified parameters and/or rendomness.
            if (algorithmParameters != null && random != null) {
                cipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameters, random);
            }
            else if (algorithmParameters != null) {
                cipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameters);
            }
            else if (random != null) {
                cipher.init(Cipher.ENCRYPT_MODE, key, random);
            }
            else {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            crypt(cipher, key, inputStream, outputStream, bufferSize);
        } catch (java.security.GeneralSecurityException secEx) {
            throw new SecurityException(secEx);
        }
    }

    /**
     * Encrypts binary input stream content to the specified outputStream using the specified algorithm and the specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param random any randomness the requested cipher should use during initialisation, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be encrypted
     * @param outputStream the destination output stream to contain the cypher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key,InputStream, OutputStream, int)
     */
    public static void encrypt(String algorithm,
                               AlgorithmParameterSpec algorithmParameters,
                               SecureRandom random,
                               String providerName,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int bufferSize) throws SecurityException {
        boolean isSymmetricAlgorithm = key instanceof SecretKey;
        if (algorithm == null) {
            algorithm = (isSymmetricAlgorithm ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        try {
            // Get the cipher object for the specified algorithm
            Cipher cipher = providerName != null ? Cipher.getInstance(algorithm, providerName)
                    : Cipher.getInstance(algorithm);
            // Initialise the cipher using the specified parameters and/or randomness.
            if (algorithmParameters != null && random != null) {
                cipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameters, random);
            }
            else if (algorithmParameters != null) {
                cipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameters);
            }
            else if (random != null) {
                cipher.init(Cipher.ENCRYPT_MODE, key, random);
            }
            else {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            crypt(cipher, key, inputStream, outputStream, bufferSize);
        } catch (java.security.GeneralSecurityException secEx) {
            throw new SecurityException(secEx);
        }
    }

    /**
     * Decrypts binary input stream content to the specified outputStream using the specified algorithm and the specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be decrypted
     * @param outputStream the destination output stream to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key,InputStream, OutputStream, int)
     */
    public static void decrypt(String algorithm,
                               String providerName,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int bufferSize) throws SecurityException {
        decrypt(algorithm, (AlgorithmParameters) null, null, providerName, key, inputStream, outputStream, bufferSize);
    }

    /**
     * Decrypts binary input stream content to the specified outputStream using the specified algorithm and the specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param random any randomness the requested cipher should use during initialisation, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be decrypted
     * @param outputStream the destination output stream to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key,InputStream, OutputStream, int)
     */
    public static void decrypt(String algorithm,
                               AlgorithmParameters algorithmParameters,
                               SecureRandom random,
                               String providerName,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int bufferSize) throws SecurityException {
        boolean isSymmetricAlgorithm = key instanceof SecretKey;
        if (algorithm == null) {
            algorithm = (isSymmetricAlgorithm ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        try {
            // Get the cipher object for the specified algorithm
            Cipher cipher = providerName != null ? Cipher.getInstance(algorithm, providerName)
                    : Cipher.getInstance(algorithm);
            // Initialise the cipher using the specified parameters and/or rendomness.
            if (algorithmParameters != null && random != null) {
                cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters, random);
            }
            else if (algorithmParameters != null) {
                cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters);
            }
            else if (random != null) {
                cipher.init(Cipher.DECRYPT_MODE, key, random);
            }
            else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            crypt(cipher, key, inputStream, outputStream, bufferSize);
        } catch (java.security.GeneralSecurityException secEx) {
            throw new SecurityException(secEx);
        }
    }

    /**
     * Decrypts binary input stream content to the specified outputStream using the specified algorithm and the specified
     * key. The streams are <u>not</u> closed by this operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param random any randomness the requested cipher should use during initialisation, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be decrypted
     * @param outputStream the destination output stream to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key,InputStream, OutputStream, int)
     */
    public static void decrypt(String algorithm,
                               AlgorithmParameterSpec algorithmParameters,
                               SecureRandom random,
                               String providerName,
                               Key key,
                               InputStream inputStream,
                               OutputStream outputStream,
                               int bufferSize) throws SecurityException {
        boolean isSymmetricAlgorithm = key instanceof SecretKey;
        if (algorithm == null) {
            algorithm = (isSymmetricAlgorithm ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        try {
            // Get the cipher object for the specified algorithm
            Cipher cipher = providerName != null ? Cipher.getInstance(algorithm, providerName)
                    : Cipher.getInstance(algorithm);
            // Initialise the cipher using the specified parameters and/or rendomness.
            if (algorithmParameters != null && random != null) {
                cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters, random);
            }
            else if (algorithmParameters != null) {
                cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters);
            }
            else if (random != null) {
                cipher.init(Cipher.DECRYPT_MODE, key, random);
            }
            else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            crypt(cipher, key, inputStream, outputStream, bufferSize);
        } catch (java.security.GeneralSecurityException secEx) {
            throw new SecurityException(secEx);
        }
    }

    /**
     * Encrypts/Decrypts binary input stream content to the specified outputStream using the specified and configured
     * cipher. The streams are <u>not</u> closed by this operation.
     *
     * @param cipher the cipher to use in the encrypt/decrypt operation; the cipher must have been configured and
     *        <u>initialised</u> prior to calling this method.
     * @param key the key to use
     * @param inputStream the stream containing the binary content to be encrypted or decrypted
     * @param outputStream the destination output stream to contain the encrypted or decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     */
    public static void crypt(Cipher cipher,
                             Key key,
                             InputStream inputStream,
                             final OutputStream outputStream,
                             int bufferSize) throws SecurityException {
        boolean isSymmetricAlgorithm = key instanceof SecretKey;
        int blockSize = cipher.getBlockSize();

        CipherOutputStream cos = null;
        try {

            if (isSymmetricAlgorithm || (blockSize == 8) || (blockSize == 0)) {
                cos = new CipherOutputStream(new OutputStream() {
                    public void close() throws IOException {
                        // Don't close the underlying stream - just flush it
                        outputStream.flush();
                    }

                    public void write(byte b[], int off, int len) throws IOException {
                        outputStream.write(b, off, len);
                    }

                    public void write(int b) throws IOException {
                        outputStream.write(b);
                    }

                }, cipher);
                IoUtil.transfer(inputStream, cos, bufferSize);
            }
            else {
                byte inputBuf[] = new byte[blockSize];
                int readCount;
                while ((readCount = inputStream.read(inputBuf)) != -1) {
                    byte cipherText[] = cipher.doFinal(inputBuf, 0, readCount);
                    outputStream.write(cipherText);
                }
            }
        } catch (Throwable th) {
            throw new SecurityException("An unexpected error occured encrypting the data: ", th);
        } finally {
            IoUtil.closeIgnoringErrors(cos);
        }
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the system default secret
     * key.
     *
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(Resource, Resource)
     */
    public static void encrypt(Resource fromResource, Resource toResource) throws SecurityException {
        encrypt(null, null, embeddedSecretKey, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(Key, Resource, Resource)
     */
    public static void encrypt(Key key, Resource fromResource, Resource toResource) throws SecurityException {
        encrypt(null, null, key, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the system default secret
     * key.
     *
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(Resource, Resource)
     */
    public static void decrypt(Resource fromResource, Resource toResource) throws SecurityException {
        decrypt(null, null, embeddedSecretKey, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(Key, Resource, Resource)
     */
    public static void decrypt(Key key, Resource fromResource, Resource toResource) throws SecurityException {
        decrypt(null, null, key, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, Key, Resource, Resource)
     */
    public static void encrypt(String algorithm, Key key, Resource fromResource, Resource toResource) throws SecurityException {
        encrypt(algorithm, null, key, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the decryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, Key, Resource, Resource)
     */
    public static void decrypt(String algorithm, Key key, Resource fromResource, Resource toResource) throws SecurityException {
        decrypt(algorithm, null, key, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, Key, Resource, Resource, int)
     */
    public static void encrypt(String algorithm,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        encrypt(algorithm, null, key, fromResource, toResource, bufferSize);
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, Key, Resource, Resource, int)
     */
    public static void decrypt(String algorithm,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        decrypt(algorithm, null, key, fromResource, toResource, bufferSize);
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key, Resource, Resource, int)
     */
    public static void encrypt(String algorithm,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource) throws SecurityException {
        encrypt(algorithm, providerName, key, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key, Resource, Resource, int)
     */
    public static void encrypt(String algorithm,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        Assert.notNull(key, "The encryption key must not be null");
        if (algorithm == null) {
            algorithm = (key instanceof SecretKey ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            encrypt(algorithm, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key, Resource, Resource, int)
     */
    public static void encrypt(String algorithm,
                               AlgorithmParameterSpec algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource) throws SecurityException {
        encrypt(algorithm,
                algorithmParameters,
                providerName,
                key,
                fromResource,
                toResource,
                IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key, Resource, Resource, int)
     */
    public static void encrypt(String algorithm,
                               AlgorithmParameterSpec algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        Assert.notNull(key, "The encryption key must not be null");
        if (algorithm == null) {
            algorithm = (key instanceof SecretKey ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            encrypt(algorithm, algorithmParameters, null, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key, Resource, Resource, int)
     */
    public static void encrypt(String algorithm,
                               AlgorithmParameters algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource) throws SecurityException {
        encrypt(algorithm,
                algorithmParameters,
                providerName,
                key,
                fromResource,
                toResource,
                IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Encrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be encrypted
     * @param toResource the destination resource stream to contain the cypher text
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #decrypt(String, String, Key, Resource, Resource, int)
     */
    public static void encrypt(String algorithm,
                               AlgorithmParameters algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        Assert.notNull(key, "The encryption key must not be null");
        if (algorithm == null) {
            algorithm = (key instanceof SecretKey ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            encrypt(algorithm, algorithmParameters, null, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key, Resource, Resource, int)
     */
    public static void decrypt(String algorithm,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource) throws SecurityException {
        decrypt(algorithm, providerName, key, fromResource, toResource, IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key, Resource, Resource, int)
     */
    public static void decrypt(String algorithm,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        Assert.notNull(key, "The encryption key must not be null");
        if (algorithm == null) {
            algorithm = (key instanceof SecretKey ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            decrypt(algorithm, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key, Resource, Resource, int)
     */
    public static void decrypt(String algorithm,
                               AlgorithmParameters algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource) throws SecurityException {
        decrypt(algorithm,
                algorithmParameters,
                providerName,
                key,
                fromResource,
                toResource,
                IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key, Resource, Resource, int)
     */
    public static void decrypt(String algorithm,
                               AlgorithmParameters algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        Assert.notNull(key, "The encryption key must not be null");
        if (algorithm == null) {
            algorithm = (key instanceof SecretKey ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            decrypt(algorithm, algorithmParameters, null, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key, Resource, Resource, int)
     */
    public static void decrypt(String algorithm,
                               AlgorithmParameterSpec algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource) throws SecurityException {
        decrypt(algorithm,
                algorithmParameters,
                providerName,
                key,
                fromResource,
                toResource,
                IoUtil.DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Decrypts resource binary input stream content to the specified resource output stream using the specified
     * algorithm and the specified key. The resources are opened, if not already, and are subsequently closed by this
     * operation.
     *
     * @param algorithm the name of the encryption algorithm to use, which may be null. If null, the default symmetric or
     *        asymmetric cryptography algorithm is used, depending on whether the key specified is a symmetric or
     *        asymmetric key.
     * @param algorithmParameters the parameters to the specified algorithm, which may be null.
     * @param providerName the name of the cryptography provider to use, such as "BC" for Bouncy Castle, which may be
     *        null
     * @param key the key to use
     * @param fromResource the resource stream containing the binary content to be decrypted
     * @param toResource the destination resource stream to contain the decrypted data
     * @param bufferSize the input transfer buffer size of the buffer to use during the transfer for efficiency.
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #DEFAULT_ASYMMETRIC_ALGORITHM
     * @see #DEFAULT_SYMMETRIC_ALGORITHM
     * @see #encrypt(String, String, Key, Resource, Resource, int)
     */
    public static void decrypt(String algorithm,
                               AlgorithmParameterSpec algorithmParameters,
                               String providerName,
                               Key key,
                               Resource fromResource,
                               Resource toResource,
                               int bufferSize) throws SecurityException {
        Assert.notNull(key, "The encryption key must not be null");
        if (algorithm == null) {
            algorithm = (key instanceof SecretKey ? DEFAULT_SYMMETRIC_ALGORITHM : DEFAULT_ASYMMETRIC_ALGORITHM);
        }

        InputStream fromResourceIS = null;
        OutputStream toResourceOS = null;
        try {
            fromResourceIS = fromResource.getInputStream();
            toResourceOS = toResource.getOutputStream();
            decrypt(algorithm, algorithmParameters, null, providerName, key, fromResourceIS, toResourceOS, bufferSize);
        } finally {
            IoUtil.closeIgnoringErrors(fromResourceIS);
            IoUtil.closeIgnoringErrors(toResourceOS);
        }
    }

    /**
     * Encrypts binary content using a default, embedded, symmetric key and algorithm.
     *
     * @param data the binary content to be encrypted
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(byte[])
     */
    public static byte[] encrypt(byte[] data) throws SecurityException {
        return encrypt(DEFAULT_SYMMETRIC_ALGORITHM, embeddedSecretKey, data);
    }

    /**
     * Decrypts binary content using a default, embedded, symmetric key and algorithm.
     *
     * @param encryptedData the binary content to be decrypted
     * @return the decrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(byte[])
     */
    public static byte[] decrypt(byte[] encryptedData) throws SecurityException {
        return decrypt(DEFAULT_SYMMETRIC_ALGORITHM, embeddedSecretKey, encryptedData);
    }

    /**
     * Encrypts string content using a default, embedded, symmetric key and algorithm.
     *
     * @param data the binary content to be encrypted
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     * @see #decrypt(String)
     */
    public static byte[] encrypt(String data) throws SecurityException {
        return encrypt(data.getBytes());
    }

    /**
     * Decrypts string content using a default, embedded, symmetric key and algorithm.
     *
     * @param data the binary content to be decrypted
     * @return the decrypted data
     * @throws SecurityException if an unexpected error occurs during decryption
     * @see #encrypt(String)
     */
    public static byte[] decrypt(String data) throws SecurityException {
        return decrypt(data.getBytes());
    }

    /**
     * Encrypts string content using a default, embedded, symmetric key and algorithm.
     *
     * @param data the binary content to be encrypted
     * @param charSetName the character set encoding of the specified data
     * @return the encrypted cypher of the specified data
     * @throws SecurityException if an unexpected error occurs during encryption
     */
    public static byte[] encrypt(String data, String charSetName) throws SecurityException {
        try {
            return encrypt(data.getBytes(charSetName));
        } catch (UnsupportedEncodingException ex) {
            throw new SecurityException("Unable to encrypt data: ", ex);
        }
    }

    /**
     * Decrypts binary content using a specified algorithm and key, expecting a valid string as the decrypted result
     *
     * <p>
     * The system default chaacter encoding is assumed for the resulting decrypted characters.
     *
     * @param encryptedData the binary content to be encrypted
     * @return the decrypted data as a string
     * @throws SecurityException if an unexpected error occurs during encryption
     */
    public static String decryptToString(byte[] encryptedData) throws SecurityException {
        return new String(decrypt(encryptedData));
    }

    /**
     * Decrypts binary content using a specified algorithm and key, expecting a valid string as the decrypted result
     *
     * @param encryptedData the binary content to be encrypted
     * @param charSetName the character set encoding of the specified data
     * @return the decrypted data as a string
     * @throws SecurityException if an unexpected error occurs during encryption
     */
    public static String decryptToString(byte[] encryptedData, String charSetName) throws SecurityException {
        try {
            return new String(decrypt(encryptedData), charSetName);
        } catch (UnsupportedEncodingException ex) {
            throw new SecurityException("Unable to decrypt data as a string [characterSetName=" + charSetName + "]: ", ex);
        }
    }

    /**
     * Utility method to return all of the cryptograhic service providers registered in the system.
     *
     * @return a list of the cryptograhic service providers
     */
    public static String[] getServiceTypes() {
        Set<String> result = new HashSet<String>();

        for (Provider provider : Security.getProviders()) {
            // Get services provided by each provider
            for (Object key : provider.keySet()) {
                String keyStr = ((String) key).split(" ")[0];

                if (keyStr.startsWith("Alg.Alias.")) {
                    // Strip the alias
                    keyStr = keyStr.substring(10);
                }
                int ix = keyStr.indexOf('.');
                result.add(keyStr.substring(0, ix));
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * A utility method which returns a list of the cryptograhic implementations provided by the specified service
     * provider.
     *
     * @param serviceType the cryptograhic service for which all implementations are to be returned
     * @return a list of the cryptograhic implementations supported by the specified service provider
     */
    public static String[] getCryptoImpls(String serviceType) {
        Set<String> result = new HashSet<String>();

        for (Provider provider : Security.getProviders()) {
            // Get services provided by each provider
            for (Object key : provider.keySet()) {
                String keyStr = ((String) key).split(" ")[0];
                key = keyStr.split(" ")[0];

                if (keyStr.startsWith(serviceType + ".")) {
                    result.add(keyStr.substring(serviceType.length() + 1));
                }
                else if (keyStr.startsWith("Alg.Alias." + serviceType + ".")) {
                    // This is an alias
                    result.add(keyStr.substring(serviceType.length() + 11));
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Attempts to locate an return the public/private keypair from the specified keystore using the specified keystore
     * location, keystore type, keystore password, keypair alias and keypair alias password.
     *
     * @param keystoreFile the location of the keystore to load
     * @param aliasName the alias of the keypair entry in the store
     * @param aliasEntryPassword the password for th entry
     * @return the keypair, or null if the given alias does not exist or does not identify a key entry.
     * @throws SecurityException if an error occurs locating the key entry
     * @see #getKeyPairFromStore(InputStream, String, String, String, String)
     * @see KeyStore#getKey(String, char[]) for further information
     */
    public KeyPair getKeyPairFromStore(File keystoreFile,
                                       String keystoreType,
                                       String keyStorePassword,
                                       String aliasName,
                                       String aliasEntryPassword) throws SecurityException {
        if (!keystoreFile.exists() || !keystoreFile.canRead()) {
            throw new SecurityException("Unable to read keystore file: [" + keystoreFile.getAbsolutePath()
                                                + "]: the file does not exist or is not readable.");
        }

        InputStream keystoreIS = null;
        try {
            keystoreIS = new FileInputStream(keystoreFile);
            return getKeyPairFromStore(keystoreIS, keystoreType, keyStorePassword, aliasName, aliasEntryPassword);
        } catch (Exception ex) {
            throw new SecurityException("Unable to locate keypair in keystore [keystore=" + keystoreFile + ", alias="
                                                + aliasName + "]: ", ex);
        } finally {
            IoUtil.closeIgnoringErrors(keystoreIS);
        }
    }

    /**
     * Attempts to locate an return the public/private keypair from the specified keystore using the specified keystore
     * location, keystore type, keystore password, keypair alias and keypair alias password.
     *
     * @param keystoreURL the location of the keystore to load
     * @param aliasName the alias of the keypair entry in the store
     * @param aliasEntryPassword the password for th entry
     * @return the keypair, or null if the given alias does not exist or does not identify a key entry.
     * @throws SecurityException if an error occurs locating the key entry
     * @see #getKeyPairFromStore(InputStream, String, String, String, String)
     * @see KeyStore#getKey(String, char[]) for further information
     */
    public KeyPair getKeyPairFromStore(URL keystoreURL,
                                       String keystoreType,
                                       String keyStorePassword,
                                       String aliasName,
                                       String aliasEntryPassword) throws SecurityException {
        InputStream keystoreIS = null;
        try {
            keystoreIS = keystoreURL.openStream();
            return getKeyPairFromStore(keystoreIS, keystoreType, keyStorePassword, aliasName, aliasEntryPassword);
        } catch (Exception ex) {
            throw new SecurityException("Unable to locate keypair in keystore [keystore=" + keystoreURL.toExternalForm()
                                                + ", alias=" + aliasName + "]: ", ex);
        } finally {
            IoUtil.closeIgnoringErrors(keystoreIS);
        }
    }

    /**
     * Attempts to locate an return the public/private keypair from the specified keystore using the specified keystore
     * location, keystore type, keystore password, keypair alias and keypair alias password.
     *
     * @param keystoreIS the stream containing the keystore to load; it is up to th caller to close the stream after
     *        invoking this method.
     * @param aliasName the alias of the keypair entry in the store
     * @param aliasEntryPassword the password for th entry
     * @return the keypair, or null if the given alias does not exist or does not identify a key entry.
     * @throws SecurityException if an error occurs locating the key entry
     * @see #getKeyPairFromStore(KeyStore, String, String)
     * @see KeyStore#getKey(String, char[]) for further information
     */
    public KeyPair getKeyPairFromStore(InputStream keystoreIS,
                                       String keystoreType,
                                       String keyStorePassword,
                                       String aliasName,
                                       String aliasEntryPassword) throws SecurityException {
        try {
            KeyStore keystore = KeyStore.getInstance(keystoreType);
            keystore.load(keystoreIS, keyStorePassword.toCharArray());
            KeyPair keyPair = getKeyPairFromStore(keystore, aliasName, aliasEntryPassword);
            return keyPair;
        } catch (Exception ex) {
            throw new SecurityException("Unable to locate keypair in keystore [alias=" + aliasName + "]: ", ex);
        }
    }

    /**
     * Attempts to locate an return the public/private keypair from the specified keystore using the specified alias and
     * alias password.
     *
     * @param keystore the keystore containing the public/private key
     * @param aliasEntryPassword the password for th entry
     * @return the keypair, or null if the given alias does not exist or does not identify a key entry.
     * @throws SecurityException if an error occurs locating the key entry
     * @see KeyStore#getKey(String, char[]) for further information
     */
    public KeyPair getKeyPairFromStore(KeyStore keystore, String aliasName, String aliasEntryPassword) throws SecurityException {
        try {
            // Get private key
            Key key = keystore.getKey(aliasName, aliasEntryPassword.toCharArray());
            if (key instanceof PrivateKey) {
                // Get certificate of public key
                Certificate cert = keystore.getCertificate(aliasName);

                // Get public key
                PublicKey publicKey = cert.getPublicKey();

                // Return a key pair
                return new KeyPair(publicKey, (PrivateKey) key);
            }
        } catch (GeneralSecurityException ex) {
            throw new SecurityException("Unable to locate keypair in keystore [keystore=" + keystore + ", alias="
                                                + aliasName + "]: ", ex);
        }

        return null;
    }

    private static String getKeyTypeFromAlgorithm(String algorithm) {
        // Transformation is of the form algorithm/mode/padding
        int modePos = algorithm.indexOf('/');
        return modePos > 0 ? algorithm.substring(0, modePos) : algorithm;
    }

    public static void main(String ...  args) {
        SecretKey key = CryptoUtil.generateSymmetricKey("AES", defaultKeySizeMap.getOrDefault("AES", 128));

        System.out.println(key.getAlgorithm()+":"+key.getFormat()+":"+ Hex.encodeToHexString(key.getEncoded()));
    }

    public static void main_old(String args[]) {
        System.out.println("Service types:");
        String serviceTypes[] = getServiceTypes();
        for (int n = 0; n < serviceTypes.length; n++) {
            System.out.println(serviceTypes[n]);
        }

        System.out.println("\n\nCryptographic implementations:");
        String cryptoImpls[] = getCryptoImpls("Cipher");
        for (int n = 0; n < cryptoImpls.length; n++) {
            System.out.println(cryptoImpls[n]);
        }

        System.out.println("Generating key pair [algorithm=" + DEFAULT_ASYMMETRIC_ALGORITHM + ", key size="
                                   + DEFAULT_GENERATED_ASYMMETRIC_KEY_SIZE + "] ...");
        KeyPair keyPair = CryptoUtil.generateKeyPair();
        System.out.println("Encoding keys ...");
        byte[][] encodedKeys = generateEncodedPublicPrivateKeySpecs(keyPair);
        String publicKeyBase64 = Base64.encode(encodedKeys[0]);
        String privateKeyBase64 = Base64.encode(encodedKeys[1]);
        System.out.println("Public Key Base64 representation:\n_____STARTS BELOW____________________________________\n"
                                   + publicKeyBase64 + "\n_____ENDS ABOVE  ____________________________________");
        System.out.println("Private Key Base64 representation:\n_____STARTS BELOW____________________________________\n"
                                   + privateKeyBase64 + "\n_____ENDS ABOVE  ____________________________________");

        System.out.println("Generating secret key [algorithm=" + DEFAULT_SYMMETRIC_ALGORITHM + ", key size="
                                   + DEFAULT_GENERATED_ASYMMETRIC_KEY_SIZE + "] ...");
        SecretKey key = generateSymmetricKey();
        System.out.println("Encoding secret key ...");
        byte[] keySpec = key.getEncoded();
        String secretKeyBase64 = Base64.encode(keySpec);
        System.out.println("Secret Key Base64 representation:\n_____STARTS BELOW____________________________________\n"
                                   + secretKeyBase64 + "\n_____ENDS ABOVE  ____________________________________");

        String originalText = "Hello World!";
        byte encrypted[] = encrypt(originalText.getBytes());

        System.out.println("Encrypted text (base64) = =" + Base64.encode(encrypted) + " [original base64="
                                   + Base64.encode(originalText) + ", encrypted len=" + encrypted.length + "]");

        byte decrypted[] = decrypt(encrypted);
        System.out.println("Original =" + originalText + "\nDecrypted =" + (new String(decrypted)));

    }
}

