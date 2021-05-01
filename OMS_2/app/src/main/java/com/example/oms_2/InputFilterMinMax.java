package com.example.oms_2;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * This class sets range of values (from a minimum to a maximum)
 * for any edittext of input type number.
 * Reference: https://acomputerengineer.com/2015/12/16/limit-number-range-in-edittext-in-android-using-inputfilter/
 */
public class InputFilterMinMax implements InputFilter {

    private int min, max;

    //constructors
    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
