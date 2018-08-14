package cvic.anirevo.model.anirevo;

import android.graphics.Color;

public enum AgeRestriction {

    AGE_RESTRICTION_13(13, Color.BLUE),
    AGE_RESTRICTION_18(18, Color.RED);

    private int age;
    private int textColor;

    AgeRestriction(int age, int color) {
        this.age = age;
        this.textColor = color;
    }

    @Override
    public String toString() {
        return String.valueOf(age) + "+";
    }

    public int getTextColor() {
        return textColor;
    }

}
