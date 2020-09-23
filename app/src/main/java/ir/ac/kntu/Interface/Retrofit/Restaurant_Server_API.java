package ir.ac.kntu.Interface.Retrofit;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Entity.RestaurantClass;
import ir.ac.kntu.Entity.ServiceActivation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Restaurant_Server_API {
    /**
     * search among the restaurants having name
     *
     * @param restaurantName restaurant name
     * @return list of restaurants
     */
    @GET("restaurant_api/search_restaurants.php")
    Call<List<Restaurant>> searchRestaurants(@Query("restaurant_name") String restaurantName);

    /**
     * gets restaurant classes
     *
     * @return list of classes
     */
    @GET("restaurant_api/restaurant_classes.php")
    Call<List<RestaurantClass>> getRestaurantClasses();

    /**
     * gets location of restaurant (header will be auto assigned)
     *
     * @return loaation object
     */
    @GET("restaurant_api/restaurant_location.php")
    Call<LatLng> getRestaurantLocation();

    /**
     * gets all the restaurants belong to the selected class
     *
     * @param restaurantClass restaurant class
     * @return list of restaurants in the selected class
     */
    @GET("restaurant_api/restaurants.php")
    Call<List<Restaurant>> getRestaurantList(@Query("class") String restaurantClass);

    /**
     * get roofs of restaurants (header will be auto assigned)
     *
     * @return
     */
    @GET("restaurant_api/roofs.php")
    Call<List<Integer>> getRoofs();

    /**
     * get restaurant info with qr code
     *
     * @param qrCode QR of restaurant
     * @return restaurant wih selected QR Code
     */
    @GET("restaurant_api/restaurant_info.php")
    Call<Restaurant> getRestaurantInfo(@Query("qrCode") String qrCode);

    /**
     * checks whether restaurant is active for serve or no
     * (if no, active for serve hours will be retrieved)
     *
     * @return check result OBJECT
     */
    @GET("restaurant_api/is_active_for_serve.php")
    Call<ServiceActivation> isActiveForServe();

    /**
     * checks whether restaurant is active for delivery or no
     * (if no, active for delivery hours will be retrieved)
     *
     * @return check result OBJECT
     */
    @GET("restaurant_api/is_active_for_delivery.php")
    Call<ServiceActivation> isActiveForDelivery();
}
