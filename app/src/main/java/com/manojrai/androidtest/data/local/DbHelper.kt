package com.manojrai.androidtest.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.util.Log
import com.manojrai.androidtest.App
import com.manojrai.androidtest.data.model.Book
import io.reactivex.Single

class DbHelper : SQLiteOpenHelper(App.getInstance().getAppContext(), "test.db", null, 1) {

    private val CREATE_TABLE_BOOK = "create table table_book (" +
            "id integer primary key autoincrement, " +
            "book_name text, " +
            "author_name text, " +
            "price text, " +
            "doi text)"

    private val CREATE_TABLE_AUTHOR = "create table table_author (" +
            "id integer primary key autoincrement, " +
            "author_name text)"

    private val CREATE_TABLE_IMAGES = "create table table_images (" +
            "id integer primary key autoincrement, " +
            "book_id integer, " +
            "image text)"

    private var database: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_TABLE_BOOK)
        db.execSQL(CREATE_TABLE_AUTHOR)
        db.execSQL(CREATE_TABLE_IMAGES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun isExists(): Boolean {
        var exists = false
        database.rawQuery("select * from table_author limit 1", null).apply {
            if (moveToFirst()) {
                exists = true
            }
            close()
        }
        return exists
    }

    fun insertAuthor() {
        for (i in 1..4) {
            ContentValues().apply {
                put("author_name", "Author $i")
                database.insert("table_author", null, this)
            }
        }
    }

    fun getAuthor(): ArrayList<String> {
        val list = ArrayList<String>()
        database.rawQuery("select * from table_author", null).apply {
            while (this.moveToNext()) {
                list.add(this.getString(this.getColumnIndex("author_name")))
            }
            this.close()
        }
        return list
    }

    fun getBooks(): Single<ArrayList<Book>> {
        val bookList = ArrayList<Book>()
        val cursor = database.rawQuery("select * from table_book order by id desc", null)
        while (cursor.moveToNext()) {
            val bookId = cursor.getInt(cursor.getColumnIndex("id"))
            val imgCursor =
                database.rawQuery(
                    "select * from table_images where book_id = ?",
                    arrayOf(bookId.toString())
                )
            val uriList = ArrayList<Uri>()
            while (imgCursor.moveToNext()) {
                uriList.add(Uri.parse(imgCursor.getString(imgCursor.getColumnIndex("image"))))
            }
            imgCursor.close()
            bookList.add(Book(
                bookId,
                cursor.getString(cursor.getColumnIndex("book_name")),
                cursor.getString(cursor.getColumnIndex("author_name")),
                cursor.getString(cursor.getColumnIndex("price")),
                cursor.getString(cursor.getColumnIndex("doi")),
            ).apply {
                list = uriList
            })
        }
        cursor.close()
        return Single.just(bookList)
    }

    fun insertData(book: Book): Single<Long> =
        ContentValues().run {
            put("book_name", book.booName)
            put("author_name", book.authorName)
            put("price", book.price)
            put("doi", book.doi)
            Single.just(database.insert("table_book", null, this))
        }

    fun insertImageUri(bookId: Long, uri: String) =
        ContentValues().apply {
            put("book_id", bookId)
            put("image", uri)
            database.insert("table_images", null, this)
        }
}