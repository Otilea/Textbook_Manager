package com.example.textbookmanager

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddBook : AppCompatActivity() {

    val dbTable = "Books"
    var isbn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val btnAdd: Button = findViewById(R.id.btnAdd)
        val etTitle: EditText = findViewById(R.id.etTitle)
        val etAuthor: EditText = findViewById(R.id.etAuthor)
        val etCourse: EditText = findViewById(R.id.etCourse)

        try {
            val bundle : Bundle? = intent.extras
            isbn = bundle!!.getInt("ISBN", 0)
            if (isbn!=0)
            {
                //update book
                supportActionBar!!.title = "Update Book"
                //change button text
                btnAdd.text = "Update"
                etTitle.setText(bundle.getString("name"))
                etAuthor.setText(bundle.getString("author"))
                etCourse.setText(bundle.getString("course"))
            }
        }catch (ex : Exception){

        }

    }

    fun addFunc(view: View)
    {
        var dbManager = DbManager(this)

        var values = ContentValues()

        val etTitle: EditText = findViewById(R.id.etTitle)
        val etAuthor: EditText = findViewById(R.id.etAuthor)
        val etCourse: EditText = findViewById(R.id.etCourse)

        values.put("Title", etTitle.text.toString())
        values.put("Author", etAuthor.text.toString())
        values.put("Course", etCourse.text.toString())

        if (isbn == 0)
        {
            val ISBN = dbManager.insert(values)
            if (ISBN>0)
            {
                Toast.makeText(this, "Book is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else
            {
                Toast.makeText(this, "Error adding book...", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            var selectionArgs = arrayOf(isbn.toString())
            val ISBN = dbManager.update(values, "ISBN=?", selectionArgs)
            if (ISBN>0)
            {
                Toast.makeText(this, "Book is updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            else
            {
                Toast.makeText(this, "Error updating book...", Toast.LENGTH_SHORT).show()
            }
        }
    }

}