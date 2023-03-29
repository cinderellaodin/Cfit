package com.odin.cfit;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.odin.cfit.adapter.AlarmCursorAdapter;
import com.odin.cfit.data.AlarmReminder;
import com.odin.cfit.data.AlarmReminderContract;
import com.odin.cfit.data.AlarmReminderDbHelper;
import com.odin.cfit.data.ApplicationDatabase;

import java.util.List;

/*import android.support.v4.app.Fragment;*/


public class ReminderFragment extends Fragment implements AlarmCursorAdapter.OnItemCLickListener {

private FloatingActionButton mAddReminderButton;
AlarmCursorAdapter mAdapter;
AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(getActivity());
RecyclerView reminderListView;
ProgressDialog prgDialog;
TextView reminderText;
View emptyView;
private static final int VEHICLE_LOADER = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("CFit Reminder");

        mAddReminderButton = view.findViewById(R.id.fab_reminder_add);
        reminderListView = view.findViewById(R.id.list);
        emptyView = view.findViewById(R.id.empty_view);

        reminderText = view.findViewById(R.id.no_reminder_text);

        mAdapter = new AlarmCursorAdapter();
        mAdapter.setOnItemCLickListener(this);
        reminderListView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        reminderListView.setAdapter(mAdapter);

        reminderListView.setAdapter(mAdapter);

        mAddReminderButton.setOnClickListener(view1 -> {
            Toast.makeText(getActivity(), "Reminder", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), AddReminderActivity.class);
            startActivity(intent);
        });
        loadItems();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    private void loadItems() {
        ApplicationDatabase.getInstance(getActivity().getApplicationContext())
                .alarmReminderDao()
                .getAll()
                .observe(getViewLifecycleOwner(), this::onLoadFinished);
    }


    public void onLoadFinished(List<AlarmReminder> reminders) {
        mAdapter.setItems(reminders);
        if (reminders.size() == 0){
            reminderText.setVisibility(View.VISIBLE);
        }else {
            reminderText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(AlarmReminder reminder) {
        Intent intent = new Intent(getActivity(), AddReminderActivity.class);
        intent.putExtra("REMINDER", reminder);
        startActivity(intent);
    }
}
