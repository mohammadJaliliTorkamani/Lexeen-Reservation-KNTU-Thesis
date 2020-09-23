package ir.ac.kntu.Interface.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Technical.Other.Other.Constants;

@Dao
public interface Order_Database_API {
    /**
     * inserts orders into DB
     *
     * @param order
     * @return
     */
    @Insert
    long[] insert(Order... order);

    /**
     * retrieves order with orderID
     *
     * @param orderID to retrieve order object
     * @return order
     */
    @Query("SELECT * FROM " + Constants._DATABASE_ORDER_TABLE + " WHERE orderID = :orderID")
    List<Order> getOrder(int orderID);

    /**
     * clears order table
     */
    @Query("DELETE FROM " + Constants._DATABASE_ORDER_TABLE + " WHERE orderID>=0")
    void clearAll();

    /**
     * removes order with orderID from order table
     *
     * @param orderID to remove corresponding order object from DB
     */
    @Query("DELETE FROM " + Constants._DATABASE_ORDER_TABLE + " WHERE orderID=:orderID")
    void remove(int orderID);
}