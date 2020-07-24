package ir.ac.kntu.Technical.Other.Other;

import android.view.View;

import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import ir.ac.kntu.Entity.LexinMarchant;
import ir.ac.kntu.Interface.Retrofit.General_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_MultiArg;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * used for payment operation
 */
public class Payment {
    private static Payment instance;

    private Payment() {
    }

    public static Payment getInstance() {
        if (instance == null)
            instance = new Payment();
        return instance;
    }

    /**
     * navigates user to payment web page and runs "afterrun" when payment completed(whether succeed or failed)
     *
     * @param view       to connect server
     * @param priceToman price to purchase
     * @param afterRun   runnable after operation
     */
    public void pay(View view, long priceToman, Runnable_MultiArg<Object> afterRun) {
        Connector.createService(view, General_Server_API.class, object -> object.getMarchantInfo().enqueue(new Callback<LexinMarchant>() {
            @Override
            public void onResponse(Call<LexinMarchant> call, Response<LexinMarchant> response) {
                if (response.body() != null) {
                    try {
                        LexinMarchant marchant = response.body();
                        marchant.setEmail(Encryption.getInstance().decrypt(marchant.getEmail()));
                        marchant.setMarchantID(Encryption.getInstance().decrypt(marchant.getMarchantID()));
                        marchant.setPhone(Encryption.getInstance().decrypt(marchant.getPhone()));
                        PaymentRequest payment = ZarinPal.getPaymentRequest();
                        payment.setMerchantID(marchant.getMarchantID());
                        payment.setAmount(priceToman);
                        payment.setDescription(ContextHelper.retrieveContext().getString(R.string.marchant_description_wallet_charge));
                        payment.setCallbackURL("app://zarinpal_payment");
                        if (marchant.getPhone() != null)
                            payment.setMobile(marchant.getPhone());
                        if (marchant.getEmail() != null)
                            payment.setEmail(marchant.getEmail());

                        ZarinPal.getPurchase(ContextHelper.retrieveContext()).startPayment(payment, (status, authority, paymentGatewayUri, intent) -> afterRun.run(intent));
                    } catch (Exception e) {
                        Helper_Log.errorLog(e, Payment.class);
                    }
                } else {
                    Helper_Log.errorLog(Payment.class);
                }
            }

            @Override
            public void onFailure(Call<LexinMarchant> call, Throwable t) {
                Helper_Log.errorLog(t, Payment.class);
            }
        }));
    }
}
