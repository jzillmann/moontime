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
package moontime.alg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;

import moontime.Constants;
import moontime.Moon;
import moontime.MoonEvent;
import moontime.MoonEventType;
import moontime.MoonPhaseAlgorithm;

/**
 * Based on John Walker's <a href="http://www.fourmilab.ch/moontoolw/">Moontool</a>.
 */
public class MoonToolPhaseAlgorithm implements MoonPhaseAlgorithm, Constants {

    @Override
    public Moon calculate(Date date) {
        double julianFromUnix = getJulianFromUnix(date.getTime() / 1000);
        return phase(julianFromUnix);
    }

    @Override
    public List<MoonEvent> getNextMoonEvents(Date upFromDate, int count, EnumSet<MoonEventType> includedTypes) {
        return phasehunt(getJulianFromUnix(upFromDate.getTime() / 1000), count, includedTypes);
    }

    public static double getJulianFromUnix(long unixSecs) {
        return (unixSecs / 86400.0) + 2440587;
    }

    public static double sinFromDegree(double x) {
        return Math.sin(Math.toRadians((x)));
    }

    public static double cosFromDegree(double x) {
        return Math.cos(Math.toRadians((x)));
    }

    static Date getAsDate(double jTime) {
        YearMonthDay yearMonthDay = new YearMonthDay();
        jyear(jTime, yearMonthDay);
        Date date = new Date();
        date.setYear(yearMonthDay.year - 1900);
        date.setMonth(yearMonthDay.month - 1);
        date.setDate(yearMonthDay.day);
        jhms(jTime, date);
        return date;
    }

    static/* JHMS -- Convert Julian time to hour, minutes, and seconds. */
    void jhms(double j, Date date) {
        long ij;
        j += 0.5; /* Astronomical to civil */
        ij = (long) (((j - Math.floor(j)) * 86400.0) + 0.5); // Round to nearest second
        int h = (int) (ij / 3600L);
        int m = (int) ((ij / 60L) % 60L);
        int s = (int) (ij % 60L);
        date.setHours(h);
        date.setMinutes(m);
        date.setSeconds(s);
    }

    // / Convert internal date and time to astronomical Julian
    // time (i.e. Julian date plus day fraction, expressed as
    // a double).
    public static double jtime(Date t) {
        int c;
        Calendar cal = new GregorianCalendar();
        cal.setTime(t);
        c = -cal.get(Calendar.ZONE_OFFSET); // !!! should this be negative?
        return (jdate(t) - 0.5) + (cal.get(Calendar.SECOND) + 60 * (cal.get(Calendar.MINUTE) + c + 60 * cal.get(Calendar.HOUR_OF_DAY))) / 86400.0;
    }

    // jdate - convert internal GMT date to Julian day.
    private static long jdate(Date t) {
        long c, m, y;

        Calendar cal = new GregorianCalendar();
        cal.setTime(t);
        y = cal.get(Calendar.YEAR) + 1900;
        m = cal.get(Calendar.MONTH) + 1;
        if (m > 2)
            m = m - 3;
        else {
            m = m + 9;
            --y;
        }
        c = y / 100L; // compute century
        y -= 100L * c;
        return cal.get(Calendar.DATE) + (c * 146097L) / 4 + (y * 1461L) / 4 + (m * 153L + 2) / 5 + 1721119L;
    }

    static double meanphase(double sdate, double k) {
        double t, t2, t3, nt1;

        /* Time in Julian centuries from 1900 January 0.5 */
        t = (sdate - 2415020.0) / 36525;
        t2 = t * t; /* Square for frequent use */
        t3 = t2 * t; /* Cube for frequent use */

        nt1 = 2415020.75933 + SYNODIC_MONTH * k + 0.0001178 * t2 - 0.000000155 * t3 + 0.00033 * sinFromDegree(166.56 + 132.87 * t - 0.009173 * t2);

        return nt1;
    }

    /*
     * TRUEPHASE -- Given a K value used to determine the mean phase of the new moon, and a phase
     * selector (0.0, 0.25, 0.5, 0.75), obtain the true, corrected phase time.
     */

