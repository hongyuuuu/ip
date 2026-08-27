package cookie.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/** Represents a date, a time, or a date and time supplied by the user. */
public class DateTimeValue {

    /** Formats a date when displaying a date-time value. */
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Formats a time when displaying a date-time value. */
    private static final DateTimeFormatter TIME_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    /** Stores the date component, if one was supplied. */
    private final LocalDate date;

    /** Stores the time component, if one was supplied. */
    private final LocalTime time;

    /** Creates a value with at least one date or time component.
     *
     * @param date The date component, or {@code null} for a time-only value.
     * @param time The time component, or {@code null} for a date-only value.
     */
    public DateTimeValue(LocalDate date, LocalTime time) {
        if (date == null && time == null) {
            throw new IllegalArgumentException("A date or time is required.");
        }
        this.date = date;
        this.time = time;
    }

    /** Returns the date component, or {@code null} when this is time-only.
     *
     * @return The date component, or {@code null}.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /** Returns the time component, or {@code null} when this is date-only.
     *
     * @return The time component, or {@code null}.
     */
    public LocalTime getTime() {
        return this.time;
    }

    /** Returns the canonical representation used in the saved task file.
     *
     * @return The storage representation of this value.
     */
    public String toStorageString() {
        if (date != null && time != null) {
            return LocalDateTime.of(date, time).toString();
        }
        return date != null ? date.toString() : time.toString();
    }

    /** Returns the readable representation shown in the console.
     *
     * @return The display representation of this value.
     */
    public String toDisplayString() {
        if (date != null && time != null) {
            return date.format(DATE_DISPLAY_FORMAT) + ", " + time.format(TIME_DISPLAY_FORMAT);
        }
        return date != null
                ? date.format(DATE_DISPLAY_FORMAT)
                : time.format(TIME_DISPLAY_FORMAT);
    }

    /** Parses a canonical value loaded from the saved task file.
     *
     * @param value The canonical date, time, or date-time text to parse.
     * @return The parsed date, time, or date-time value.
     * @throws DateTimeParseException If the value does not use a supported format.
     */
    public static DateTimeValue parseStorageValue(String value) throws DateTimeParseException {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(value);
            return new DateTimeValue(dateTime.toLocalDate(), dateTime.toLocalTime());
        } catch (DateTimeParseException exception) {
            try {
                return new DateTimeValue(LocalDate.parse(value), null);
            } catch (DateTimeParseException dateException) {
                return new DateTimeValue(null, LocalTime.parse(value));
            }
        }
    }
}
