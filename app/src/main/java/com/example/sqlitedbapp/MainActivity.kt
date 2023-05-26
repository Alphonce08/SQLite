package com.example.sqlitedbapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.DialogTitle

class MainActivity : AppCompatActivity() {
    lateinit var edtName:EditText
    lateinit var edtEmail:EditText
    lateinit var edtIdnumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mbtname)
        edtEmail = findViewById(R.id.mbtemail)
        edtIdnumber = findViewById(R.id.mbtnum)
        btnSave = findViewById(R.id.btnsave)
        btnView = findViewById(R.id.btnview)
        btnDelete = findViewById(R.id.btndelete)
        //create a database
        db = openOrCreateDatabase("emobilisdb", Context.MODE_PRIVATE, null)
        //create a table inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(name VARCHAR, EMAIL VARCHAR, id_number VARCHAR)")

        btnSave.setOnClickListener {
           //Receive data from the users
            var name = edtName.text.toString()
            var email = edtEmail.text.toString()
            var idNumber = edtIdnumber.text.toString()
            //check if the user is submitting empty fields
            if (name.isEmpty()){
                edtName.setError("Please fill this input")
                edtName.requestFocus()
            } else if (email.isEmpty()) {
                edtEmail.setError("Please fill this input")
                edtEmail.requestFocus()
            } else if (idNumber.isEmpty()){
                edtIdnumber.setError("Please fill this input")
                edtIdnumber.requestFocus()
            } else {
                //proceed to save the data
                db.execSQL("INSERT INTO users VALUES($name,$email,$idNumber)")
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                edtName.setText(null)
                edtEmail.setText(null)
                edtIdnumber.setText(null)
            }
        }

        btnView.setOnClickListener {
            //use cursor to select all the users
            var cursor = db.rawQuery("SELECT * FROM users", null)
            //check if there's any record in the db
            if (cursor.count == 0) {
                displayUsers("NO RECORDS", "Sorry, no data")
            } else {

                //use string buffer to append records from the db
                var buffer = StringBuffer()
                while (cursor.moveToNext()) {
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n")
                }
                displayUsers("USERS", buffer.toString())

            }
        }
        btnDelete.setOnClickListener {
            //Receive the id of the user to be delete
            var idNumber = edtIdnumber.text.toString()
            //check if the idNumber received is empty
            if (idNumber.isEmpty()){
                edtIdnumber.setError("Please fill this input")
                edtIdnumber.requestFocus()

            } else{
                //proceed to delete
                //use cursor to select the users with id
                var cursor = db.rawQuery("SELECT * FROM users WHERE id_number=$idNumber", null)
                //check if the user with the provided id exits
                if (cursor.count ==0){
                    displayUsers("NO USERS", "Sorry, no data")
                } else {
                    //Delete the user
                    db.execSQL("DELETE FROM users WHERE id_number=$idNumber")
                    displayUsers("SUCCESS", "Users deleted!")
                }
            }
        }

    }
    fun displayUsers(title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("close", null)
        alertDialog.create().show()

    }


}