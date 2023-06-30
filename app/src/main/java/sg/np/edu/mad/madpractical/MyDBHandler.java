package sg.np.edu.mad.madpractical;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {

    public static String Database_Name = "users.db";

    public static String USER = "Users";
    public static String col_name = "Name";
    public static String col_followed = "followed";
    public static String col_id = "Id";
    public static String col_desc = "Description";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Database_Name, factory, 1);
        Log.i("MyDBHandler", "DB constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String drop_table = "DROP TABLE IF EXISTS Users";
        String CREATE_TABLE = "Create table " + USER + "(" +
                col_name + " text, " +
                col_desc  + " text, " +
                col_id + " integer primary key autoincrement, " +
                col_followed + " boolean" + ")";
        db.execSQL(drop_table);
        db.execSQL(CREATE_TABLE);
    }

    public void addUser(User userData){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO Users(Name, Description, followed) VALUES( \"" + userData.getName() + "\"," +  "\"" + userData.getDescription() + "\"," + userData.isFollowed() + ")");
        //INSERT INTO User Values( "name", "description",id,followed)
        db.close();
    }

    public ArrayList<User> getUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<User> userlist = new ArrayList<User>();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);
        while(cursor.moveToNext()){
            User user = new User();
            user.setName(cursor.getString(0));
            user.setDescription(cursor.getString(1));
            user.setId(cursor.getInt(2));
            user.setFollowed(Boolean.parseBoolean(cursor.getString(3)));
            userlist.add(user);
        }
        db.close();
        return userlist;
    }

    public void updateUser(User updatedUser){
        SQLiteDatabase db = this.getWritableDatabase();
        String UPDATE = "UPDATE " + USER + " SET " +
                col_followed + " =\'" + updatedUser.followed +"\' " +
                col_name + " =\'" + updatedUser.name + "\'" +
                col_desc + " =\'" + updatedUser.description + "\' WHERE " +
                col_id + " =" + updatedUser.id;
        db.execSQL(UPDATE);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER);
        onCreate(db);
    }

}
