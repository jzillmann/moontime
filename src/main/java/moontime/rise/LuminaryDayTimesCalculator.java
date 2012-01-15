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

import moontime.Constants;
import moontime.MoonUtil;

public abstract class LuminaryDayTimesCalculator implements Constants {

    protected final double _longitude;
    protected final double _latitude;

    public LuminaryDayTimesCalculator(double longitude, double latitude) {
        _longitude = longitude;
        _latitude = latitude;
    }

    public abstract DayTimes calculateDayTimes(Calendar calendar);

    protected DayTimes calculateTimes(Calendar calendar, double sinho) {
        double mjd = MoonUtil.toModifiedJulian(calendar);
        return calculateTimes(calendar, mjd, sinho);
    }

    private DayTimes calculateTimes(Calendar calendar, double mjd, double sinho) {
        double hour = 1.0, utRise = 0, utSet = 0;
        boolean rise = false, set = false;
        double cosLatitude = MoonUtil.cosFromDegree(_latitude);
        double sinLatitude = MoonUtil.sinFromDegree(_latitude);

        double yMinus = getSineAltitude(mjd, hour - 1.0, cosLatitude, sinLatitude) - sinho;
        double originalYMinus = yMinus;

        while (hour < 25 && (set == false || rise == false)) {
            double yThis = getSineAltitude(mjd, hour, cosLatitude, sinLatitude) - sinho;
            double yPlus = getSineAltitude(mjd, hour + 1.0, cosLatitude, sinLatitude) - sinho;
            double[] quadout = getQuadraticInterpolation(yMinus, yThis, yPlus);
            int numberOfRoots = (int) quadout[0];
            double root1 = quadout[1];
            double root2 = quadout[2];
            double ye = quadout[3];

            switch (numberOfRoots) {
            case 1:
                if (yMinus < 0.0) {
                    utRise = hour + root1;
                    rise = true;
                } else {
                    utSet = hour + root1;
                    set = true;
                }
                break;
            case 2:
                if (ye < 0.0) {
                    utRise = hour + root2;
                    utSet = hour + root1;
                } else {
                    utRise = hour + root1;
                    utSet = hour + root2;
                }
                break;
            }

            yMinus = yPlus;// next interval
            hour += 2.0;
        }

        DayTime riseTime = null;
        DayTime setTime = null;
        if (rise == true) {
            riseTime = toDayTime(utRise);
        }
        if (set == true) {
            setTime = toDayTime(utSet);
        }

        DayTimes dayTimes = new DayTimes(calendar, riseTime, setTime);
        if (rise == false && set == false) {
            if (originalYMinus > 0.0) {
                dayTimes.setAlwaysUp(true);
            } else {
                dayTimes.setAlwaysDown(true);
            }
        }
        return dayTimes;
    }

    private DayTime toDayTime(double hours) {
        double hrs, h, m;
        hrs = Math.floor(hours * 60 + 0.5) / 60.0;
        h = Math.floor(hrs);
        m = Math.floor(60 * (hrs - h) + 0.5);
        return new DayTime((int) h, (int) m);
    }

    private double getSineAltitude(double mjdMidnight, double hour, double cosLatitude, double sinLatitude) {
        double mjd = mjdMidnight + hour / 24.0;
        double utDaysSinceJ2000 = mjd - 51544.5d;
        double julianCenturiesSinceJ2000 = utDaysSinceJ2000 / 36525d;
        double[] ra_dec = getPosition(julianCenturiesSinceJ2000);
        double tau = 15.0 * (getLocalMeanSideRealTime(mjd) - ra_dec[2]);
        return sinLatitude * MoonUtil.sinFromDegree(ra_dec[1]) + cosLatitude * MoonUtil.cosFromDegree(ra_dec[1]) * MoonUtil.cosFromDegree(tau);
    }

    protected abstract double[] getPosition(double julianCenturiesSinceJ2000);

    /**
     * 
     * @param yMinus
     * @param yThis
     * @param yPlus
     * @return [0] Number of roots found, [1] First root, [2] Second root, [3] yMinus, [4] yExtreme
     */
    private double[] getQuadraticInterpolation(double yMinus, double yThis, double yPlus) {
        double[] quadout = new double[4];
        int numberOfRoots = 0;
        double root1 = 0, root2 = 0;
        double a = 0.5 * (yMinus + yPlus) - yThis;
        double b = 0.5 * (yPlus - yMinus);
        double c = yThis;
        double xe = -b / (2 * a);
        double yExtreme = (a * xe + b) * xe + c;
        double dis = b * b - 4.0 * a * c;
        if (dis > 0) {
            double dx = 0.5 * Math.sqrt(dis) / Math.abs(a);
            root1 = xe - dx;
            root2 = xe + dx;
            if (Math.abs(root1) <= 1.0) {
                numberOfRoots += 1;
            }
            if (Math.abs(root2) <= 1.0) {
                numberOfRoots += 1;
            }
            if (root1 < -1.0) {
                root1 = root2;
            }
        }
        quadout[0] = numberOfRoots;
        quadout[1] = root1;
        quadout[2] = root2;
        quadout[3] = yExtreme;
        return quadout;
    }

    private double getLocalMeanSideRealTime(double mjd) {
        double utDaysSinceJ2000 = mjd - 51544.5d;
        double julianCenturiesSinceJ2000 = utDaysSinceJ2000 / 36525d;
        double lst = MoonUtil.fixAngleDegrees(280.46061837 + 360.98564736629 * utDaysSinceJ2000 + 0.000387933 * Math.pow(julianCenturiesSinceJ2000, 2) - Math.pow(julianCenturiesSinceJ2000, 3)
                / 38710000);
        return (lst / 15.0 + _longitude / 15);
    }

    protected double getFraction(double x) {
        double fraction = x - Math.floor(x);
        if (fraction < 0) {
            fraction += 1;
        }
        return fraction;
    }

    public static class DayTimes {

        private final Calendar _day;
        private final DayTime _riseTime;
        private final DayTime _setTime;
        private boolean _alwaysUp;
        private boolean _alwaysDown;

        public DayTimes(Calendar day, DayTime riseTime, DayTime setTime) {
            _day = day;
            _riseTime = riseTime;
            _setTime = setTime;
        }

        public Calendar getDay() {
            return _day;
        }

        public DayTime getRiseTime() {
            return _riseTime;
        }

        public DayTime getSetTime() {
            return _setTime;
        }

        public boolean isAlwaysUp() {
            return _alwaysUp;
        }

        public void setAlwaysUp(boolean alwaysUp) {
            _alwaysUp = alwaysUp;
        }

        public boolean isAlwaysDown() {
            return _alwaysDown;
        }

        public void setAlwaysDown(boolean alwaysDown) {
            _alwaysDown = alwaysDown;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[rise=" + _riseTime + ", set=" + _setTime + "]";
        }
    }

    public static class DayTime {
        public int hour;
        public int minute;

        public DayTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        @Override
        public String toString() {
            return hour + ":" + minute;
        }

    }

}
