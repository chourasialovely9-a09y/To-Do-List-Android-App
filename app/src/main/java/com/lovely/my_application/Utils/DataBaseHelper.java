package com.lovely.my_application.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lovely.my_application.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="TODO_DATABASE";
    private static final String TABLE_NAME="TODO_TABLE";
    private static final String COL_1="ID";
    private static final String COL_2="TASK";
    private static final String COL_3="STATUS";

    public DataBaseHelper( Context context )
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "  + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void insertTask(ToDoModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, model.getTask());
        contentValues.put(COL_3,model.getStatus());
        db.insert(TABLE_NAME,null,contentValues);
        db.close();
    }
    public void updateTask(int id,String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,task);
        db.update(TABLE_NAME,contentValues,"ID=?",new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateStatus(int id,int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,status);
        db.update(TABLE_NAME, contentValues , "ID=?",new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(id)});
        db.close();
        }

    public List<ToDoModel> getAllTasks() {

        List<ToDoModel> modelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "STATUS ASC, ID DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ToDoModel toDoModel = new ToDoModel();
                toDoModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_1)));
                toDoModel.setTask(cursor.getString(cursor.getColumnIndexOrThrow(COL_2)));
                toDoModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_3)));
                modelList.add(toDoModel);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return modelList;
    }

}
