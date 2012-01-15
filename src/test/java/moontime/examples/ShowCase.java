package moontime.examples;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

import moontime.MoonEvent;
import moontime.MoonEventType;
import moontime.alg.MoonToolPhaseAlgorithm;
import moontime.rise.LuminaryDayTimesCalculator;
import moontime.rise.LuminaryDayTimesCalculator.DayTimes;
import moontime.rise.MoonTimesCalculator;
import moontime.rise.SunTimesCalculator;
import moontime.testsupport.TestConstants;

import org.junit.Test;

public class ShowCase {

    private Calendar _now = Calendar.getInstance();
    
    @Test
    public void testNextMoonEvents() throws Exception {
        System.out.println("next moon events:");
        MoonToolPhaseAlgorithm phaseAlgorithm = new MoonToolPhaseAlgorithm();
        List<MoonEvent> nextMoonEvents = phaseAlgorithm.getNextMoonEvents(_now, 3, EnumSet.of(MoonEventType.FULL_MOON,MoonEventType.NEW_MOON));
        for (MoonEvent moonEvent : nextMoonEvents) {
            System.out.println("\t:"+moonEvent);
        }
    }
    
    @Test
    public void testSunMoonRise() throws Exception {
         LuminaryDayTimesCalculator moon = new MoonTimesCalculator(TestConstants.LEIPZIG_LONGITUDE, TestConstants.LEIPZIG_LATITUDE);
         DayTimes moonDayTimes = moon.calculateDayTimes(_now);
         System.out.println("\nmoonrise ("+moonDayTimes.getDay().getTime()+"):");
         System.out.println("\t"+moonDayTimes);
         
         LuminaryDayTimesCalculator sun = new SunTimesCalculator(TestConstants.LEIPZIG_LONGITUDE, TestConstants.LEIPZIG_LATITUDE);
         DayTimes sunDayTimes = sun.calculateDayTimes(_now);
         System.out.println("\nsunrise ("+sunDayTimes.getDay().getTime()+"):");
         System.out.println("\t"+sunDayTimes);
    }
}
