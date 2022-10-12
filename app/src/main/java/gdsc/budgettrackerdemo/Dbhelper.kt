package gdsc.budgettrackerdemo

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.math.exp

class Dbhelper(context: Context,factory: SQLiteDatabase.CursorFactory?):SQLiteOpenHelper(context,
    DATABASR_NAME,factory,
    DATABASE_VERSION
) {

    companion object{
        var DATABASR_NAME:String = "GDSC"
        var DATABASE_VERSION:Int =1
    }

    override fun onCreate(db: SQLiteDatabase) {

        val create_query:String = ("CREATE TABLE EXPENSE_TABLE (EXP_NAME TEXT , EXP_AMT INTEGER)")
        db.execSQL(create_query)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insert_expense(expName:String,exp_Amt:Int)
    {
        var insert_query:String = ("INSERT INTO EXPENSE_TABLE VALUES('$expName',$exp_Amt) ")
        var db = this.writableDatabase
        db.execSQL(insert_query)
//        db.close()
    }

    fun show_expense():MutableList<expense_details>
    {
        var exp_list:MutableList<expense_details> = mutableListOf<expense_details>()

        val show_query:String = ("SELECT * FROM EXPENSE_TABLE")
        var db = this.readableDatabase
        var cursor:Cursor = db.rawQuery(show_query,null)

        if(cursor!!.moveToNext())
        {
            do {
                var exp_name:String = cursor.getString(0).toString()
                var exp_Amt:Int = cursor.getInt(1)

                var temp = expense_details(exp_name,exp_Amt)

                Log.d("DATABASE_UPDATE",exp_name+exp_Amt)

                exp_list.add(temp)
            }while(cursor.moveToNext())
        }

        return exp_list
    }


    fun delete_expense(exp_name : String)
    {
        val delete_query = ("DELETE FROM EXPENSE_TABLE WHERE EXP_NAME = '$exp_name'")

        var db = this.writableDatabase
        db.execSQL(delete_query)
    }

    fun total_expense_amt():Int
    {
        var total_expense_query:String = ("SELECT EXP_AMT FROM EXPENSE_TABLE")
        val db = this.writableDatabase

        var cursor: Cursor = db.rawQuery(total_expense_query,null)

        var total_amt: Int = 0

        if(cursor!!.moveToNext())
        {
            do {
                var amt :Int = cursor.getInt(0)

                total_amt = total_amt + amt

            }while(cursor.moveToNext())
        }

        return total_amt;
    }


}