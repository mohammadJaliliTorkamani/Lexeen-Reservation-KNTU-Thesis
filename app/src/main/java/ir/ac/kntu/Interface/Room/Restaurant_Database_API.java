package ir.ac.kntu.Interface.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Technical.Other.Other.Constants;

@Dao
public interface Restaurant_Database_API {

    /**
     * retrieves all the restaurants from DB
     *
     * @return list of restaurants
     */
    @Query("SELECT * FROM " + Constants._DATABASE_RESTAURANT_TABLE)
    List<Restaurant> getAll();

    /**
     * counts number of restaurants exist in DB
     *
     * @return number of restaurants
     */
    @Query("SELECT COUNT(*) from " + Constants._DATABASE_RESTAURANT_TABLE)
    int countRestaurants();

    /**
     * inserts restaurants to DB
     *
     * @param restaurants to be inserted
     */
    @Insert
    void insert(Restaurant... restaurants);

    /**
     * deletes restaurant from DB
     *
     * @param restaurant to be deleted
     */
    @Delete
    void delete(Restaurant restaurant);

    /**
     * gets restaurant with id
     *
     * @param id id of restaurant
     * @return restaurant
     */
    @Query("SELECT * FROM " + Constants._DATABASE_RESTAURANT_TABLE + " WHERE id LIKE :id")
    Restaurant getRestaurant(int id);

    /**
     * gets restaurant with decryptedCode
     *
     * @param decryptedCode decrypted code of restaurant
     * @return restaurant
     */
    @Query("SELECT * FROM " + Constants._DATABASE_RESTAURANT_TABLE + " WHERE qrCode LIKE :decryptedCode")
    Restaurant getRestaurant(String decryptedCode);

    @Query("DELETE FROM " + Constants._DATABASE_RESTAURANT_TABLE + " WHERE id > 0")
    void clearAll();
}