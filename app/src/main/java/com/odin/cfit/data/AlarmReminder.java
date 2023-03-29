package com.odin.cfit.data;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Model class for a reminder which triggers an alarm, as set by the user of the application.
 */
@Entity
public class AlarmReminder implements Parcelable {

    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public Date time;
    public Boolean repeat;
    public Integer repeatNo;
    public RepeatType repeatType;
    public Boolean active;

    public String getId() {
        return id;
    }

    public AlarmReminder setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AlarmReminder setTitle(String title) {
        this.title = title;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public AlarmReminder setTime(Date time) {
        this.time = time;
        return this;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public AlarmReminder setRepeat(Boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public Integer getRepeatNo() {
        return repeatNo;
    }

    public AlarmReminder setRepeatNo(Integer repeatNo) {
        this.repeatNo = repeatNo;
        return this;
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public AlarmReminder setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public AlarmReminder setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Enum type representing how often this reminder is triggered for repeating alarms.
     */
    public enum RepeatType {
        NONE,
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH;

        /**
         * Parses the name of a {@link RepeatType} into its corresponding enum value. If none is found,
         * the default of <code>NONE</code> is returned.
         * @param name the name of a {@link RepeatType} to find
         * @return a {@link RepeatType} with the corresponding name
         */
        public static RepeatType parse(String name) {
            switch (name) {
                case "minute": return MINUTE;
                case "hour": return HOUR;
                case "day": return DAY;
                case "week": return WEEK;
                case "month": return MONTH;
            }
            return NONE;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeLong(this.time != null ? this.time.getTime() : -1);
        dest.writeValue(this.repeat);
        dest.writeValue(this.repeatNo);
        dest.writeInt(this.repeatType == null ? -1 : this.repeatType.ordinal());
        dest.writeValue(this.active);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        long tmpTime = source.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.repeat = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.repeatNo = (Integer) source.readValue(Integer.class.getClassLoader());
        int tmpRepeatType = source.readInt();
        this.repeatType = tmpRepeatType == -1 ? null : RepeatType.values()[tmpRepeatType];
        this.active = (Boolean) source.readValue(Boolean.class.getClassLoader());
    }

    public AlarmReminder() {
    }

    protected AlarmReminder(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.repeat = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.repeatNo = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpRepeatType = in.readInt();
        this.repeatType = tmpRepeatType == -1 ? null : RepeatType.values()[tmpRepeatType];
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<AlarmReminder> CREATOR = new Parcelable.Creator<AlarmReminder>() {
        @Override
        public AlarmReminder createFromParcel(Parcel source) {
            return new AlarmReminder(source);
        }

        @Override
        public AlarmReminder[] newArray(int size) {
            return new AlarmReminder[size];
        }
    };
}
