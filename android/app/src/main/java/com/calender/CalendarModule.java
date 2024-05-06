package com.calender;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import androidx.core.content.ContextCompat;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarModule extends ReactContextBaseJavaModule {

    private final Context context;

    public CalendarModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "CalendarModule";
    }

    @ReactMethod
    public void addEvent(String title, String location, String startDate, String endDate, Promise promise) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            promise.reject("PERMISSION_REQUIRED", "Write calendar permission is required");
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.TITLE, title);
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, location);

        // Convert start and end dates to milliseconds
        long startMillis = getMillisFromString(startDate);
        long endMillis = getMillisFromString(endDate);

        contentValues.put(CalendarContract.Events.DTSTART, startMillis);
        contentValues.put(CalendarContract.Events.DTEND, endMillis);
        contentValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);
        if (uri != null) {
            promise.resolve(uri.toString());
        } else {
            promise.reject("EVENT_CREATION_FAILED", "Failed to create event");
        }
    }

    // Helper method to convert date string to milliseconds
    private long getMillisFromString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if parsing fails
        }
    }
}