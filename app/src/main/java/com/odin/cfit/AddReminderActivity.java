package com.odin.cfit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.odin.cfit.data.AlarmReminderContract;
import com.odin.cfit.reminnder.AlarmScheduler;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_VEHICLE_LOADER = 0;

    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mReapeatNoText, mRepeatTextType;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;

    private Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;

    //values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";


    //constant values is milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        if (mCurrentReminderUri == null) {
            setTitle("Add Reminder Details");

            //Invalidate the options menu, so the "Delete" menu option can be hidden.
            //It doesn't make sense to delete a reminder that hasn't been created yet.
            invalidateOptionsMenu();
        }else{
            setTitle("Edit Reminder");

            getSupportLoaderManager().initLoader(EXISTING_VEHICLE_LOADER,
                    null, this );
        }

        //Intitalize Views
        mToolbar = findViewById(R.id.toolbar);
        mTitleText = findViewById(R.id.reminder_title);
        mDateText = findViewById(R.id.set_date);
        mTimeText = findViewById(R.id.set_time);
        mRepeatText = findViewById(R.id.set_repeat);
        mReapeatNoText = findViewById(R.id.set_repeat_no);
        mRepeatTextType = findViewById(R.id.set_repeat_type);
        mRepeatSwitch = findViewById(R.id.repeat_switch);
        mFAB1 = findViewById(R.id.starred1);
        mFAB2 = findViewById(R.id.starred2);

        //intialize default view
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTitle = charSequence.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

            //Setup Textview using reminder values
            mDateText.setText(mDate);
            mTimeText.setText(mTime);
            mReapeatNoText.setText(mRepeatNo);
            mRepeatTextType.setText(mRepeatType);
            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

            if (savedInstanceState != null) {
                String savedTitle = savedInstanceState.getString(KEY_TITLE);
                mTitleText.setText(savedTitle);
                mTitle = savedTitle;

                String savedTime = savedInstanceState.getString(KEY_TIME);
                mTimeText.setText(savedTime);
                mTime = savedTime;

                String savedDate = savedInstanceState.getString(KEY_DATE);
                mDateText.setText(savedDate);
                mDate = savedDate;

                String savedRepeat = savedInstanceState.getString(KEY_REPEAT);
                mRepeatText.setText(savedRepeat);
                mRepeat = savedRepeat;

                String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
                mReapeatNoText.setText(savedRepeatNo);
                mRepeatNo = savedRepeatNo;

                String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
                mRepeatTextType.setText(savedRepeatType);
                mRepeatType = savedRepeatType;

                mActive = savedInstanceState.getString(KEY_ACTIVE);
            }
            // setup up active buttons
        if (mActive.equals("false")){
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);

        }else if (mActive.equals("true")){
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }



    public void setTime(View v){
        Calendar now = Calendar.getInstance();

        TimePickerDialog tpd = new TimePickerDialog(this, this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);
        tpd.show();
    }

    //on clicking Date picker
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(
                this, this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show();
    }

    //obtian time from time picker
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10){
            mTime = mHour + ":" + "0" + mMinute;
        } else{
            mTime = mHour + ":" + mMinute;
        }
        mTimeText.setText(mTime);
    }

    //obtian date from date picker
    @Override
    public void onDateSet(DatePicker datePicker, int year, int montOfYear, int dayOfMonth) {
        montOfYear ++;
        mDay = dayOfMonth;
        mYear = year;
        mDate = dayOfMonth + "/" + montOfYear + "/" + year;
        mDateText.setText(mDate);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mReapeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTextType.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }

    //on clicking the active button
    public void selectFab1(View v) {
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    //on clicking the inactive button
    public void selectFab2(View v) {
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }

    //onclicking the repeat switch
    public void onSwitchRepeat(View view){
        boolean on = ((Switch) view).isChecked();
        if (on){
            mRepeat = "true";
            mRepeatText.setText("Every" + mRepeatNo + " " + mRepeatType + "(s)");
        }else {
            mRepeat = "false";
            mRepeatText.setText("Off");
        }
    }

    // On clicking repeat type button
    public void selectRepeatType(View v) {
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";


        //create list dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                mRepeatType = items[item];
                mRepeatTextType.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
//on clicking repeat interval button
    public void selectRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter number");

        //create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichbutton){

              if (input.getText().toString().length() ==0){
                  mRepeatNo = Integer.toString(1);
                  mReapeatNoText.setText(mRepeatNo);
                  mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
              }
              else {
                  mRepeatNo = input.getText().toString().trim();
                  mReapeatNoText.setText(mRepeatNo);
                  mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
              }
          }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichbutton) {   //do nothing

            //do nothing
                }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu( @NonNull Menu menu) {
         super.onPrepareOptionsMenu(menu);
         //if this is a new reminder, hide the delete menu item
        if (mCurrentReminderUri == null){
            MenuItem menuItem = menu.findItem(R.id.discard_reminder);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //user clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()){
            //respond to a click on the save menu option
            case R.id.save_reminder:

                if (mTitleText.getText().toString().length() == 0){
                    mTitleText.setError("Reminder Title cannot be blank");
                }
                else{
                    saveReminder();
                    finish();
                }

        return true;
        //Respond to a click on the "Delete" menu option
        case R.id.discard_reminder:
            //pop up confirmation dialog for deletion
            showDeleteConfirmationDialog();
            return true;

            case android.R.id.home:
                // If the reminder hasn't changed, continue with navigating up to parent activity
                //which is the {@link mainActivity}
                if (!mVehicleHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                return true;
                }

                //otherwise if there are unsaved changes, setup a dialog to warn the user
                //create a click listener to handle the user confirming that
                //changes should be discarded

                DialogInterface.OnClickListener discardButtonClickListener =
                        (dialogInterface, i) -> {
                            //User clicked "Discard" button, navigate to parent activity.
                            NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                        };
                //show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
        }
        return super .onOptionsItemSelected(item);
    }
