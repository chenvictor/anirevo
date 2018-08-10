package com.example.anirevo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anirevo.model.ArGuest;
import com.example.anirevo.model.GuestManager;

public class ActivityGuest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        Intent intent = getIntent();
        ArGuest guest = GuestManager.getInstance().getGuest(intent.getIntExtra(BrowseGuestsFragment.EXTRA_GUEST_IDX, 0));

        ImageView img = findViewById(R.id.guest_portrait);
        TextView name = findViewById(R.id.guest_name);
        TextView japanese = findViewById(R.id.guest_japanese_name);

        img.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_portrait));
        name.setText(guest.getName());
        if (guest.hasJapanese()) {
            japanese.setText(guest.getJapanese());
            japanese.setVisibility(View.VISIBLE);
        } else {
            japanese.setVisibility(View.GONE);
        }
    }
}
