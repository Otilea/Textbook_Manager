package com.example.textbookmanager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.media.Image
import android.provider.ContactsContract
import android.view.*
import android.widget.*
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    var listBooks = ArrayList<Book>()

    var mSharedPref : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSharedPref = this.getSharedPreferences("My_Data", Context.MODE_PRIVATE)

        val mSorting = mSharedPref!!.getString("Sort", "newest")
        when(mSorting)
        {
            "newest" -> LoadQueryNewest("%")
            "oldest" -> LoadQueryOldest("%")
            "ascending" -> LoadQueryAscending("%")
            "descending" -> LoadQueryDescending("%")
        }

    }

    override fun onResume() {
        super.onResume()

        val mSorting = mSharedPref!!.getString("Sort", "newest")
        when(mSorting)
        {
            "newest" -> LoadQueryNewest("%")
            "oldest" -> LoadQueryOldest("%")
            "ascending" -> LoadQueryAscending("%")
            "descending" -> LoadQueryDescending("%")
        }
    }

    @SuppressLint("Range")
    private fun LoadQueryAscending(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)

        //sort by title
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listBooks.clear()

        //ascending
        if (cursor.moveToFirst()) {
            do {
                val ISBN = cursor.getInt(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listBooks.add(Book(ISBN, Title, Author, Course))

            } while (cursor.moveToNext())
        }

        //adapter
        var myBooksAdapter = MyBooksAdapter(this, listBooks)
        //set adapter
        var lvBooks: ListView = findViewById(R.id.lvBooks)
        lvBooks.adapter = myBooksAdapter

        val total = lvBooks.count
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total Text book(s) in this list..."
        }
    }

    @SuppressLint("Range")
    private fun LoadQueryDescending(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)
        //sort by title
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listBooks.clear()
        //descending
        if (cursor.moveToLast()) {
            do {
                val ISBN = cursor.getInt(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listBooks.add(Book(ISBN, Title, Author, Course))

            } while (cursor.moveToPrevious())
        }

        //adapter
        var myBooksAdapter = MyBooksAdapter(this, listBooks)
        //set adapter
        var lvBooks: ListView = findViewById(R.id.lvBooks)
        lvBooks.adapter = myBooksAdapter

        val total = lvBooks.count
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total Text book(s) in this list..."
        }
    }


    @SuppressLint("Range")
    private fun LoadQueryNewest(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)
        //sort by ID
        val cursor = dbManager.Query(projections, "ISBN like ?", selectionArgs, "ISBN")
        listBooks.clear()

        //Newest first(the record will be entered at the bottom of previous records and has larger ID then previous records)

        if (cursor.moveToLast()) {
            do {
                val ISBN = cursor.getInt(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listBooks.add(Book(ISBN, Title, Author, Course))

            } while (cursor.moveToPrevious())
        }


        var myBooksAdapter = MyBooksAdapter(this, listBooks)

        var lvBooks: ListView = findViewById(R.id.lvBooks)
        lvBooks.adapter = myBooksAdapter

        val total = lvBooks.count
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            mActionBar.subtitle = "You have $total Text book(s) in this list..."
        }
    }

    @SuppressLint("Range")
    private fun LoadQueryOldest(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "ISBN like ?", selectionArgs, "ISBN")
        listBooks.clear()

        //oldest first(the record will be entered at the bottom of previous records and has larger ID then previous records, so lesser the ID is the oldest the record is)

        if (cursor.moveToFirst()) {
            do {
                val ISBN = cursor.getInt(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listBooks.add(Book(ISBN, Title, Author, Course))

            } while (cursor.moveToNext())
        }

        var myBooksAdapter = MyBooksAdapter(this, listBooks)
        var lvBooks: ListView = findViewById(R.id.lvBooks)
        lvBooks.adapter = myBooksAdapter

        val total = lvBooks.count
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            mActionBar.subtitle = "You have $total Text book(s) in this list..."
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                LoadQueryAscending("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LoadQueryAscending("%"+newText+"%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null)
        {
            when(item.itemId)
            {
                R.id.addBook ->
                {
                    startActivity(Intent(this, AddBook::class.java))
                }
                R.id.action_sort ->
                {
                    showSortDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showSortDialog() {
        val sortOption = arrayOf("Newest", "Older", "Title(Ascending)", "Title(Descending)")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Sort by")
        mBuilder.setIcon(R.drawable.sort_icon)
        mBuilder.setSingleChoiceItems(sortOption, -1)
        {
                dialogInterface, i ->
            if (i==0)
            {
                //newest first
                Toast.makeText(this,"Newest",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "newest")
                editor.apply()
                LoadQueryNewest("%")

            }
            if (i==1)
            {
                //older first
                Toast.makeText(this,"Oldest",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "oldest")
                editor.apply()
                LoadQueryOldest("%")
            }
            if (i==2)
            {
                //title ascending
                Toast.makeText(this,"Title(Ascending)",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "ascending")
                editor.apply()
                LoadQueryAscending("%")
            }
            if (i==3)
            {
                //title descending
                Toast.makeText(this,"Title(Descending)",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "Descending")
                editor.apply()
                LoadQueryDescending("%")
            }
            dialogInterface.dismiss()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }

    inner class MyBooksAdapter : BaseAdapter {
        var listBooksAdapter = ArrayList<Book>()
        var context: Context? = null

        constructor(context: Context, listBooksAdapter: ArrayList<Book>) : super() {
            this.listBooksAdapter = listBooksAdapter
            this.context = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            var myView = layoutInflater.inflate(R.layout.row, null)
            val myBook = listBooksAdapter[position]

            val tvTitle: TextView = myView.findViewById(R.id.tvTitle)
            val tvAuthor: TextView = myView.findViewById(R.id.tvAuthor)
            val tvCourse: TextView = myView.findViewById(R.id.tvCourse)
            val ibDelete: ImageButton = myView.findViewById(R.id.ibDelete)
            val ibEdit: ImageButton = myView.findViewById(R.id.ibEdit)
            val ibCopy: ImageButton = myView.findViewById(R.id.ibCopy)


            tvTitle.text = myBook.bookName
            tvAuthor.text = myBook.bookAuthor
            tvCourse.text = myBook.bookCourse
            //delete btn click
            ibDelete.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myBook.bookISBN.toString())
                dbManager.delete("ISBN=?", selectionArgs)
                LoadQueryAscending("%")
            }
            //edit,update button click
            ibEdit.setOnClickListener {
                GoToUpdateFun(myBook)
            }
            //copy btn
            ibCopy.setOnClickListener {

                val title = tvTitle.text.toString()
                //val author = tvAuthor.text.toString()
                val course = tvCourse.text.toString()

                val s = title + "\n" + course
                val cb = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s
                Toast.makeText(this@MainActivity, "Copied...", Toast.LENGTH_SHORT).show()
            }

            return myView
        }


        override fun getItem(position: Int): Any {
            return listBooksAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listBooksAdapter.size
        }

    }

    private fun GoToUpdateFun(myBook: Book) {
        val intent = Intent(this, AddBook::class.java)
        intent.putExtra("ISBN", myBook.bookISBN)
        intent.putExtra("name", myBook.bookName)
        intent.putExtra("author", myBook.bookAuthor)
        intent.putExtra("course", myBook.bookCourse)
        startActivity(intent)
    }
}