    static double truephase(double k, double phase) {
        double t, t2, t3, pt, m, mprime, f;
        boolean apcor = false;

        k += phase; /* Add phase to new moon time */
        t = k / 1236.85; /*
                          * Time in Julian centuries from 1900 January 0.5
                          */
        t2 = t * t; /* Square for frequent use */
        t3 = t2 * t; /* Cube for frequent use */
        pt = 2415020.75933 /* Mean time of phase */
                + SYNODIC_MONTH * k + 0.0001178 * t2 - 0.000000155 * t3 + 0.00033 * sinFromDegree(166.56 + 132.87 * t - 0.009173 * t2);

        m = 359.2242 /* Sun's mean anomaly */
                + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3;
        mprime = 306.0253 /* Moon's mean anomaly */
                + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3;
        f = 21.2964 /* Moon's argument of latitude */
                + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3;
        if ((phase < 0.01) || (Math.abs(phase - 0.5) < 0.01)) {

            /* Corrections for New and Full Moon */

            pt += (0.1734 - 0.000393 * t) * sinFromDegree(m) + 0.0021 * sinFromDegree(2 * m) - 0.4068 * sinFromDegree(mprime) + 0.0161 * sinFromDegree(2 * mprime) - 0.0004 * sinFromDegree(3 * mprime)
                    + 0.0104 * sinFromDegree(2 * f) - 0.0051 * sinFromDegree(m + mprime) - 0.0074 * sinFromDegree(m - mprime) + 0.0004 * sinFromDegree(2 * f + m) - 0.0004 * sinFromDegree(2 * f - m)
                    - 0.0006 * sinFromDegree(2 * f + mprime) + 0.0010 * sinFromDegree(2 * f - mprime) + 0.0005 * sinFromDegree(m + 2 * mprime);
            apcor = true;
        } else if ((Math.abs(phase - 0.25) < 0.01 || (Math.abs(phase - 0.75) < 0.01))) {
            pt += (0.1721 - 0.0004 * t) * sinFromDegree(m) + 0.0021 * sinFromDegree(2 * m) - 0.6280 * sinFromDegree(mprime) + 0.0089 * sinFromDegree(2 * mprime) - 0.0004 * sinFromDegree(3 * mprime)
                    + 0.0079 * sinFromDegree(2 * f) - 0.0119 * sinFromDegree(m + mprime) - 0.0047 * sinFromDegree(m - mprime) + 0.0003 * sinFromDegree(2 * f + m) - 0.0004 * sinFromDegree(2 * f - m)
                    - 0.0006 * sinFromDegree(2 * f + mprime) + 0.0021 * sinFromDegree(2 * f - mprime) + 0.0003 * sinFromDegree(m + 2 * mprime) + 0.0004 * sinFromDegree(m - 2 * mprime) - 0.0003
                    * sinFromDegree(2 * m + mprime);
            if (phase < 0.5)
                /* First quarter correction */
                pt += 0.0028 - 0.0004 * cosFromDegree(m) + 0.0003 * cosFromDegree(mprime);
            else
                /* Last quarter correction */
                pt += -0.0028 + 0.0004 * cosFromDegree(m) - 0.0003 * cosFromDegree(mprime);
            apcor = true;
        }
        if (!apcor) {
            System.err.println("MoonToolPhaseAlgorithm.truephase()");
            // MessageBox(hWndMain, rstring(IDS_ERR_TRUEPHASE), rstring(IDS_ERR_IERR),
            // MB_ICONEXCLAMATION | MB_OK | MB_APPLMODAL);
        }
        return pt;
    }

    /*
     * PHASEHUNT -- Find time of phases of the moon which surround the current date. Five phases are
     * found, starting and ending with the new moons which bound the current lunation.
     */

    static List<MoonEvent> phasehunt(double sdate, int count, EnumSet<MoonEventType> includedTypes) {
        double adate, k1, k2, nt1, nt2;
        adate = sdate - 45;

        YearMonthDay yearMonthDay = new YearMonthDay();
        jyear(adate, yearMonthDay);
        k1 = Math.floor((yearMonthDay.year + ((yearMonthDay.month - 1) * (1.0 / 12.0)) - 1900) * 12.3685);

        adate = nt1 = meanphase(adate, k1);
        while (true) {
            adate += SYNODIC_MONTH;
            k2 = k1 + 1;
            nt2 = meanphase(adate, k2);
            if (nt1 <= sdate && nt2 > sdate) {
                break;
            }
            nt1 = nt2;
            k1 = k2;
        }

        return getEventsFrom(k1, includedTypes, count, sdate);
    }

    private static List<MoonEvent> getEventsFrom(double currentMonthDate, EnumSet<MoonEventType> includedTypes, int targetCount, double startJDate) {
        List<MoonEvent> events = new ArrayList<MoonEvent>(targetCount);
        while (true) {
            for (MoonEventType moonEventType : includedTypes) {
                double truephase = truephase(currentMonthDate, moonEventType.getFraction());
                if (truephase >= startJDate) {
                    events.add(new MoonEvent(moonEventType, getAsDate(truephase)));
                    if (events.size() == targetCount) {
                        return events;
                    }
                }
            }
            currentMonthDate++;
        }
    }

