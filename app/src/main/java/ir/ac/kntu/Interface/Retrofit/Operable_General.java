package ir.ac.kntu.Interface.Retrofit;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import ir.ac.kntu.Entity.AuthenticationResponse;
import ir.ac.kntu.Entity.Discount;
import ir.ac.kntu.Entity.FAQ;
import ir.ac.kntu.Entity.LexinMarchant;
import ir.ac.kntu.Entity.RefactoredBills;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Entity.RestaurantClass;
import ir.ac.kntu.Entity.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Operable_General {
    @GET("general_api/key_exchange.php")
    Call<Long> exchangeKeys(@Query("client_private_key") long clientPrivateKey);

    @GET("general_api/login.php")
    Call<AuthenticationResponse> login(@Query("shared_key") long sharedKey, @Query("username") String username, @Query("password") String password);

    @GET("general_api/register.php")
    Call<AuthenticationResponse> register(@Query("code") String code, @Query("first_name") String firstName,
                                          @Query("last_name") String lastName, @Query("phone_number") String phoneNumber,
                                          @Query("password") String password, @Query("male") boolean isMale);

    @GET("general_api/restaurant_info.php")
    Call<Restaurant> getRestaurantInfo(@Query("qrCode") String qrCode);

    @GET("general_api/faqs.php")
    Call<List<FAQ>> getFAQs(@Query("order") String mode);

    @GET("general_api/marchant_id.php")
    Call<LexinMarchant> getMarchantInfo();

    @GET("user_api/discount_with_code.php")
    Call<Discount> getDiscountWithCode(@Query("code") String code);

    @GET("general_api/roofs.php")
    Call<List<Integer>> getRoofs();

    @GET("general_api/restaurants.php")
    Call<List<Restaurant>> getRestaurantList(@Query("class") String restaurantClass);

    @GET("general_api/check_send.php")
    Call<ServerResponse> checkSend(@Query("phone") String phone);

    @GET("general_api/enter_forgetness_verification_code.php")
    Call<ServerResponse> sendForgetnessVerificationCode(@Query("code") String code);

    @GET("general_api/reset_forgot_password.php")
    Call<ServerResponse> resetForgotPassword(@Query("password") String password, @Query("phone") String phone);

    @GET("general_api/enter_register_verification_code.php")
    Call<ServerResponse> sendRegisterVerificationCode(@Query("phone") String phone);

    @GET("general_api/restaurant_location.php")
    Call<LatLng> getRestaurantLocation();

    @GET("general_api/check_for_update.php")
    Call<ServerResponse> isLatestAppVersion();

    @POST("general_api/is_good_order.php")
    Call<ServerResponse> isGoodOrder(@Body RefactoredBills refactoredBills);

    @GET("general_api/search_restaurants.php")
    Call<List<Restaurant>> searchRestaurants(@Query("restaurant_name") String restaurantName);

    @GET("general_api/restaurant_classes.php")
    Call<List<RestaurantClass>> getRestaurantClasses();
}