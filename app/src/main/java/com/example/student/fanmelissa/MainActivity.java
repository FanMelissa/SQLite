package com.example.student.fanmelissa;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    EditText et_id, et_name, et_address, et_email;
    Button bt_save, bt_select, bt_update, bt_delete, bt_exit;
    GridView gv_display;
    ArrayAdapter<String> adapter;
    Dialog dialog;
    DBHelper dbHelper;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventClickExit();
    }

    private void eventClickExit() {
        bt_exit = (Button) findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void evenClickSelect() {
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                ArrayList<Author> authorlist = new ArrayList<>();
                String id = et_id.getText().toString();
                if (!id.isEmpty()) {
                    int idkq = Integer.parseInt(id);
                    Author author = dbHelper.getAuthor(idkq);
                    list.add(author.getId() + "");
                    list.add(author.getName());
                    list.add(author.getAddress());
                    list.add(author.getEmail());
                } else {
                    authorlist = dbHelper.getAllAuthor();
                    for (Author a : authorlist) {
                        list.add(a.getId() + "");
                        list.add(a.getName());
                        list.add(a.getAddress());
                        list.add(a.getEmail());
                    }
                }
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                gv_display.setAdapter(adapter);
            }
        });
    }



    private void evenClickSave() {
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Author author = new Author();
                String id = et_id.getText().toString();
                String name = et_name.getText().toString();
                String address = et_address.getText().toString();
                String email = et_email.getText().toString();
                if (isEmpty(id, name, address,email)) {
                    author.setId(Integer.parseInt(et_id.getText().toString()));
                    author.setName(name);
                    author.setAddress(address);
                    author.setEmail(email);
                    if (dbHelper.insertAuthor(author)) {
                        notifyList();
                        clear();
                        Toast.makeText(getApplicationContext(), "Saved Successfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! Save Unsuccessfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter full information !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void evenClickUpdate() {
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id1 = et_id.getText().toString();
                String name = et_name.getText().toString();
                String address = et_address.getText().toString();
                String email = et_email.getText().toString();
                if (isEmpty(id1, name, address,email)) {
                    int id = Integer.parseInt(et_id.getText().toString());
                    boolean isUpdate = dbHelper.updateAuthor(id, name,address,email);
                    if (isUpdate) {
                        notifyList();
                        clear();
                        Toast.makeText(getApplicationContext(), "Updated Successfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! Update Unsuccessfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter full information !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void evenClickDelete() {
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                if (!id.isEmpty()) {
                    int idkq = Integer.parseInt(id);
                    boolean del = dbHelper.deleteAuthor(idkq);
                    if (del) {
                        adapter.notifyDataSetChanged();
                        notifyList();
                        clear();
                        Toast.makeText(getApplicationContext(), "Deleted Successfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! Delete Unsuccessfully.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter ID !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void notifyList() {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Author> authorlist = new ArrayList<>();
        authorlist = dbHelper.getAllAuthor();
        for (Author a : authorlist) {
            list.add(a.getId() + "");
            list.add(a.getName());
            list.add(a.getAddress());
            list.add(a.getEmail());
        }
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
        gv_display.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_author:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showDialog() {
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Thông tin tác giả");
        dialog.setContentView(R.layout.dialog);
        mappingView();
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        evenClickSelect();
        evenClickSave();
        evenClickDelete();
        evenClickUpdate();
    }

    private void clear() {
        et_id.setText("");
        et_name.setText("");
        et_address.setText("");
        et_email.setText("");
        et_id.requestFocus();
    }

    private boolean isEmpty(String id, String name, String address, String email) {
        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || email.isEmpty()) {
            return false;
        }
        return true;
    }

    private void mappingView() {
        //EditText
        et_id = (EditText) dialog.findViewById(R.id.et_ID);
        et_name = (EditText) dialog.findViewById(R.id.et_Name);
        et_address = (EditText) dialog.findViewById(R.id.et_address);
        et_email = (EditText) dialog.findViewById(R.id.et_email);

        //GridView
        gv_display = (GridView) dialog.findViewById(R.id.gv_listItem);

        //DBHelper
        dbHelper = new DBHelper(this);

        //Button
        bt_save = (Button) dialog.findViewById(R.id.bt_save);
        bt_select = (Button) dialog.findViewById(R.id.bt_select);
        bt_delete = (Button) dialog.findViewById(R.id.bt_delete);
        bt_update = (Button) dialog.findViewById(R.id.bt_update);
    }
}
