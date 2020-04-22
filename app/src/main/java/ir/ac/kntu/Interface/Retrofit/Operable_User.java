package ir.ac.kntu.Interface.Retrofit;

import java.util.List;

import ir.ac.kntu.Entity.NormalUser;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Operable_User {
    @GET("user_api/drawer_content.php")
    Call<NormalUser> getDrawerContent();

    @POST("user_api/remained_cash.php")
    Call<Double> getCash();

    @POST("user_api/acceptable_cash_amounts.php")
    Call<List<Double>> getAcceptableCashAmounts();

    @POST("food_api/order.php")
    Call<ServerResponse> order(@Body Order order);

    @GET("user_api/charge_wallet.php")
    Call<ServerResponse> chargeWallet(@Query("ref_id") String refID, @Query("amount") float amount);

    @POST("food_api/deliver.php")
    Call<ServerResponse> deliver(@Body Order order);

    @POST("user_api/log_out.php")
    Call<ServerResponse> logOut();
}
