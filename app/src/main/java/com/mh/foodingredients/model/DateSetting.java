package com.mh.foodingredients.model;

import android.app.Activity;
import java.util.Calendar;

public class DateSetting extends Activity {

    public int year, month, day;

    public DateSetting() {

        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);


    }

}
