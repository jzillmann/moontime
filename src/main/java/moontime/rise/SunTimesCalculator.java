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
package moontime.rise;

import java.util.Calendar;

import moontime.MoonUtil;

public class SunTimesCalculator extends LuminaryDayTimesCalculator {

    public SunTimesCalculator(double longitude, double latitude) {
        super(longitude, latitude);
    }

    @Override
    public SunTimes calculateDayTimes(Calendar calendar) {
        return find_sun_and_twi_events_for_date(calendar);
    }

    public SunTimes find_sun_and_twi_events_for_date(Calendar calendar) {
        DayTimes riseSetTimes = calculateTimes(calendar, MoonUtil.sinFromDegree(SUN_ALTITUDE_UPPER_LIMB_TOUCHING_HORIZON));
        DayTimes twighlightTimes = calculateTimes(calendar, MoonUtil.sinFromDegree(SUN_ALTITUDE_CIVIL_TWIGHLIGHT));

        SunTimes sunTimes = new SunTimes(calendar, riseSetTimes.getRiseTime(), riseSetTimes.getSetTime(), twighlightTimes.getRiseTime(), twighlightTimes.getSetTime());
        sunTimes.setAlwaysUp(riseSetTimes.isAlwaysUp());
        sunTimes.setAlwaysDown(riseSetTimes.isAlwaysDown());
        return sunTimes;
    }

    @Override
    protected double[] getPosition(double julianCenturiesSinceJ2000) {
        double p2 = 6.283185307, coseps = 0.91748, sineps = 0.39778;
        double[] ephemeris = new double[3];

        double M = p2 * getFraction(0.993133 + 99.997361 * julianCenturiesSinceJ2000);
        double DL = 6893.0 * Math.sin(M) + 72.0 * Math.sin(2 * M);
        double L = p2 * getFraction(0.7859453 + M / p2 + (6191.2 * julianCenturiesSinceJ2000 + DL) / 1296000);
        double SL = Math.sin(L);
        double X = Math.cos(L);
        double Y = coseps * SL;
        double Z = sineps * SL;
        double RHO = Math.sqrt(1 - Z * Z);
        double dec = (360.0 / p2) * Math.atan(Z / RHO);
        double ra = (48.0 / p2) * Math.atan(Y / (X + RHO));
        if (ra < 0) {
            ra += 24;
        }
        ephemeris[1] = dec;
        ephemeris[2] = ra;
        return ephemeris;

    }

    public static class SunTimes extends DayTimes {

        private final DayTime _dawnStart;
        private final DayTime _duskEnd;

        public SunTimes(Calendar day, DayTime riseTime, DayTime setTime, DayTime dawnStart, DayTime duskEnd) {
            super(day, riseTime, setTime);
            _dawnStart = dawnStart;
            _duskEnd = duskEnd;
        }

        public DayTime getDawnStart() {
            return _dawnStart;
        }

        public DayTime getDuskEnd() {
            return _duskEnd;
        }
    }

}
