package com.iamdj.crudsqliteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnInsertData, btnDeleteData, btnUpdateData, btnReadData;
    EditText textId, textName, textSurname, textMarks;


    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);  // created object of DatabaseHelper class
        //myDB.getWritableDatabase(); // for checking db is created or not.

        btnInsertData = findViewById(R.id.btnInsertData);
        btnDeleteData = findViewById(R.id.btnDeleteData);
        btnUpdateData = findViewById(R.id.btnUpdateData);
        btnReadData = findViewById(R.id.btnReadData);

        textId = findViewById(R.id.textId);
        textName = findViewById(R.id.textName);
        textSurname = findViewById(R.id.textSurname);
        textMarks = findViewById(R.id.textMarks);

        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert data into database
                boolean isInserted = myDB.insertData(textName.getText().toString(), textSurname.getText().toString(), textMarks.getText().toString());

                // Show toast when data inserted successfully
                if(isInserted){
                    Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
                }

                // After inserting make edit box empty
                textId.setText("");
                textName.setText("");
                textSurname.setText("");
                textMarks.setText("");
            }
        });


        btnReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cur = myDB.getAllData();

                if(cur.getCount() == 0){
                    showMessage("Error","No Data Found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (cur.moveToNext()){

                    buffer.append("ID: "+ cur.getString(0)+"\n");  // 0 is index here shows columns in database
                    buffer.append("Name: "+ cur.getString(1)+"\n");
                    buffer.append("Surname: "+ cur.getString(2)+"\n");
                    buffer.append("Marks: "+ cur.getString(3)+"\n\n");
                }

                showMessage("Data",buffer.toString());
            }
        });


        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check emptiness of edit boxes
                if(textId.getText().toString().isEmpty() || textName.getText().toString().isEmpty() || textSurname.getText().toString().isEmpty() || textMarks.getText().toString().isEmpty()){
                    showMessage("Error","Please fill the all fields to Updating");
                    return;
                }
                boolean isUpdated = myDB.updateData(textId.getText().toString(), textName.getText().toString(), textSurname.getText().toString(), textMarks.getText().toString());

                if(isUpdated){
                    Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Data Not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer isDeleted  = myDB.deleteData(textId.getText().toString());

                if(isDeleted > 0){
                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    // Method to show database table
    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}