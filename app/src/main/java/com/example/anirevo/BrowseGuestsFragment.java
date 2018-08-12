package com.example.anirevo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anirevo.model.ArGuest;
import com.example.anirevo.model.GuestManager;

import java.util.List;

public class BrowseGuestsFragment extends Fragment {

    public static final String EXTRA_GUEST_IDX = "cvic.anirevo.EXTRA_GUEST_IDX";

    private GridView gridView;
    private CustomAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_guest, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        gridView = view.findViewById(R.id.grid_view_guest);

        adapter = new CustomAdapter(getContext(), GuestManager.getInstance().getGuests());
        gridView.setAdapter(adapter);
    }

    private class CustomAdapter extends BaseAdapter {

        Context mCtx;
        List<ArGuest> guests;

        CustomAdapter(Context mCtx, List<ArGuest> guests) {
            this.mCtx = mCtx;
            this.guests = guests;
        }

        @Override
        public int getCount() {
            return guests.size();
        }

        @Override
        public Object getItem(int i) {
            return guests.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(mCtx).inflate(R.layout.guest_card_layout, viewGroup,false);
            }

            final ArGuest guest = (ArGuest) getItem(i);

            ImageView img = view.findViewById(R.id.guest_card_portrait);
            TextView name = view.findViewById(R.id.guest_card_name);
            TextView title = view.findViewById(R.id.guest_card_title);

            //
            img.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.placeholder_portrait));
            name.setText(guest.getName());
            title.setText(guest.getTitle());

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //Move to GuestActivity
                    Intent intent = new Intent(getContext(), GuestActivity.class);
                    intent.putExtra(EXTRA_GUEST_IDX, i);
                    startActivity(intent);
                }
            });

            return view;
        }
    }

}
