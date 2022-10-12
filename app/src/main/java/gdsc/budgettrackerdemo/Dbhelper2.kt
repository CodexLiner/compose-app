package gdsc.budgettrackerdemo

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class Dbhelper2(context:Context,factory: SQLiteDatabase.CursorFactory):SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION){

    companion object{
        var DATABASE_NAME = "GDSC"
        var DATABASE_VERSION:Int = 1
    }

    override fun onCreate(db: SQLiteDatabase) {

        val create_query:String = ("CREATE TABLE EXPENSE_TABLE (EXP_NAME TEXT,EXP_AMT INT)")
        db.execSQL(create_query)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertExp(expName:String, expAMt:Int)
    {
        val insert_query:String = ("INSERT INTO EXPENSE_TABLE VALUES('$expName',$expAMt)")
        var db = this.writableDatabase
        db.execSQL(insert_query)
    }

    fun show_exp():MutableList<expense_details>
    {
        val show_query = ("SELECT * FROM EXPENSE_TABLE")
        var db = this.readableDatabase

        var cursor:Cursor = db.rawQuery(show_query,null)

        var exp_list:MutableList<expense_details> = mutableListOf<expense_details>()


        if(cursor!!.moveToNext())
        {
            do{
                var temp_expName = cursor.getString(0)
                var temp_expAmt = cursor.getInt(1)


                var temp_exp = expense_details(temp_expName,temp_expAmt)

                exp_list.add(temp_exp)

            }while(cursor.moveToNext())

        }

        return exp_list



    }

}