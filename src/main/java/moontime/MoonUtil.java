/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package moontime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MoonUtil implements Constants {

    public static Calendar newCalender(Date date) {
        return newCalender(date.getTime());
    }

    public static Calendar newCalender(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar;
    }

    public static Calendar newCalender(int year, int month, int day, int hour, int minutes, int seconds, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(year, month, day, hour, minutes, seconds);
        return calendar;
    }

    public static double toJulian(Calendar calendar) {
        return toJulianFromUnixTimestamp(calendar.getTimeInMillis() / 1000);
    }

    public static double toJulianFromUnixTimestamp(long unixSecs) {
        return (unixSecs / SECONDS_PER_DAY) + JULIAN_1_1_1970;
    }

    public static long toUnixTimestampFromJulian(double julianDate) {
        return (long) ((julianDate - JULIAN_1_1_1970) * SECONDS_PER_DAY);
    }

    public static Calendar toGregorian(double julianDate) {
        return newCalender(toUnixTimestampFromJulian(julianDate) * 1000);
    }

    public static Date toGregorianDate(double julianDate) {
        return new Date(toUnixTimestampFromJulian(julianDate) * 1000);
    }

    public static double sinFromDegree(double x) {
        return Math.sin(Math.toRadians((x)));
    }

    public static double cosFromDegree(double x) {
        return Math.cos(Math.toRadians((x)));
    }

    public static double solveKeplerEquation(double m, double ecc) {
        double e, delta;
        double EPSILON = 1E-6;
        e = m = Math.toRadians(m);
        do {
            delta = e - ecc * Math.sin(e) - m;
            e -= delta / (1 - ecc * Math.cos(e));
        } while (Math.abs(delta) > EPSILON);
        return e;
    }
}
