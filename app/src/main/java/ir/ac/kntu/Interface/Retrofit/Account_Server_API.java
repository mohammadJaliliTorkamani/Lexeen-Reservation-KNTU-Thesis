package ir.ac.kntu.Interface.Retrofit;

import ir.ac.kntu.Entity.AuthenticationResponse;
import ir.ac.kntu.Entity.NormalUser;
import ir.ac.kntu.Entity.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Account_Server_API {

    /**
     * get user info which is shown in drawer
     *
     * @return user info object
     */
    @GET("account_api/drawer_content.php")
    Call<NormalUser> getDrawerContent();

    /**
     * get user cash
     *
     * @return cash
     */
    @GET("account_api/remained_cash.php")
    Call<Double> getCash();

    /**
     * charge wallet with refID (which is got after payment) and amount to charge
     *
     * @param refID  refrence ID
     * @param amount amount to charge
     * @return charge result
     */
    @FormUrlEncoded
    @POST("account_api/charge_wallet.php")
    Call<ServerResponse> chargeWallet(@Field("ref_id") String refID, @Field("amount") float amount);

    /**
     * submit logout to server
     *
     * @return logout result
     */
    @POST("account_api/log_out.php")
    Call<ServerResponse> logOut();

    /**
     * send activation register code to the phonen umber
     *
     * @param phone phone to send code inro.
     * @return send result
     */
    @FormUrlEncoded
    @POST("account_api/enter_register_verification_code.php")
    Call<ServerResponse> sendRegisterVerificationCode(@Field("phone") String phone);

    /**
     * send forget operation activation code to phone
     *
     * @param phone phone to send code into.
     * @return send result
     */
    @FormUrlEncoded
    @POST("account_api/check_send.php")
    Call<ServerResponse> checkSend(@Field("phone") String phone);

    /**
     * sends user input code into server to check with sent code
     *
     * @param code user input ode
     * @return check result
     */
    @FormUrlEncoded
    @POST("account_api/enter_forgetness_verification_code.php")
    Call<ServerResponse> sendForgetnessVerificationCode(@Field("code") String code);

    /**
     * resets user profile password if forgot
     *
     * @param password new password
     * @param phone    user phone number
     * @return reset result
     */
    @FormUrlEncoded
    @POST("account_api/reset_forgot_password.php")
    Call<ServerResponse> resetForgotPassword(@Field("password") String password, @Field("phone") String phone);

    /**
     * signs up the user with received details
     *
     * @param code        activation code
     * @param firstName   first name
     * @param lastName    last name
     * @param phoneNumber phone
     * @param password    password
     * @param isMale      gender (true = male)
     * @return register result
     */
    @FormUrlEncoded
    @POST("account_api/register.php")
    Call<AuthenticationResponse> register(@Field("code") String code, @Field("first_name") String firstName,
                                          @Field("last_name") String lastName, @Field("phone_number") String phoneNumber,
                                          @Field("password") String password, @Field("male") boolean isMale);

    /**
     * exchanges key for DiffieHelman protocol
     *
     * @param clientPrivateKey user private key
     * @return server key
     */
    @GET("account_api/key_exchange.php")
    Call<Long> exchangeKeys(@Query("client_private_key") long clientPrivateKey);

    /**
     * login user
     *
     * @param username username
     * @param password password
     * @return login result
     */
    @GET("account_api/login.php")
    Call<AuthenticationResponse> login(@Query("username") String username, @Query("password") String password);
}