    /*
     * JYEAR -- Convert Julian date to year, month, day, which are returned via integer pointers to
     * integers (note that year is a long).
     */
    static void jyear(double td, YearMonthDay yearMonthDay) {
        double z, f, a, alpha, b, c, d, e;

        td += 0.5;
        z = Math.floor(td);
        f = td - z;

        if (z < 2299161.0) {
            a = z;
        } else {
            alpha = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1 + alpha - Math.floor(alpha / 4);
        }

        b = a + 1524;
        c = Math.floor((b - 122.1) / 365.25);
        d = Math.floor(365.25 * c);
        e = Math.floor((b - d) / 30.6001);

        yearMonthDay.day = (int) (b - d - Math.floor(30.6001 * e) + f);
        yearMonthDay.month = (int) ((e < 14) ? (e - 1) : (e - 13));
        yearMonthDay.year = (int) ((yearMonthDay.month > 2) ? (c - 4716) : (c - 4715));
    }

    public static class YearMonthDay {
        int year;
        int month;
        int day;
    }

    public static double fixangle(double a) {
        return ((a) - 360.0 * (Math.floor((a) / 360.0)));
    }

    /* KEPLER -- Solve the equation of Kepler. */

    static double solveKeplerEquation(double m, double ecc) {
        double e, delta;
        double EPSILON = 1E-6;
        e = m = Math.toRadians(m);
        do {
            delta = e - ecc * Math.sin(e) - m;
            e -= delta / (1 - ecc * Math.cos(e));
        } while (Math.abs(delta) > EPSILON);
        return e;
    }

    /*
     * PHASE -- Calculate phase of moon as a fraction:
     * 
     * The argument is the time for which the phase is requested, expressed as a Julian date and
     * fraction. Returns the terminator phase angle as a percentage of a full circle (i.e., 0 to 1),
     * and stores into pointer arguments the illuminated fraction of the Moon's disc, the Moon's age
     * in days and fraction, the distance of the Moon from the center of the Earth, and the angular
     * diameter subtended by the Moon as seen by an observer at the center of the Earth.
     */
    public static Moon phase(double pdate) {
        double dayOfEpoch = pdate - EPOCH;
        double meanSunAnomaly = fixangle((360 / 365.2422) * dayOfEpoch);

        // SUN
        // Convert from perigee coordinates to epoch 1980.0
        double M = fixangle(meanSunAnomaly + SUN_ECLIPTIC_LONGITUDE_AT_EPOCH - SUN_ECLIPTIC_LONGITUDE_AT_EPOCH_AT_PERIGREE);

        double Ec = solveKeplerEquation(M, ECCENTRICITY);
        Ec = Math.sqrt((1 + ECCENTRICITY) / (1 - ECCENTRICITY)) * Math.tan(Ec / 2);
        Ec = 2 * Math.toDegrees(Math.atan(Ec)); /* True anomaly */

        double sunGeocentricEclipticLongitude = fixangle(Ec + SUN_ECLIPTIC_LONGITUDE_AT_EPOCH_AT_PERIGREE);

        // MOON
        double meanMoonLongitude = fixangle(13.1763966 * dayOfEpoch + MOONS_MEAN_LONGITUDE);
        double meanMoonAnomaly = fixangle(meanMoonLongitude - 0.1114041 * dayOfEpoch - MOONS_MEAN_LONGITUDE_AT_EPOCH_AT_PERIGREE);

        /* Evection */
        double Ev = 1.2739 * Math.sin(Math.toRadians(2 * (meanMoonLongitude - sunGeocentricEclipticLongitude) - meanMoonAnomaly));

        double annualEquation = 0.1858 * Math.sin(Math.toRadians(M));
        /* Correction term */
        double A3 = 0.37 * Math.sin(Math.toRadians(M));
        /* Corrected anomaly */
        double MmP = meanMoonAnomaly + Ev - annualEquation - A3;
        /* Correction for the equation of the center */
        double mEc = 6.2886 * Math.sin(Math.toRadians(MmP));
        /* Another correction term */
        double A4 = 0.214 * Math.sin(Math.toRadians(2 * MmP));

        /* Corrected longitude */
        double lP = meanMoonLongitude + Ev + mEc - annualEquation + A4;
        /* Variation */
        double V = 0.6583 * Math.sin(Math.toRadians(2 * (lP - sunGeocentricEclipticLongitude)));
        /* True longitude */
        double lPP = lP + V;

        double MoonAgeInDegrees = lPP - sunGeocentricEclipticLongitude;
        // double MoonPhase = (1 - Math.cos(torad(MoonAgeInDegrees))) / 2;
        // System.out.println("MJD:" + pdate);
        // System.out.println("Day:" + Day);
        // System.out.println("M:" + M);
        // System.out.println("EC:" + Ec);
        // System.out.println("MoonAgeDegrees: " + MoonAgeInDegrees);
        // System.out.println("MoonPhase: " + MoonPhase);

        double moonFraction = fixangle(MoonAgeInDegrees) / 360.0;
        double moonAge = SYNODIC_MONTH * moonFraction;
        return new Moon(moonAge, moonFraction);
    }

}