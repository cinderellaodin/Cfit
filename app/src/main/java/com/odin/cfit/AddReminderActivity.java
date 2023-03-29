package com.odin.cfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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
import com.odin.cfit.data.AlarmReminder;
import com.odin.cfit.data.ApplicationDatabase;
import com.odin.cfit.reminder.AlarmReminderScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.schedulers.Schedulers;
import kotlinx.coroutines.Dispatchers;

public class AddReminderActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

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
    private Date mDate;
    private Boolean mRepeat;
    private Integer mRepeatNo;
    private AlarmReminder.RepeatType mRepeatType;
    private Boolean mActive;

    private AlarmReminder mAlarmReminder;
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

    private CompositeDisposable disposables = new CompositeDisposable();


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
        mAlarmReminder = intent.getParcelableExtra("REMINDER");
        if (mAlarmReminder == null) {
            setTitle("Add Reminder");

            //Invalidate the options menu, so the "Delete" menu option can be hidden.
            //It doesn't make sense to delete a reminder that hasn't been created yet.
            invalidateOptionsMenu();
        }else{
            setTitle("Edit Reminder");
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
        if (mAlarmReminder != null) {
            mTitle = mAlarmReminder.getTitle();
            mActive = mAlarmReminder.getActive();
            mRepeat = mAlarmReminder.getRepeat();
            mRepeatNo = mAlarmReminder.getRepeatNo();
            mRepeatType = mAlarmReminder.getRepeatType();

            mCalendar = Calendar.getInstance();
            mCalendar.setTime(mAlarmReminder.getTime());
            mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            mMinute = mCalendar.get(Calendar.MINUTE);
            mYear = mCalendar.get(Calendar.YEAR);
            mMonth = mCalendar.get(Calendar.MONTH) + 1;
            mDay = mCalendar.get(Calendar.DATE);
            mDate = mCalendar.getTime();
        } else {
            mActive = true;
            mRepeat = true;
            mRepeatNo = 1;
            mRepeatType = AlarmReminder.RepeatType.HOUR;

            mCalendar = Calendar.getInstance();
            mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            mMinute = mCalendar.get(Calendar.MINUTE);
            mYear = mCalendar.get(Calendar.YEAR);
            mMonth = mCalendar.get(Calendar.MONTH) + 1;
            mDay = mCalendar.get(Calendar.DATE);

            mDate = mCalendar.getTime();
        }

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
        mTitleText.setText(mTitle);
        mDateText.setText(SimpleDateFormat.getDateInstance().format(mDate));
        mTimeText.setText(SimpleDateFormat.getTimeInstance().format(mDate));
        mReapeatNoText.setText(mRepeatNo.toString());
        mRepeatTextType.setText(mRepeatType.toString().toLowerCase());
        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType.toString().toLowerCase() + "(s)");

        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
//                mDate = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
//                mDate = savedDate;

