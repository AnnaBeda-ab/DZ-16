package utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBooking {
    /*    {
         "bookingid": 1,
         "booking": {
                 "firstname": "Jim",
                 "lastname": "Brown",
                 "totalprice": 111,
                 "depositpaid": true,
                 "bookingdates": {
                           "checkin": "2018-01-01",
                           "checkout": "2019-01-01"  },
                 "additionalneeds": "Breakfast"
                }
          }
     */
    private int bookingid;
    private CreateBookingBody booking;
}
