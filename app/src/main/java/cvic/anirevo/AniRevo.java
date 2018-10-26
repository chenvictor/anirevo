package cvic.anirevo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

import java.lang.ref.WeakReference;

import cvic.anirevo.handlers.NavigationHandler;
import cvic.anirevo.handlers.StorageHandler;
import cvic.anirevo.model.anirevo.AgeRestriction;
import cvic.anirevo.tasks.CheckUpdatesTask;
import cvic.anirevo.utils.TempUtils;

public class AniRevo extends AppCompatActivity implements CheckUpdatesTask.UpdateListener{

    private static final String TAG = "cvic.anirevo.MAIN";

    private NavigationHandler mNavigationHandler;
    private StorageHandler mStorageHandler;
    private TabLayout mAppBarTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ani_revo);

        mAppBarTabs = findViewById(R.id.appbar_tabs);
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
        AgeRestriction.initialize(getResources());
        TempUtils.init(PreferenceManager.getDefaultSharedPreferences(this), getString(R.string.display_13_key), getString(R.string.display_18_key));
    }

    private void initHandlers() {
        mNavigationHandler = new NavigationHandler(this, mAppBarTabs);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case NavigationHandler.AGE_CHANGED_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    boolean shouldReload = data.getBooleanExtra(SettingsActivity.EXTRA_SHOULD_RELOAD, false);
                    if (shouldReload) {
                        reload();
                    }
                }
                break;
            default:
                Log.i(TAG, "Unknown activity result: requestCode " + requestCode);
        }

    }

    private void reload() {
        finish();
        TempUtils.getAgeRestriction(true);
        startActivity(getIntent());
    }

    @Override
    public void updated() {
        reload();
    }
}
