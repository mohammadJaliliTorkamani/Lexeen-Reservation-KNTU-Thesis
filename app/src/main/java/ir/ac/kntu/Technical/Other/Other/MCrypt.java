package ir.ac.kntu.Technical.Other.Other;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * encryptor/decryptor base class (used in  class "Encryptor")
 */
public class MCrypt {

    private String iv;
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    private String SecretKey;

    public MCrypt(long sharedKey) throws Exception {
        String sharedKeyStr = String.valueOf(sharedKey);
        String hashedString = Helper.getInstance().hash(sharedKeyStr);
        if (hashedString.length() < 16)
            throw new Exception("not enough length");
        SecretKey = hashedString.substring(0, 16);
        iv = hashedString.substring(hashedString.length() - 16);
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        cipher = Cipher.getInstance("AES/CBC/NoPadding");
    }

    /**
     * converts byte array to HEX string
     *
     * @param data byte array to be converted
     * @return HEX string
     */
    private static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
        }
        return str;
    }

    /**
     * converts HEX string to byte array
     *
     * @param str hex string
     * @return byte array
     */
    private static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    /**
     * put padding to source (used in encryption)
     *
     * @param source source to add padding
     * @return fully padded string
     */
    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        StringBuilder sourceBuilder = new StringBuilder(source);
        for (int i = 0; i < padLength; i++) {
            sourceBuilder.append(paddingChar);
        }
        source = sourceBuilder.toString();

        return source;
    }

    /**
     * encrypts string
     *
     * @param text raw string to encrypt
     * @return encrypted string (cipher)
     * @throws Exception when encryption failed
     */
    public String encrypt(String text) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");
        if (Helper.getInstance().getSharedKey() == null)
            return text;
        byte[] encrypted;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }


        return bytesToHex(encrypted);
    }

    /**
     * encrypts string
     *
     * @param code cipher to decrypt
     * @return decrypted string (raw string)
     * @throws Exception when decryption failed
     */
    public String decrypt(String code) throws Exception {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");
        if (Helper.getInstance().getSharedKey() == null)
            return code;
        byte[] decrypted;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            decrypted = cipher.doFinal(hexToBytes(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }

        return new String(decrypted).trim();
    }
}

