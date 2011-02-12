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
import java.util.List;

import moontime.Constants;
import moontime.Moon;
import moontime.MoonEvent;
import moontime.MoonEventType;
import moontime.MoonPhaseAlgorithm;
import moontime.MoonUtil;

/**
 * Based on John Walker's <a href="http://www.fourmilab.ch/moontoolw/">Moontool</a>.
 */
public class MoonToolPhaseAlgorithm implements MoonPhaseAlgorithm, Constants {

    @Override
    public Moon calculate(Calendar calender) {
        return phase(MoonUtil.toJulian(calender));
    }

    @Override
    public List<MoonEvent> getNextMoonEvents(Calendar upFromDate, int count, EnumSet<MoonEventType> includedTypes) {
        return phasehunt(MoonUtil.toJulian(upFromDate), count, includedTypes);
    }

    static double meanphase(double sdate, double k) {
        double t, t2, t3, nt1;

        /* Time in Julian centuries from 1900 January 0.5 */
        t = (sdate - 2415020.0) / 36525;
        t2 = t * t; /* Square for frequent use */
        t3 = t2 * t; /* Cube for frequent use */

        nt1 = 2415020.75933 + SYNODIC_MONTH * k + 0.0001178 * t2 - 0.000000155 * t3 + 0.00033 * MoonUtil.sinFromDegree(166.56 + 132.87 * t - 0.009173 * t2);
        return nt1;
    }

    /**
     * TRUEPHASE -- Given a K value used to determine the mean phase of the new moon, and a phase
     * selector (0.0, 0.25, 0.5, 0.75), obtain the true, corrected phase time.
     * 
     * @param k
     * @param phase
     * @return phase time
     */
    static double truephase(double k, double phase) {
        double t, t2, t3, pt, m, mprime, f;

        k += phase; /* Add phase to new moon time */
        t = k / 1236.85; /*
                          * Time in Julian centuries from 1900 January 0.5
                          */
        t2 = t * t; /* Square for frequent use */
        t3 = t2 * t; /* Cube for frequent use */
        pt = 2415020.75933 /* Mean time of phase */
                + SYNODIC_MONTH * k + 0.0001178 * t2 - 0.000000155 * t3 + 0.00033 * MoonUtil.sinFromDegree(166.56 + 132.87 * t - 0.009173 * t2);

        m = 359.2242 /* Sun's mean anomaly */
                + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3;
        mprime = 306.0253 /* Moon's mean anomaly */
                + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3;
        f = 21.2964 /* Moon's argument of latitude */
                + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3;
        if ((phase < 0.01) || (Math.abs(phase - 0.5) < 0.01)) {
            /* Corrections for New and Full Moon */
            pt += (0.1734 - 0.000393 * t) * MoonUtil.sinFromDegree(m) + 0.0021 * MoonUtil.sinFromDegree(2 * m) - 0.4068 * MoonUtil.sinFromDegree(mprime) + 0.0161 * MoonUtil.sinFromDegree(2 * mprime)
                    - 0.0004 * MoonUtil.sinFromDegree(3 * mprime) + 0.0104 * MoonUtil.sinFromDegree(2 * f) - 0.0051 * MoonUtil.sinFromDegree(m + mprime) - 0.0074 * MoonUtil.sinFromDegree(m - mprime)
                    + 0.0004 * MoonUtil.sinFromDegree(2 * f + m) - 0.0004 * MoonUtil.sinFromDegree(2 * f - m) - 0.0006 * MoonUtil.sinFromDegree(2 * f + mprime) + 0.0010
                    * MoonUtil.sinFromDegree(2 * f - mprime) + 0.0005 * MoonUtil.sinFromDegree(m + 2 * mprime);
        } else if ((Math.abs(phase - 0.25) < 0.01 || (Math.abs(phase - 0.75) < 0.01))) {
            pt += (0.1721 - 0.0004 * t) * MoonUtil.sinFromDegree(m) + 0.0021 * MoonUtil.sinFromDegree(2 * m) - 0.6280 * MoonUtil.sinFromDegree(mprime) + 0.0089 * MoonUtil.sinFromDegree(2 * mprime)
                    - 0.0004 * MoonUtil.sinFromDegree(3 * mprime) + 0.0079 * MoonUtil.sinFromDegree(2 * f) - 0.0119 * MoonUtil.sinFromDegree(m + mprime) - 0.0047 * MoonUtil.sinFromDegree(m - mprime)
                    + 0.0003 * MoonUtil.sinFromDegree(2 * f + m) - 0.0004 * MoonUtil.sinFromDegree(2 * f - m) - 0.0006 * MoonUtil.sinFromDegree(2 * f + mprime) + 0.0021
                    * MoonUtil.sinFromDegree(2 * f - mprime) + 0.0003 * MoonUtil.sinFromDegree(m + 2 * mprime) + 0.0004 * MoonUtil.sinFromDegree(m - 2 * mprime) - 0.0003
                    * MoonUtil.sinFromDegree(2 * m + mprime);
            if (phase < 0.5) {
                /* First quarter correction */
                pt += 0.0028 - 0.0004 * MoonUtil.cosFromDegree(m) + 0.0003 * MoonUtil.cosFromDegree(mprime);
            } else {
                /* Last quarter correction */
                pt += -0.0028 + 0.0004 * MoonUtil.cosFromDegree(m) - 0.0003 * MoonUtil.cosFromDegree(mprime);
            }
        }
        return pt;
    }

