package com.jakomulski.datacollector;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jakomulski.datacollector.helpers.Language;
import com.jakomulski.datacollector.models.User;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.jakomulski.datacollector.UsersActivity.DB_DATA_SOURCE;

public class AddUserActivity extends AppCompatActivity {
    public static final String USER_ID_EXTRA = "USER_ID_EXTRA";

    private EditText name;
    private EditText lastname;
    private EditText birthdate;
    private ImageButton forwardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_user_title);
        name = (EditText) findViewById(R.id.editTextName);
        lastname = (EditText) findViewById(R.id.editTextLastName);
        birthdate = (EditText) findViewById(R.id.birthdate);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener((e)->onBackPressed());

        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener((e)-> onAddUser());

        openDatePickerDialog();
    }

    private void onAddUser() {
        User user = DB_DATA_SOURCE.createUser(name.getText().toString(),lastname.getText().toString(),birthdate.getText().toString());
        onBackPressed();
    }

    private void openDatePickerDialog() {
        final DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day)-> {
            birthdate.setText(String.format("%s-%s-%s", year, month + 1, day));
            Calendar calendar = new GregorianCalendar();
            calendar.set(year, month, day);
        }, 1995, 1, 1);

        birthdate.setOnFocusChangeListener((view, state)-> showDialogIfTrue(dialog, state));
        birthdate.setOnClickListener((view)->dialog.show());
    }

    private void showDialogIfTrue(DatePickerDialog dialog, boolean isTrue){
        if(isTrue) {
            dialog.show();
        }
    }
}
