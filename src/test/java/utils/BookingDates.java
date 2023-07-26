package utils;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.sql.Date.valueOf;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDates {
    private Date checkin;
    private Date checkout;
    public static Date generateDate (String date){
        LocalDate localDate = LocalDate
                .parse(date,
                        DateTimeFormatter
                                .ofPattern("yyyy-MM-dd"));
        return valueOf(localDate);
    }

}
