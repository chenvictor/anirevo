package cvic.anirevo;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cvic.anirevo.handlers.NavigationHandler;
import cvic.anirevo.handlers.StorageHandler;
import cvic.anirevo.model.calendar.CalendarEvent;
import cvic.anirevo.tasks.CheckUpdatesTask;
import cvic.anirevo.ui.SettingsFragment;
import cvic.anirevo.utils.TempUtils;

public class AniRevo extends AppCompatActivity implements SettingsFragment.SettingsFragmentInteractionListener, CheckUpdatesTask.UpdateListener{

    private static final String TAG = "cvic.anirevo.MAIN";

    private NavigationHandler mNavigationHandler;
    private StorageHandler mStorageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani_revo);

        init();

        initHandlers();

        mStorageHandler.loadJSON();

        mNavigationHandler.start();

        //check wifi
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.startup_update_check_key), true)) {
            new CheckUpdatesTask(new WeakReference<Context>(this), this, false).execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStorageHandler.saveJSON();
    }

    private void init() {
        CalendarEvent.setDefaultColor(getResources().getColor(R.color.calendarEventDefault));
        TempUtils.init(PreferenceManager.getDefaultSharedPreferences(this), getString(R.string.display_13_key), getString(R.string.display_18_key));
    }

    private void initHandlers() {
        mNavigationHandler = new NavigationHandler(this);
        mStorageHandler = new StorageHandler(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mNavigationHandler.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!mNavigationHandler.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void reload() {
        Toast.makeText(this, "Reloading Activity", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void checkUpdates() {
        new CheckUpdatesTask(new WeakReference<Context>(this), this, true).execute();
    }

    @Override
    public void updated() {
        reload();
    }
}