//dialogs
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        //crate an AlertDialog.builder and set the message, and click listeners
        //for the positive and negative buttons on the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", ((dialogInterface, i) -> {
            //user clicked the keep editing button, so dismiss the dialog
            //continue editing the reminder

            if (dialogInterface != null){
                dialogInterface.dismiss();
            }
        }));

        //create and show the Alertdialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog(){
        //crate an AlertDialog.builder and set the message, and click listeners
        //for the positive and negative buttons on the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this reminder");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            deleteReminder();
        } );
        builder.setNegativeButton("cancel", (dialog, i) -> {
            //User clicked the cancel button, so dismiss the dialog
            //and continue editing the reminder
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        //create and show the Alertdialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteReminder(){
        if (mCurrentReminderUri != null){
            //only perform delete if there is an existing reminder
            //pass in null for the selection and selection args because mCurrentreminder url
            //content URI already identifies the reminder that we want
            int rowsDeleted = getContentResolver().delete(mCurrentReminderUri, null, null);

            //show a toast message depending on whether or not the delete was successful
            if (rowsDeleted == 0){
                //if no rows were deleted then there was an error with the delete
                Toast.makeText(this, "Error with deleting reminder", Toast.LENGTH_SHORT).show();
            }else {
                //otherwise, the delete was successful and we can display a toast
                Toast.makeText(this, "Reminder deleted", Toast.LENGTH_LONG).show();
            }
        }
        //close the activity
        finish();
    }

    //on clicking the save button
    public void saveReminder(){
     /*  if (mCurrentReminderUri == null){
           //since no feilds were modified, we can return early without creating a new reminder
           //no need to create contentvalues and no need to do any contentProvider operations
           return;
       }*/

        ContentValues values = new ContentValues();

        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);

        //set up calendar for creating the notification
        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp = mCalendar.getTimeInMillis();
        // check repeat type
        if (mRepeatType.equals("Minute")){
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
        }else if (mRepeatType.equals("Hour")){
            mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
        }else if (mRepeatType.equals("Day")){
            mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
        }else if (mRepeatType.equals("Week")){
            mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
        }else if (mRepeatType.equals("Month")){
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
        }

        if (mCurrentReminderUri == null){
            //this is a new reminder, so insert a new reminder into the provider
            //returning the content URI for the new reminder

            Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

            //show a toast message depending on whether or not the insertion was successful
            if (newUri == null){
                //if the new content URI is null, then there was an error with insertion
                Toast.makeText(this, "Error with saving reminder", Toast.LENGTH_SHORT).show();
            }else {
                //otherwise, the insertion was successful and we can display a toast
                Toast.makeText(this, "Reminder saved", Toast.LENGTH_SHORT).show();
            }
        }else {
            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            //show a toast message depending on whether or not the update was successful
            if (rowsAffected == 0){
                //if the new content URI is null, then there was an error with insertion
                Toast.makeText(this, "Error with updating reminder", Toast.LENGTH_SHORT).show();
            }else {
                //otherwise, the update was successful and we can display a toast
                Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();
            }
        }
        //create a new notification
        if (mActive.equals("true")){
            if (mRepeat.equals("true")){
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime );
            }else if (mRepeat.equals(false)){
                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
            }
            Toast.makeText(this, "Alarm time is" + selectedTimestamp, Toast.LENGTH_LONG).show();

        }
        //create toast to confrirm new reminder
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

    }

    //on pressing the back button


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE,
        };
        //This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this, //parent activity context
                mCurrentReminderUri,        //Query the content URI for the current reminder
                projection,                 //columns to include in the resulting cursor
                null,               //no selection clause
                null,           //no selection arguments
                null);             //default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data == null || data.getCount() < 1){
            return;
        }

        //proceed with moving to the first row of the cusrsor and reading data from it
        //this should be the only row in the cursor
        if (data.moveToFirst()){
            int titleColumnIndex = data.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = data.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = data.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
            int repeatColumnIndex = data.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = data.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = data.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = data.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);

            //Extract out the value from the cursor for the given column index
            String title = data.getString(titleColumnIndex);
            String date = data.getString(dateColumnIndex);
            String time = data.getString(timeColumnIndex);
            String repeat = data.getString(repeatColumnIndex);
            String repeatNo = data.getString(repeatNoColumnIndex);
            String repeatType = data.getString(repeatTypeColumnIndex);
            String active = data.getString(activeColumnIndex);

            //update the views on the screen with the values from the database
            mTitleText.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
            mReapeatNoText.setText(repeatNo);
            mRepeatTextType.setText(repeatType);
            mRepeatText.setText("Every " + repeatNo + " " + repeatType + "(s)");
            //setup active buttons
            //setup repeat switch
            if (repeat.equals("false")){
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText("off");
            }else if (repeat.equals("true")){
                mRepeatSwitch.setChecked(true);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}