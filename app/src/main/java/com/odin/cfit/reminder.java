package com.odin.cfit;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.odin.cfit.adapter.AlarmCursorAdapter;
import com.odin.cfit.data.AlarmReminderContract;
import com.odin.cfit.data.AlarmReminderDbHelper;

/*import android.support.v4.app.Fragment;*/


public class reminder extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

private FloatingActionButton mAddReminderButton;
AlarmCursorAdapter mCursorAdapter;
AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(getActivity());
ListView reminderListView;
ProgressDialog prgDialog;
TextView reminderText;
private static final int VEHICLE_LOADER = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("CFit Reminder");



        mAddReminderButton = view.findViewById(R.id.fab_add);
        reminderListView = view.findViewById(R.id.list);
        View emptyView = view.findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        reminderText = view.findViewById(R.id.no_reminder_text);

        mCursorAdapter = new AlarmCursorAdapter(getActivity(), null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, i);

                //set the uri on the data field of the intent
                intent.setData(currentVehicleUri);
                startActivity(intent);
            }
        });

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Reminder", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), AddReminderActivity.class);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminder, container, false);
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
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE
        };


        return new CursorLoader(getActivity(), //parent fragment/activity
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, //provider content uri to query
                projection, //columns to include in the resulting cursor
                null, //no selection clause
                null, //no selection arguments
                null //default sort order
                );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        if (data.getCount() > 0){
            reminderText.setVisibility(View.VISIBLE);
        }else {
            reminderText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
