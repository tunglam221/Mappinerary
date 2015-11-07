package com.example.admin.mappinerary;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.example.admin.mappinerary.db.ItemContract;
import com.example.admin.mappinerary.db.ItemDBHelper;

import java.util.ArrayList;
import java.util.Map;

public class Detail extends AppCompatActivity {
    private ItemDBHelper helper;
    CustomAdapter customAdapter;
    ArrayList<String[]> resource;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //TODO: call initiateItemList() only when there is a change in locations
        //initiateItemList();
        updateUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initiateItemList() {
        helper = new ItemDBHelper(Detail.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.delete(ItemContract.TABLE, null, null);

        for (Map.Entry<String, String[]> entry : (new TodoData()).toBringList.entrySet()) {
            for (String item : entry.getValue()) {
                ContentValues values = new ContentValues();
                values.clear();
                values.put(ItemContract.Columns.ITEM, item);
                values.put(ItemContract.Columns.CHECKED, "N");

                sqlDB.insertWithOnConflict(ItemContract.TABLE, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
            }
        }
    }

    private void updateUI() {
        helper = new ItemDBHelper(Detail.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(ItemContract.TABLE,
                new String[]{ItemContract.Columns._ID, ItemContract.Columns.ITEM, ItemContract.Columns.CHECKED},
                null, null, null, null, null);

        cursor.moveToFirst();

        resource = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            String item = cursor.getString(1);
            String checked = cursor.getString(2);
            cursor.moveToNext();

            resource.add(new String[]{item, checked});
        }

        customAdapter = new CustomAdapter(this, resource);

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(customAdapter);
    }

    private void refreshAdapter() {
        helper = new ItemDBHelper(Detail.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(ItemContract.TABLE,
                new String[]{ItemContract.Columns._ID, ItemContract.Columns.ITEM, ItemContract.Columns.CHECKED},
                null, null, null, null, null);

        cursor.moveToFirst();

        resource = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            String item = cursor.getString(1);
            String checked = cursor.getString(2);
            cursor.moveToNext();
            resource.add(new String[]{item, checked});
        }

        customAdapter.updateListView(resource);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a packing item");
                builder.setMessage("What do you want to add to packing list?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item = inputField.getText().toString();

                        ItemDBHelper helper = new ItemDBHelper(Detail.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(ItemContract.Columns.ITEM, item);
                        values.put(ItemContract.Columns.CHECKED, "N");

                        db.insertWithOnConflict(ItemContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });

                builder.setNegativeButton("Cancel",null);

                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    public void onDeleteButtonClick(View view) {
        View v = (View) view.getParent();
        CheckBox itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
        String item = itemCheckBox.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                ItemContract.TABLE,
                ItemContract.Columns.ITEM,
                item);


        helper = new ItemDBHelper(Detail.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();

    }

    public void onCheckboxCheck (View view) {
        View v = (View) view.getParent();
        CheckBox itemCheckBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
        String item = itemCheckBox.getText().toString();

        String checked;
        if (itemCheckBox.isChecked()) {
            itemCheckBox.setTypeface(null, Typeface.BOLD_ITALIC);
            checked = "Y";
        }
        else {
            itemCheckBox.setTypeface(null, Typeface.NORMAL);
            checked = "N";
        }

        ContentValues values = new ContentValues();

        values.clear();
        values.put(ItemContract.Columns.CHECKED, checked);

        String where = String.format("%s = '%s'", ItemContract.Columns.ITEM, item);
        helper = new ItemDBHelper(Detail.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.update(ItemContract.TABLE, values, where, null);
        refreshAdapter();
    }
}

