package com.odin.cfit.data;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class ApplicationTypeConverters {

    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.ENGLISH);

    @TypeConverter
    public Date toDate(String value) {
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @TypeConverter
    public String fromDate(Date date) {
        return formatter.format(date);
    }

    @TypeConverter
    public AlarmReminder.RepeatType toType(String value) {
        return AlarmReminder.RepeatType.parse(value);
    }

    @TypeConverter
    public String fromType(AlarmReminder.RepeatType type) {
        switch (type) {
            case DAY: return "day";
            case HOUR: return "hour";
            case WEEK: return "week";
            case MONTH: return "month";
            case MINUTE: return "minute";
        }
        return "none";
    }

}
