package cvic.anirevo.utils;

import android.content.SharedPreferences;

import cvic.anirevo.model.anirevo.AgeRestriction;

public class TempUtils {

    private static SharedPreferences prefs;
    private static String DISPLAY_13_KEY;
    private static String DISPLAY_18_KEY;

    private static AgeRestriction RESTRICTION;

    public static void init(SharedPreferences prefs, String DISPLAY_13_KEY, String DISPLAY_18_KEY) {
        TempUtils.prefs = prefs;
        TempUtils.DISPLAY_13_KEY = DISPLAY_13_KEY;
        TempUtils.DISPLAY_18_KEY = DISPLAY_18_KEY;
    }

    public static AgeRestriction getAgeRestriction() {
        return getAgeRestriction(false);
    }

    private static AgeRestriction getAgeRestriction(boolean forceRecalculate) {
        if (RESTRICTION == null || forceRecalculate) {
            RESTRICTION = calculateAgeRestriction();
        }
        return RESTRICTION;
    }

    private static AgeRestriction calculateAgeRestriction() {
        AgeRestriction restriction = null;
        if (prefs.getBoolean(DISPLAY_13_KEY, true)) {
            restriction = AgeRestriction.AGE_RESTRICTION_13;
            if (prefs.getBoolean(DISPLAY_18_KEY, false)) {
                restriction = AgeRestriction.AGE_RESTRICTION_18;
            }
        }
        return restriction;
    }

    private static final String LAST_UPDATED_DATE_KEY = "cvic.anirevo.last_updated_key";

    public static void wipeLastUpdate() {
        prefs.edit().remove(LAST_UPDATED_DATE_KEY).apply();
    }

}
