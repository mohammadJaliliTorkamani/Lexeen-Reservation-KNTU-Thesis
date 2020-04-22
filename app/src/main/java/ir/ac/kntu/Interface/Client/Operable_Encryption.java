package ir.ac.kntu.Interface.Client;

import android.view.View;

import java.io.IOException;

import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;

public interface Operable_Encryption {
    long G = 6;
    long Prime = 13;

    String encrypt(String str) throws Exception;

    String decrypt(String str) throws Exception;

    void generateG();

    void generatePrime();

    long selectPrivateKey();

    void calculateX(long PrivateKey);

    void computeSharedSecretKey(View view, long privateKey, Runnable_SingleArg<Long> runnable) throws IOException;
}
