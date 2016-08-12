package net.razorblade446.android.libertadoresnavigator.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import net.razorblade446.android.libertadoresnavigator.MainScreen;
import net.razorblade446.android.libertadoresnavigator.NavigatorApplication;
import net.razorblade446.android.libertadoresnavigator.R;
import net.razorblade446.android.libertadoresnavigator.common.dtos.Schedule;
import net.razorblade446.android.libertadoresnavigator.services.UlisesService;

import java.util.ArrayList;

public class ScheduleScreen extends Fragment {

    private ScheduleCursorAdapter scheduleAdapter;

    public static ScheduleScreen newInstance() {
        return new ScheduleScreen();
    }

    public ScheduleScreen() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.schedule_screen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myScheduleView = inflater.inflate(R.layout.fragment_schedule, container, false);

        ListView scheduleListView = (ListView) myScheduleView.findViewById(R.id.scheduleList);
        scheduleAdapter = new ScheduleCursorAdapter(getContext(), new ArrayList<Schedule>());
        scheduleListView.setAdapter(scheduleAdapter);

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(UlisesService.ACTION_GET_SCHEDULE_DONE);
        ScheduleReceiver scheduleReceiver = new ScheduleReceiver();
        getActivity().registerReceiver(scheduleReceiver, iFilter);

        Intent testIntent = new Intent(getActivity(), UlisesService.class);
        testIntent.putExtra("login", "fypenas");
        testIntent.putExtra("password", "charisaur");
        getActivity().startService(testIntent);

        return myScheduleView;
    }

    public class ScheduleReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UlisesService.ACTION_GET_SCHEDULE_DONE)) {
                ArrayList<Schedule> schedules = intent.getParcelableArrayListExtra("schedule");
                scheduleAdapter.updateData(schedules);
                Log.i(NavigatorApplication.TAG, "BroadcastDone");
            }
        }
    }

    public class ScheduleCursorAdapter extends BaseAdapter {

        private ArrayList scheduleList = new ArrayList();
        private LayoutInflater inflater;
        private Context context;

        public ScheduleCursorAdapter(Context context, ArrayList myList) {
            this.scheduleList = myList;
            this.context = context;
            this.inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return scheduleList.size();
        }

        @Override
        public Schedule getItem(int i) {
            return (Schedule) scheduleList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScheduleViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_schedule, parent, false);
                mViewHolder = new ScheduleViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ScheduleViewHolder) convertView.getTag();
            }

            Schedule currentData = getItem(position);
            mViewHolder.course.setText(currentData.course);
            mViewHolder.classRoom.setText(currentData.classroom);
            mViewHolder.scheduledTime.setText(getString(R.string.schedule_day_time, currentData.day, currentData.time));
            mViewHolder.location.setText(currentData.location);

            return convertView;
        }

        public void updateData(ArrayList<Schedule> newScheduleList) {
            this.scheduleList = newScheduleList;
            notifyDataSetChanged();
        }
    }

    class ScheduleViewHolder {
        public TextView course, classRoom, scheduledTime, location;

        public ScheduleViewHolder(View item) {
            course = (TextView) item.findViewById(R.id.course);
            classRoom = (TextView) item.findViewById(R.id.classroom);
            scheduledTime = (TextView) item.findViewById(R.id.scheduledTime);
            location = (TextView) item.findViewById(R.id.location);
        }
    }
}