//                String savedRepeat = savedInstanceState.getString(KEY_REPEAT);
//                mRepeatText.setText(savedRepeat);
//                mRepeat = savedRepeat;
//
//                String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
//                mReapeatNoText.setText(savedRepeatNo);
//                mRepeatNo = savedRepeatNo;
//
//                String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
//                mRepeatTextType.setText(savedRepeatType);
//                mRepeatType = savedRepeatType;
//
//                mActive = savedInstanceState.getString(KEY_ACTIVE);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        mHour = hourOfDay;
        mMinute = minute;
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mDate = calendar.getTime();
        mTimeText.setText(SimpleDateFormat.getTimeInstance().format(mDate));
    }

    //obtian date from date picker
    @Override
    public void onDateSet(DatePicker datePicker, int year, int montOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, montOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mDay = dayOfMonth;
        mYear = year;
        mDate = calendar.getTime();
        mDateText.setText(SimpleDateFormat.getDateInstance().format(mDate));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//
//        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
//        outState.putCharSequence(KEY_TIME, mTimeText.getText());
//        outState.putCharSequence(KEY_DATE, mDateText.getText());
//        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
//        outState.putCharSequence(KEY_REPEAT_NO, mReapeatNoText.getText());
//        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTextType.getText());
//        outState.putCharSequence(KEY_ACTIVE, mActive);
    }

    //on clicking the active button
    public void selectFab1(View v) {
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = true;
    }

    //on clicking the inactive button
    public void selectFab2(View v) {
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = false;
    }

    //onclicking the repeat switch
    public void onSwitchRepeat(View view){
        boolean on = ((Switch) view).isChecked();
        if (on){
            mRepeat = true;
            mRepeatText.setText("Every" + mRepeatNo + " " + mRepeatType + "(s)");
        }else {
            mRepeat = false;
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
                switch (item) {
                    case 0:
                        mRepeatType = AlarmReminder.RepeatType.MINUTE;
                        break;
                    case 1:
                        mRepeatType = AlarmReminder.RepeatType.HOUR;
                        break;
                    case 2:
                        mRepeatType = AlarmReminder.RepeatType.DAY;
                        break;
                    case 3:
                        mRepeatType = AlarmReminder.RepeatType.WEEK;
                        break;
                    case 4:
                        mRepeatType = AlarmReminder.RepeatType.MONTH;
                        break;

                }
                mRepeatTextType.setText(mRepeatType.toString().toLowerCase());
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType.toString().toLowerCase() + "(s)");

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //on clicking repeat interval button
    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter number");

        //create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichbutton){

                if (input.getText().toString().length() ==0){
                    mRepeatNo = 1;
                    mReapeatNoText.setText(mRepeatNo.toString());
                    mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType.toString().toLowerCase() + "(s)");
                }
                else {
                    mRepeatNo = Integer.parseInt(input.getText().toString().trim());
                    mReapeatNoText.setText(mRepeatNo.toString());
                    mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType.toString().toLowerCase() + "(s)");
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
        if (mAlarmReminder == null){
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
        if (mAlarmReminder != null){
            //only perform delete if there is an existing reminder
            //pass in null for the selection and selection args because mCurrentreminder url
            //content URI already identifies the reminder that we want
            Disposable disposable = Completable.create(emitter -> {
                        ApplicationDatabase.getInstance(this.getApplicationContext())
                                .alarmReminderDao()
                                .delete(mAlarmReminder);
                        emitter.onComplete();
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> {
                        AlarmReminderScheduler.getInstance().unscheduleAlarmReminder(AddReminderActivity.this.getApplicationContext(), mAlarmReminder);
                        finish();
                    }, throwable -> {
                        throwable.printStackTrace();
                        Toast.makeText(this, "Error deleting reminder", Toast.LENGTH_SHORT).show();
                    });
            disposables.add(disposable);

        }
    }

    //on clicking the save button
    public void saveReminder(){
     /*  if (mCurrentReminderUri == null){
           //since no feilds were modified, we can return early without creating a new reminder
           //no need to create contentvalues and no need to do any contentProvider operations
           return;
       }*/

        AlarmReminder alarmReminder = null;
        if (mAlarmReminder != null) {
            alarmReminder = mAlarmReminder;
        } else {
            alarmReminder = new AlarmReminder();
        }
        alarmReminder.setTitle(mTitle);
        alarmReminder.setTime(mDate);
        alarmReminder.setRepeat(mRepeat);
        alarmReminder.setRepeatNo(mRepeatNo);
        alarmReminder.setRepeatType(mRepeatType);
        alarmReminder.setActive(mActive);

        //set up calendar for creating the notification
        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        if (mAlarmReminder == null){
            //this is a new reminder, so insert a new reminder into the provider
            //returning the content URI for the new reminder
            mAlarmReminder = alarmReminder;
            if (mAlarmReminder.getId() == null || mAlarmReminder.getId().isEmpty()) {
                mAlarmReminder.setId(UUID.randomUUID().toString());
            }
            Disposable disposable = Completable.create(emitter -> {
                        ApplicationDatabase.getInstance(this.getApplicationContext())
                                .alarmReminderDao()
                                .insert(mAlarmReminder);
                        emitter.onComplete();
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Toast.makeText(AddReminderActivity.this, "Reminder saved", Toast.LENGTH_SHORT).show();
                    }, throwable -> {
                        Toast.makeText(AddReminderActivity.this, "Error saving reminder", Toast.LENGTH_SHORT).show();
                    });
            disposables.add(disposable);
        }else {

            if (mAlarmReminder.getId() == null || mAlarmReminder.getId().isEmpty()) {
                mAlarmReminder.setId(UUID.randomUUID().toString());
            }

            Disposable disposable = Completable.create(emitter -> {
                        ApplicationDatabase.getInstance(this.getApplicationContext())
                                .alarmReminderDao()
                                .insert(mAlarmReminder);
                        emitter.onComplete();
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Toast.makeText(AddReminderActivity.this, "Reminder saved", Toast.LENGTH_SHORT).show();
                    }, throwable -> {
                        Toast.makeText(AddReminderActivity.this, "Error saving reminder", Toast.LENGTH_SHORT).show();
                    });
            disposables.add(disposable);

//                    .observe(this, new Observer<Integer>() {
//                        @Override
//                        public void onChanged(Integer integer) {
//                            //show a toast message depending on whether or not the insertion was successful
//                            if (integer > 1) {
//                                //if the new content URI is null, then there was an error with insertion
//                                Toast.makeText(AddReminderActivity.this, "Error with saving reminder", Toast.LENGTH_SHORT).show();
//                            }else {
//                                //otherwise, the insertion was successful and we can display a toast
//                                Toast.makeText(AddReminderActivity.this, "Reminder updated.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
        }
        //create a new notification
        if (mActive){
            if (mRepeat){
                AlarmReminderScheduler.getInstance().setRepeatAlarm(this.getApplicationContext(), mAlarmReminder);
            }else {
                AlarmReminderScheduler.getInstance().setAlarm(this.getApplicationContext(), mAlarmReminder);
            }
            Toast.makeText(this, "Alarm time is" + SimpleDateFormat.getDateTimeInstance().format(mAlarmReminder.getTime()), Toast.LENGTH_LONG).show();

        }
        //create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

    //on pressing the back button


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}