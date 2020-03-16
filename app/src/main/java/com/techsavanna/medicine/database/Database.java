package com.techsavanna.medicine.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.techsavanna.medicine.models.OrderModel;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    public final static String DB_NAME = "smartdishes.db";
    public final static int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<OrderModel> getCarts(){
        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ProductName", "Quantity", "ProductId", "Price", "Category", "Image"};
        String sqlTable = "OrderDetail";
        queryBuilder.setTables(sqlTable);

        Cursor cursor = queryBuilder.query(database, sqlSelect, null, null, null, null, null);
        final List<OrderModel> result = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                result.add(new OrderModel(cursor.getString(cursor.getColumnIndex("ProductId")),
                cursor.getString(cursor.getColumnIndex("ProductName")),
                cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Category")),
                        cursor.getString(cursor.getColumnIndex("Image"))
                ));
            }while (cursor.moveToNext());
        }
        return result;
    }

    public void addToCart(OrderModel model){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId, ProductName, Quantity, Price, Category, Image) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');",
        model.getProductId(),
                model.getProductName(),
                model.getQuantity(),
                model.getPrice(),
                model.getCategory(),
                model.getImage());
        sqLiteDatabase.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        sqLiteDatabase.execSQL(query);
    }

    public void removeCart(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail(ProductId) VALUES('%s')");
        sqLiteDatabase.execSQL(query);
    }
}
