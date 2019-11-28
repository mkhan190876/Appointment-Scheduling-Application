package scheduler.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class DateTime {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final long FIFTEEN_MINUTES = MILLISECONDS.convert(15, MINUTES);
  
  public static boolean inFifteenMinutes(String today, String date) throws ParseException{
    Date startTime = DATE_FORMAT.parse(date);
    Date currentTime = DATE_FORMAT.parse(today);          
    long time = startTime.getTime() - currentTime.getTime();
    return time <= FIFTEEN_MINUTES;
  }
  
  public static String makeDateLocal(String date) throws ParseException{
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    TimeZone utcZone = TimeZone.getTimeZone("UTC");
    simpleDateFormat.setTimeZone(utcZone);
    Date myDate = simpleDateFormat.parse(date);
    simpleDateFormat.setTimeZone(TimeZone.getDefault());
    String formattedDate = simpleDateFormat.format(myDate);
    return formattedDate;
  }
  
  public static String makeDateUTC(String date){
    ZoneId timeZone = ZoneId.systemDefault();
    LocalDateTime dateLocalDateTime = LocalDateTime.parse(date, DATE_FORMATTER);
    ZonedDateTime dateZonedDateTime = dateLocalDateTime.atZone(timeZone);
    ZonedDateTime UTCZonedDateTime = dateZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    LocalDateTime UTCLocalDateTime = UTCZonedDateTime.toLocalDateTime();
    Timestamp UTCTimestamp = Timestamp.valueOf(UTCLocalDateTime);
    return UTCTimestamp.toString();
  }
}
