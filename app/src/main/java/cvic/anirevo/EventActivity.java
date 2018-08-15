package cvic.anirevo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cvic.anirevo.exceptions.InvalidIdException;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.model.anirevo.ArEvent;
import cvic.anirevo.model.anirevo.EventManager;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        ArEvent event = null;
        try {
            event = EventManager.getInstance().getEvent(intent.getIntExtra(EventsFragment.EXTRA_EVENT_ID, 0));
        } catch (InvalidIdException e) {
            //Make toast
            Toast.makeText(getApplicationContext(), "Invalid Event ID Provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        TextView warn = findViewById(R.id.event_activity_warning_18);
        TextView age = findViewById(R.id.event_activity_age_restriction);
        TextView title = findViewById(R.id.event_activity_title);
        TextView time = findViewById(R.id.event_activity_time);
        TextView loc = findViewById(R.id.event_activity_location);
        TextView desc = findViewById(R.id.event_activity_description);

        title.setText(event.getTitle());
        time.setText(event.getDetails()); //TODO: modify this
        loc.setText(event.getLocation().getPurpose());
        desc.setText(event.getDesc());

        age.setVisibility(View.GONE);
        warn.setVisibility(View.GONE);
        if (event.isAgeRestricted()) {
            age.setVisibility(View.VISIBLE);
            age.setText(event.getRestriction().toString());
            age.setTextColor(event.getRestriction().getTextColor());
            if (event.getRestriction() == AgeRestriction.AGE_RESTRICTION_18) {
                warn.setVisibility(View.VISIBLE);
            }
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
