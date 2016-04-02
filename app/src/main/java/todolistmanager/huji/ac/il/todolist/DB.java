package todolistmanager.huji.ac.il.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yarden Oren on 29/03/2016.
 */
    public class DB extends SQLiteOpenHelper {

        SQLiteDatabase todo_db;

        public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            this.todo_db = this.getWritableDatabase();
            this.todo_db.execSQL("CREATE TABLE IF NOT EXISTS tasks(`title` TEXT, `due` REAL);");
        }

        public List<ItemLine> getAllTasks(){
            List<ItemLine> tasks = new ArrayList<ItemLine>();
            Cursor cursor = todo_db.rawQuery("SELECT * FROM tasks", null);
            if (cursor.moveToFirst()) {
                do {
                    tasks.add(new ItemLine(cursor.getString(cursor.getColumnIndex("title")), cursor.getLong(cursor.getColumnIndex("due"))));
                } while (cursor.moveToNext());
            }
            return tasks;
        }

        public void saveTask(String task, long dueDate) {
            todo_db.execSQL("INSERT INTO tasks (`title`, `due`) VALUES ('" +task + "','" + dueDate + "');");
        }

        public void deleteTask(String task, long dueDate){
            todo_db.execSQL("DELETE FROM tasks WHERE title ='"+task+"' AND due ='"+dueDate+"';");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

