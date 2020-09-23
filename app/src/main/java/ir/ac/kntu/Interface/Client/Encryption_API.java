package ir.ac.kntu.Interface.Client;

import android.view.View;

import java.io.IOException;

import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;

public interface Encryption_API {
    long G = 6;
    long Prime = 13;

    /**
     * encrypts string and returns cipher
     *
     * @param str string to encrypt
     * @return cipher
     * @throws Exception when encryption failed
     */
    String encrypt(String str) throws Exception;

    /**
     * decrypts cipher and returns raw strings
     *
     * @param str string to decrypt
     * @return raw string
     * @throws Exception when decryption failed
     */
    String decrypt(String str) throws Exception;

    /**
     * generate(store) G
     */
    void generateG();

    /**
     * generate(store) Prime number
     */
    void generatePrime();

    /**
     * creates private key and return it
     *
     * @return created private key
     */
    long selectPrivateKey();

    /**
     * calculates X from the private key (used in Diffie-Helman)
     *
     * @param PrivateKey private key to calculate X from.
     */
    void calculateX(long PrivateKey);

    /**
     * computes shared secret key from the private key (needs server operations) and runs runnable
     * after calculations
     *
     * @param view       view to connect server
     * @param privateKey private key to calculate shared secret key from
     * @param runnable   runnable to run after calculations
     * @throws IOException when shared key calculations failed
     */
    void computeSharedSecretKey(View view, long privateKey, Runnable_SingleArg<Long> runnable) throws IOException;
}
