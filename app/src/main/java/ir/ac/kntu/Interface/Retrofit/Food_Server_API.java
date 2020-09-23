package ir.ac.kntu.Interface.Retrofit;

import java.util.List;

import ir.ac.kntu.Entity.Food;
import ir.ac.kntu.Entity.FoodMenu;
import ir.ac.kntu.Entity.Offer;
import ir.ac.kntu.Entity.WeLove;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Food_Server_API {

    /**
     * get favorite foods from server
     *
     * @return list of favorite foods
     */
    @GET("food_api/food_we_love.php")
    Call<List<WeLove>> getFoodsWeLove();

    /**
     * get food info from the server with selected ID
     *
     * @param foodID food id
     * @return food info object
     */
    @GET("food_api/food.php")
    Call<Food> getFood(@Query("food_id") int foodID);

    /**
     * get restaurant menu from server
     *
     * @return list of menu categories
     */
    @GET("food_api/food_menu.php")
    Call<List<FoodMenu>> getFoodMenu();

    /**
     * get lexeen offers from server
     *
     * @return list of offers
     */
    @GET("food_api/our_offer.php")
    Call<List<Offer>> getOurOffer();

    /**
     * get fields of food from server with selected food ID
     *
     * @param foodID food id
     * @return list of fields
     */
    @GET("food_api/food_field.php")
    Call<List<String>> getFoodField(@Query("food_id") int foodID);

    /**
     * get list of foods having name 'foodName'
     *
     * @param foodName food name
     * @return list of foods
     */
    @GET("food_api/search_food.php")
    Call<List<Food>> getFoodsContaining(@Query("food_name") String foodName);


}
