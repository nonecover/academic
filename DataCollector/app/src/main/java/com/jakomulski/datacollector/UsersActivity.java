package com.jakomulski.datacollector;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jakomulski.datacollector.db.DbDataSource;
import com.jakomulski.datacollector.helpers.Language;
import com.jakomulski.datacollector.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    public static DbDataSource DB_DATA_SOURCE;
    private ListView usersView;
    private List<User> users = new ArrayList<>();
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        DB_DATA_SOURCE = new DbDataSource(getBaseContext());

        adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, users);
        usersView = (ListView) findViewById(R.id.listView_users);

        usersView.setOnItemClickListener((adapterView, view, i, l) ->
            openPhotoActivity(((ArrayAdapter<User>) adapterView.getAdapter()).getItem(i)));

        usersView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            openRemoveItemDialog(((ArrayAdapter<User>) adapterView.getAdapter()).getItem(i));
            return true;
        });

        findViewById(R.id.addButton).setOnClickListener((e)->openAddUserActivity());
        findViewById(R.id.logsButton).setOnClickListener((e)->openLogsActivity());
        findViewById(R.id.imageButton_pl).setOnClickListener((e)-> Language.POLISH.set(getBaseContext()));
        findViewById(R.id.imageButton_en).setOnClickListener((e)-> Language.ENGLISH.set(getBaseContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<User> dbUsers = DB_DATA_SOURCE.getAllUsers();
        users.clear();
        users.addAll(dbUsers);
        usersView.setAdapter(adapter);
    }

    private void openAddUserActivity() {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    private void openLogsActivity() {
        Intent intent = new Intent(this, LogsActivity.class);
        startActivity(intent);
    }

    private void openPhotoActivity(User user) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(AddUserActivity.USER_ID_EXTRA, user.getId());
        startActivity(intent);
    }

    private void openRemoveItemDialog(User user) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("delete record");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (d, i)->deleteUser(user));
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", (d, i)->{});
        alertDialog.show();
    }

    private void deleteUser(User user){
        DB_DATA_SOURCE.deleteUser(user);
        onResume();
    }

}
