package ir.ac.kntu.Interface.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Technical.Other.Other.Constants;

@Dao
public interface Operable_Restaurant {

    @Query("SELECT * FROM " + Constants._DATABASE_RESTAURANT_TABLE)
    List<Restaurant> getAll();

    @Query("SELECT COUNT(*) from " + Constants._DATABASE_RESTAURANT_TABLE)
    int countRestaurants();

    @Insert
    void insert(Restaurant... restaurants);

    @Delete
    void delete(Restaurant restaurant);

    @Query("SELECT * FROM " + Constants._DATABASE_RESTAURANT_TABLE + " WHERE id LIKE :id")
    Restaurant getRestaurant(int id);

    @Query("SELECT * FROM " + Constants._DATABASE_RESTAURANT_TABLE + " WHERE encryptedCode LIKE :encryptedCode")
    Restaurant getRestaurant(String encryptedCode);

}