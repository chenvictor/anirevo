package cvic.anirevo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cvic.anirevo.exceptions.InvalidIdException;
import cvic.anirevo.model.anirevo.ArGuest;
import cvic.anirevo.model.anirevo.GuestManager;

public class GuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        Intent intent = getIntent();
        ArGuest guest = null;
        try {
            guest = GuestManager.getInstance().getGuest(intent.getIntExtra(BrowseGuestsFragment.EXTRA_GUEST_ID, 0));
        } catch (InvalidIdException e) {
            //Make toast
            Toast.makeText(getApplicationContext(), "Invalid Guest ID Provided", Toast.LENGTH_SHORT).show();
            finish();
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
