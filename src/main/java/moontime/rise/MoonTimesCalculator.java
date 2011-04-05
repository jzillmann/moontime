package moontime.rise;

import java.util.Calendar;

import moontime.MoonUtil;

public class MoonTimesCalculator extends LuminaryDayTimesCalculator {

    public MoonTimesCalculator(double longitude, double latitude) {
        super(longitude, latitude);
    }

    @Override
    public DayTimes calculateDayTimes(Calendar calendar) {
        return calculateTimes(calendar, MoonUtil.sinFromDegree(MOONS_ALTITUDE_CENTER));
    }

    @Override
    protected double[] getPosition(double julianCenturiesSinceJ2000) {
        double p2 = 6.283185307, arc = 206264.8062, coseps = 0.91748, sineps = 0.39778;
        double[] ephemeris = new double[3];

        double L0 = getFraction(0.606433 + 1336.855225 * julianCenturiesSinceJ2000); // mean-longitude
        double L = p2 * getFraction(0.374897 + 1325.552410 * julianCenturiesSinceJ2000); // mean-anomaly
        double LS = p2 * getFraction(0.993133 + 99.997361 * julianCenturiesSinceJ2000); // mean-sun-anomaly
        double D = p2 * getFraction(0.827361 + 1236.853086 * julianCenturiesSinceJ2000); // difference-moon/sun
        double F = p2 * getFraction(0.259086 + 1342.227825 * julianCenturiesSinceJ2000); // mean-latitude

        // corrections to mean longitude
        double DL = 22640 * Math.sin(L);
        DL += -4586 * Math.sin(L - 2 * D);
        DL += +2370 * Math.sin(2 * D);
        DL += +769 * Math.sin(2 * L);
        DL += -668 * Math.sin(LS);
        DL += -412 * Math.sin(2 * F);
        DL += -212 * Math.sin(2 * L - 2 * D);
        DL += -206 * Math.sin(L + LS - 2 * D);
        DL += +192 * Math.sin(L + 2 * D);
        DL += -165 * Math.sin(LS - 2 * D);
        DL += -125 * Math.sin(D);
        DL += -110 * Math.sin(L + LS);
        DL += +148 * Math.sin(L - LS);
        DL += -55 * Math.sin(2 * F - 2 * D);

        double S = F + (DL + 412 * Math.sin(2 * F) + 541 * Math.sin(LS)) / arc;
        double H = F - 2 * D;
        double N = -526 * Math.sin(H);
        N += +44 * Math.sin(L + H);
        N += -31 * Math.sin(-L + H);
        N += -23 * Math.sin(LS + H);
        N += +11 * Math.sin(-LS + H);
        N += -25 * Math.sin(-2 * L + F);
        N += +21 * Math.sin(-L + F);

        double L_moon = p2 * getFraction(L0 + DL / 1296000);
        double B_moon = (18520.0 * Math.sin(S) + N) / arc;

        // equatorial coordinates
        double CB = Math.cos(B_moon);
        double X = CB * Math.cos(L_moon);
        double V = CB * Math.sin(L_moon);
        double W = Math.sin(B_moon);
        double Y = coseps * V - sineps * W;
        double Z = sineps * V + coseps * W;
        double RHO = Math.sqrt(1.0 - Z * Z);
        double dec = (360.0 / p2) * Math.atan(Z / RHO);
        double ra = (48.0 / p2) * Math.atan(Y / (X + RHO));
        if (ra < 0) {
            ra += 24;
        }
        ephemeris[1] = dec;
        ephemeris[2] = ra;
        return ephemeris;
    }

}