    /**
     * PHASEHUNT -- Find time of phases of the moon which surround the current date. Five phases are
     * found, starting and ending with the new moons which bound the current lunation.
     * 
     * @param startDate
     * @param count
     * @param includedTypes
     * @return found events
     */
    @SuppressWarnings("deprecation")
    static List<MoonEvent> phasehunt(double startDate, int count, EnumSet<MoonEventType> includedTypes) {
        double adate, k1, k2, nt1, nt2;
        adate = startDate - 45;

        Date date = MoonUtil.toGregorianDate(adate);
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        k1 = Math.floor((year + (month * (1.0 / 12.0)) - 1900) * 12.3685);

        adate = nt1 = meanphase(adate, k1);
        while (true) {
            adate += SYNODIC_MONTH;
            k2 = k1 + 1;
            nt2 = meanphase(adate, k2);
            if (nt1 <= startDate && nt2 > startDate) {
                break;
            }
            nt1 = nt2;
            k1 = k2;
        }

        return getEventsFrom(k1, includedTypes, count, startDate);
    }

    private static List<MoonEvent> getEventsFrom(double currentMonthDate, EnumSet<MoonEventType> includedTypes, int targetCount, double startJDate) {
        List<MoonEvent> events = new ArrayList<MoonEvent>(targetCount);
        while (true) {
            for (MoonEventType moonEventType : includedTypes) {
                double truephase = truephase(currentMonthDate, moonEventType.getFraction());
                if (truephase >= startJDate) {
                    events.add(new MoonEvent(moonEventType, MoonUtil.toGregorian(truephase).getTime()));
                    if (events.size() == targetCount) {
                        return events;
                    }
                }
            }
            currentMonthDate++;
        }
    }

    public static double fixangle(double a) {
        return ((a) - 360.0 * (Math.floor((a) / 360.0)));
    }

    /**
     * PHASE -- Calculate phase of moon as a fraction:
     * 
     * The argument is the time for which the phase is requested, expressed as a Julian date and
     * fraction. Returns the terminator phase angle as a percentage of a full circle (i.e., 0 to 1),
     * and stores into pointer arguments the illuminated fraction of the Moon's disc, the Moon's age
     * in days and fraction, the distance of the Moon from the center of the Earth, and the angular
     * diameter subtended by the Moon as seen by an observer at the center of the Earth.
     * 
     * @param pdate
     * @return
     */
    public static Moon phase(double pdate) {
        double dayOfEpoch = pdate - EPOCH;
        double meanSunAnomaly = fixangle((360 / 365.2422) * dayOfEpoch);

        // SUN
        // Convert from perigee coordinates to epoch 1980.0
        double M = fixangle(meanSunAnomaly + SUN_ECLIPTIC_LONGITUDE_AT_EPOCH - SUN_ECLIPTIC_LONGITUDE_AT_EPOCH_AT_PERIGREE);

        double Ec = MoonUtil.solveKeplerEquation(M, ECCENTRICITY);
        Ec = Math.sqrt((1 + ECCENTRICITY) / (1 - ECCENTRICITY)) * Math.tan(Ec / 2);
        Ec = 2 * Math.toDegrees(Math.atan(Ec)); /* True anomaly */

        double sunGeocentricEclipticLongitude = fixangle(Ec + SUN_ECLIPTIC_LONGITUDE_AT_EPOCH_AT_PERIGREE);

        // MOON
        double meanMoonLongitude = fixangle(13.1763966 * dayOfEpoch + MOONS_MEAN_LONGITUDE);
        double meanMoonAnomaly = fixangle(meanMoonLongitude - 0.1114041 * dayOfEpoch - MOONS_MEAN_LONGITUDE_AT_EPOCH_AT_PERIGREE);

        /* Evection */
        double Ev = 1.2739 * MoonUtil.sinFromDegree(2 * (meanMoonLongitude - sunGeocentricEclipticLongitude) - meanMoonAnomaly);

        double annualEquation = 0.1858 * MoonUtil.sinFromDegree(M);
        /* Correction term */
        double A3 = 0.37 * MoonUtil.sinFromDegree(M);
        /* Corrected anomaly */
        double MmP = meanMoonAnomaly + Ev - annualEquation - A3;
        /* Correction for the equation of the center */
        double mEc = 6.2886 * MoonUtil.sinFromDegree(MmP);
        /* Another correction term */
        double A4 = 0.214 * MoonUtil.sinFromDegree(2 * MmP);

        /* Corrected longitude */
        double lP = meanMoonLongitude + Ev + mEc - annualEquation + A4;
        /* Variation */
        double V = 0.6583 * MoonUtil.sinFromDegree(2 * (lP - sunGeocentricEclipticLongitude));
        /* True longitude */
        double lPP = lP + V;

        double MoonAgeInDegrees = lPP - sunGeocentricEclipticLongitude;

        double moonFraction = fixangle(MoonAgeInDegrees) / 360.0;
        double moonAge = SYNODIC_MONTH * moonFraction;
        return new Moon(moonAge, moonFraction);
    }
}