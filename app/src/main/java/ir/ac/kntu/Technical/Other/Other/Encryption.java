package ir.ac.kntu.Technical.Other.Other;

import android.util.Log;
import android.view.View;

import ir.ac.kntu.Interface.Client.Encryption_API;
import ir.ac.kntu.Interface.Retrofit.Account_Server_API;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Encryption implements Encryption_API {
    private static Encryption instance;
    private MCrypt cipher;
    private long clientPrivateKey;
    private long x;
    private long y;
    private long G;
    private long P;
    private long sharedKey;

    private Encryption() {
    }

    public static Encryption getInstance() {
        if (instance == null)
            instance = new Encryption();
        return instance;
    }

    @Override
    public String encrypt(String str) {
        String sharedKey = Helper.getInstance().getSharedKey();
        if (sharedKey == null)
            return str;
        if (cipher == null) {
            try {
                long sharedKeyValue = Long.parseLong(sharedKey);
                cipher = new MCrypt(sharedKeyValue);
            } catch (Exception e) {
                Helper_Log.errorLog(e, Encryption.class);
            }
        }
        try {
            return cipher.encrypt(str);
        } catch (Exception e) {
            Helper_Log.errorLog(e, Encryption.class);
            return str;
        }
    }

    @Override
    public String decrypt(String str) {
        String sharedKey = Helper.getInstance().getSharedKey();
        if (sharedKey == null)
            return str;
        if (cipher == null) {
            try {
                long sharedKeyValue = Long.parseLong(sharedKey);
                cipher = new MCrypt(sharedKeyValue);
            } catch (Exception e) {
                Helper_Log.errorLog(e, Encryption.class);
            }
        }
        try {
            return cipher.decrypt(str);
        } catch (Exception e) {
            Helper_Log.errorLog(e, Encryption.class);
            return str;
        }
    }

    @Override
    public void generateG() {
        this.G = Encryption_API.G;
    }

    @Override
    public void generatePrime() {
        this.P = Encryption_API.Prime;
    }

    @Override
    public long selectPrivateKey() {
        clientPrivateKey = Helper.getInstance().generateRandomNumber(3, 14);
        return clientPrivateKey;
    }

    @Override
    public void calculateX(long privateKey) {
        this.x = (long) (Math.pow(G, privateKey) % P);
    }

    @Override
    public void computeSharedSecretKey(View view, long privateKey, Runnable_SingleArg<Long> runnable) {
        Connector.createService(view, Account_Server_API.class, object -> object.exchangeKeys(this.x).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.body() != null) {
                    y = response.body();
                    sharedKey = (long) (Math.pow(y, clientPrivateKey) % (P));
                    Log.d("XCXC", "" + sharedKey + "#" + privateKey);
                    Setting.getInstance().saveSetting(Constants._TABLE_PROFILE, Constants._KEY_SHARED_KEY, String.valueOf(sharedKey));
                    try {
                        cipher = new MCrypt(sharedKey);
                    } catch (Exception e) {
                        Helper_Log.errorLog(e, Encryption.class);
                    }
                    runnable.run(sharedKey);
                } else {
                    Log.d("XCXC2", "" + privateKey + ",nulllll");
                    runnable.run((long) -1);
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.d("XCXC2", "" + privateKey + "$" + t.getMessage());
                Helper_Log.errorLog(t, Encryption.class);
            }
        }));
    }
}
