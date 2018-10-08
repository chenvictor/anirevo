package cvic.anirevo.model.anirevo;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cvic.anirevo.R;

public enum AgeRestriction {

    DEFAULT(0),
    AGE_RESTRICTION_13(13),
    AGE_RESTRICTION_18(18);

    private static final int DEFAULT_COLOR = Color.BLACK;
    private static Map<AgeRestriction, Integer> colorMap;

    static {
        colorMap = new HashMap<>();
    }

    private static final String TAG = "anirevo.AgeRes";

    private final int age;

    public static void initialize(Resources resources) {
        colorMap.put(AGE_RESTRICTION_13, resources.getColor(R.color.restriction13plus));
        colorMap.put(AGE_RESTRICTION_18, resources.getColor(R.color.restriction18plus));
    }

    AgeRestriction(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.valueOf(age) + "+";
    }

    public int getColor() {
        if (colorMap.containsKey(this)) {
            return colorMap.get(this);
        }
        return DEFAULT_COLOR;
    }

    /**
     * Compares two age restrictions
     * @param otherRestriction  restriction to compare with
     * @return  true if this restriction age >= other restriction age
     */
    public boolean restricts(AgeRestriction otherRestriction) {
        return otherRestriction != null && this.age < otherRestriction.age;
    }

    public static AgeRestriction getRestriction(int age) {
        switch (age) {
            case 13: return AGE_RESTRICTION_13;
            case 18: return AGE_RESTRICTION_18;
            default:
                Log.i(TAG, "AgeRestriction " + age + " is not valid.");
            case 0:
                return DEFAULT;
        }
    }

}
