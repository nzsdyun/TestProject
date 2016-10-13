package com.example.sky.contentproviderdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.sky.contentproviderdemo.com.example.sky.contentproviderdemo.provider.PersonContract;
import com.example.sky.contentproviderdemo.com.example.sky.contentproviderdemo.provider.PersonProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Click(View view) {
        switch (view.getId()) {
            case R.id.add:
                addPerson();
                break;
            case R.id.del:
                deletePerson();
                break;
            case R.id.update:
                updatePerson();
                break;
            case R.id.query:
                queryPerson();
                break;
        }

    }

    private void addPerson() {
        ContentValues values = new ContentValues();
        values.put(PersonContract.Person.NAME, "sky");
        values.put(PersonContract.Person.AGE, (int)(10 + Math.random() * 20));
        getContentResolver().insert(PersonContract.Person.CONTENT_URI, values);
    }

    private void deletePerson() {
        getContentResolver().delete(PersonContract.Person.CONTENT_URI, null, null);
    }

    private void updatePerson() {
        ContentValues values = new ContentValues();
        values.put(PersonContract.Person.NAME, "lm");
        final String selection = PersonContract.Person.AGE + " BETWEEN ? AND ?";
        final String[] selectionArgs = {"10", "20"};
        getContentResolver().update(PersonContract.Person.CONTENT_URI, values, selection, selectionArgs);
    }

    private void queryPerson() {
        final String[] PROJECTION = {
                PersonContract.Person.NAME,
                PersonContract.Person.AGE
        };
        final int NAME_INDEX = 0;
        final int AGE_INDEX = 1;
        final String selection = PersonContract.Person.NAME + " LIKE ?";
        final String[] selectionArgs = {"lm"};
        final String orderBy = PersonContract.Person.AGE + " ASC";
        Cursor cursor = getContentResolver().query(PersonContract.Person.CONTENT_URI,
                PROJECTION, selection, selectionArgs, orderBy);
        StringBuilder sb = new StringBuilder();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(NAME_INDEX);
                int age = cursor.getInt(AGE_INDEX);
                sb.append("name:" + name + ",");
                sb.append("age:" + age);
            }
        }

        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();

    }
}
