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

import org.junit.After;
import org.junit.Test;

public class ShowCase {

    private Calendar _now = Calendar.getInstance();

    @After
    public void after() {
        System.out.println();
    }

    @Test
    public void testNextMoonEvents() throws Exception {
        System.out.println("next moon events:");
        MoonToolPhaseAlgorithm phaseAlgorithm = new MoonToolPhaseAlgorithm();
        List<MoonEvent> nextMoonEvents = phaseAlgorithm.getNextMoonEvents(_now, 3, EnumSet.of(MoonEventType.FULL_MOON, MoonEventType.NEW_MOON));
        for (MoonEvent moonEvent : nextMoonEvents) {
            System.out.println("\t" + moonEvent);
        }
    }

    @Test
    public void testMoonRiseSet() throws Exception {
        LuminaryDayTimesCalculator moon = new MoonTimesCalculator(TestConstants.LEIPZIG_LONGITUDE, TestConstants.LEIPZIG_LATITUDE);
        DayTimes moonDayTimes = moon.calculateDayTimes(_now);
        System.out.println("moonrise (" + moonDayTimes.getDay().getTime() + "):");
        System.out.println("\t" + moonDayTimes);
    }

    @Test
    public void testSunRiseSet() throws Exception {
        LuminaryDayTimesCalculator sun = new SunTimesCalculator(TestConstants.LEIPZIG_LONGITUDE, TestConstants.LEIPZIG_LATITUDE);
        DayTimes sunDayTimes = sun.calculateDayTimes(_now);
        System.out.println("sunrise (" + sunDayTimes.getDay().getTime() + "):");
        System.out.println("\t" + sunDayTimes);
    }
}
