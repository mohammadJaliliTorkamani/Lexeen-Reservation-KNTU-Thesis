package ir.ac.kntu.Interface.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Technical.Other.Other.Constants;

@Dao
public interface Operable_Order {
    @Insert
    long[] insert(Order... order);

    @Query("SELECT * FROM " + Constants._DATABASE_ORDER_TABLE + " WHERE orderID = :orderID")
    List<Order> getOrder(int orderID);

    @Query("DELETE FROM " + Constants._DATABASE_ORDER_TABLE + " WHERE orderID>=0")
    void clearAll();

    @Query("DELETE FROM " + Constants._DATABASE_ORDER_TABLE + " WHERE orderID=:orderID")
    void remove(int orderID);
}