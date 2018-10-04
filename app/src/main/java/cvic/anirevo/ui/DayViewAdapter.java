package cvic.anirevo.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cvic.anirevo.R;
import cvic.anirevo.utils.TimeUtils;

public class DayViewAdapter extends RecyclerView.Adapter {

    private int startHour;
    private int endHour;
    private LayoutInflater mInflater;

    DayViewAdapter(LayoutInflater inflater) {
        startHour = 0;
        endHour = 24;
        mInflater = inflater;
    }

    public void setHourBounds(int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException("Start hour must be before end hour");
        }
        startHour = start;
        endHour = end;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.view_calendar_hour, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TextView hourText = viewHolder.itemView.findViewById(R.id.text_hour);
        hourText.setText(TimeUtils.formatHourString(startHour + i));
    }

    @Override
    public int getItemCount() {
        return endHour - startHour + 1;
    }

}
