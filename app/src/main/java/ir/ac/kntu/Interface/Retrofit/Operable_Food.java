package ir.ac.kntu.Interface.Retrofit;

import java.util.List;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Food;
import ir.ac.kntu.Entity.FoodMenu;
import ir.ac.kntu.Entity.Offer;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.WeLove;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Operable_Food {
    @POST("food_api/food_we_love.php")
    Call<List<WeLove>> getFoodsWeLove();

    @GET("food_api/food.php")
    Call<Food> getFood(@Query("food_id") int foodID);

    @POST("food_api/food_menu.php")
    Call<List<FoodMenu>> getFoodMenu();

    @POST("food_api/our_offer.php")
    Call<List<Offer>> getOurOffer();

    @GET("food_api/food_field.php")
    Call<List<String>> getFoodField(@Query("food_id") int foodID);

    @POST("food_api/food_bill.php")
    Call<List<Bill>> completeBills(@Body List<Bill> list);

    @GET("food_api/search_food.php")
    Call<List<Food>> getFoodsContaining(@Query("food_name") String foodName);

    @GET("food_api/orders.php")
    Call<List<Order>> getOrders();
}
