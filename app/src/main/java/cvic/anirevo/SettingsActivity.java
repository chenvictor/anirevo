package cvic.anirevo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import cvic.anirevo.handlers.AgePrefChangedDetector;
import cvic.anirevo.tasks.CheckUpdatesTask;

public class SettingsActivity extends AppCompatActivity implements CheckUpdatesTask.UpdateListener {

    public static final String EXTRA_SHOULD_RELOAD = "ANIREVO_SHOULD_RELOAD_EXTRA";
    private AgePrefChangedDetector ageRestrictionChangedDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ageRestrictionChangedDetector = new AgePrefChangedDetector();
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    private void checkUpdates() {
        new CheckUpdatesTask(new WeakReference<Context>(this), this, true).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        passBack(ageRestrictionChangedDetector.isChanged());
    }

    @Override
    public void updated() {
        passBack(true);
    }

    /**
     * Finishes the activity, passing back a result based on shouldReload
     * @param shouldReload  value to pass back
     */
    private void passBack(boolean shouldReload) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SHOULD_RELOAD, shouldReload);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SettingsActivity parent;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof SettingsActivity) {
                parent = (SettingsActivity) context;
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            parent = null;
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            PreferenceManager.setDefaultValues(parent.getApplicationContext(), R.xml.preferences, false);

            if (parent != null) {
                parent.ageRestrictionChangedDetector.setPref13(findPreference(getString(R.string.display_13_key)));
                parent.ageRestrictionChangedDetector.setPref18(findPreference(getString(R.string.display_18_key)));
            }

            final Preference checkUpdateBtn = findPreference(getString(R.string.check_updates_key));
            checkUpdateBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    parent.checkUpdates();
                    return true;
                }
            });
        }
    }

}
