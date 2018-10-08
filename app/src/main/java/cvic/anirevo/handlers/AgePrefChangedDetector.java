package cvic.anirevo.handlers;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;

public class AgePrefChangedDetector implements Preference.OnPreferenceChangeListener{

    private String key13, key18;
    private boolean initial13, initial18;
    private boolean new13, new18;

    public AgePrefChangedDetector() {

    }

    public void setPref13(Preference pref) {
        boolean value = PreferenceManager.getDefaultSharedPreferences(pref.getContext()).getBoolean(pref.getKey(), true);
        initial13 = value;
        new13 = value;
        key13 = pref.getKey();
        pref.setOnPreferenceChangeListener(this);
    }

    public void setPref18(Preference pref) {
        boolean value = PreferenceManager.getDefaultSharedPreferences(pref.getContext()).getBoolean(pref.getKey(), true);
        initial18 = value;
        new18 = value;
        key18 = pref.getKey();
        pref.setOnPreferenceChangeListener(this);
    }

    /**
     * Detects if age restriction has changed
     * @return  true if restriction has changed, false otherwise
     */
    public boolean isChanged() {
        if (!initial13 && !new13) {
            return false;
        }
        if (initial13 && new13) {
            return initial18 != new18;
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (o instanceof Boolean) {
            if (preference.getKey().equals(key13)) {
                new13 = (Boolean) o;
                return true;
            } else if (preference.getKey().equals(key18)) {
                new18 = (Boolean) o;
                return true;
            }
        }
        return false;
    }
}
