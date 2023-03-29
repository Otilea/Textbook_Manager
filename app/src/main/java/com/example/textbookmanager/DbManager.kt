package com.example.textbookmanager

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {

    var dbName = "MyBooks"

    var dbTable = "Books"

    var colISBN = "ISBN"
    var colTitle = "Title"
    var colAuthor = "Author"
    var colCourse = "Course"


    var dbVersion = 1

    //CREATE TABLE IF NOT EXISTS MyBooks (ISBN INTEGER PRIMARY KEY, title TEXT, Author TEXT, Course TEXT)
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS "+dbTable+" ("+colISBN+" INTEGER PRIMARY KEY,"+ colTitle +" TEXT, "+ colAuthor +" TEXT, "+colCourse+" TEXT)"

    var sqlDB: SQLiteDatabase? = null

    constructor(context: Context)
    {
        var db = DatabaseHelperBooks(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperBooks : SQLiteOpenHelper {
        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "database created...", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table if Exists" + dbTable)
        }
    }

    fun insert(values : ContentValues) : Long
    {
        val ISBN = sqlDB!!.insert(dbTable,"", values)
        return ISBN
    }

    fun Query(projection : Array<String>, selection : String, selectionArgs : Array<String>, sorOrder : String) : Cursor
    {
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sorOrder)
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>) : Int
    {
        val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>) : Int
    {
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }


}