package com.example.group9_sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
        DatabaseHelper myDb;
        EditText editName, editSurname,editgrade, editTextId;
        Button btnAddData;
        Button btnviewAll;
        Button btnviewUpdate;
        Button btnDelete;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            myDb = new DatabaseHelper(this);

            editName = (EditText)findViewById(R.id.editText_name);
            editSurname = (EditText)findViewById(R.id.editText_surname);
            editgrade = (EditText)findViewById(R.id.editText_grade);
            editTextId = (EditText)findViewById(R.id.editText_id);
            btnAddData = (Button)findViewById(R.id.button_add);
            btnviewAll = (Button)findViewById(R.id.button2);
            btnviewUpdate = (Button)findViewById(R.id.button_update);
            btnDelete = (Button)findViewById(R.id.button_delete);
            AddData();
            viewAll();
            UpdateData();
            DeleteData();
        }
        public void DeleteData(){
            btnDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                            if (deletedRows > 0)
                                Toast.makeText(MainActivity.this, "Data Deleted",Toast.LENGTH_LONG).show();
                            else Toast.makeText(MainActivity.this, "Data not Deleted",Toast.LENGTH_LONG).show();

                        }
                    }
            );
        }
        public void UpdateData(){
            btnviewUpdate.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isUpdate = myDb.updateData(editTextId.getText().toString(), editName.getText().toString(), editSurname.getText().toString(), editgrade.getText().toString());
                            if (isUpdate == true)
                                Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();

                        }

                    }
            );
        }

        public void AddData(){
            btnAddData.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isInserted = myDb.insertData(editName.getText().toString(),
                                    editSurname.getText().toString(),
                                    editgrade.getText().toString() );
                            if (isInserted == true)
                                Toast.makeText(MainActivity.this, "Data Inserted",Toast.LENGTH_LONG).show();
                            else Toast.makeText(MainActivity.this, "Data not Inserted",Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }

        public void viewAll() {
            btnviewAll.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Cursor res = myDb.getAllData();
                            if(res.getCount() == 0){
                                //show message
                                showMessage("Error", "No Data Found" );
                                return;
                            }

                            StringBuffer buffer = new StringBuffer();
                            while (res.moveToNext()) {
                                buffer.append("ID :" + res.getString(0)+"\n");
                                buffer.append("NAME :" + res.getString(1)+"\n");
                                buffer.append("SURNAME :" + res.getString(2)+"\n");
                                buffer.append("GRADE :" + res.getString(3)+"\n");
                            }

                            showMessage("Data", buffer.toString());
                        }
                    }
            );
        }

        public void showMessage(String title, String Message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(Message);
            builder.show();
        }
        public boolean onCreateOptionsMenu (Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();
            return false;
        }

    }