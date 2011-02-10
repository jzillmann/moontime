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

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import moontime.Constants;
import moontime.Moon;
import moontime.MoonEvent;
import moontime.MoonEventType;
import moontime.MoonPhaseAlgorithm;
import moontime.testsupport.AbstractTest;

import org.fest.assertions.Delta;
import org.junit.Test;

public abstract class AbstractMoonPhaseAlgorithmTest extends AbstractTest {

    /** allowed deviation */
    private final double _moonAgeDelta;
    private final double _moonFractionDelta;
    private final long _moonEventDeviationDelta;
    private final MoonPhaseAlgorithm _algorithm;

    public AbstractMoonPhaseAlgorithmTest(MoonPhaseAlgorithm algorithm, double moonAgeDelta, double moonFractionDelta, long moonEventDeviationDelta) {
        _algorithm = algorithm;
        _moonAgeDelta = moonAgeDelta;
        _moonFractionDelta = moonFractionDelta;
        _moonEventDeviationDelta = moonEventDeviationDelta;
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCalculateNexMoonEvents() {
        List<MoonEvent> events = _algorithm.getNextMoonEvents(new Date(1295129899864L), 5, EnumSet.of(MoonEventType.NEW_MOON, MoonEventType.FULL_MOON));
        assertThat(events).hasSize(5);
        List<MoonEvent> expectedEvents = new ArrayList<MoonEvent>();
        expectedEvents.add(new MoonEvent(MoonEventType.FULL_MOON, new Date(2011 - 1900, 0, 19, 21, 21)));
        expectedEvents.add(new MoonEvent(MoonEventType.NEW_MOON, new Date(2011 - 1900, 1, 3, 2, 31)));
        expectedEvents.add(new MoonEvent(MoonEventType.FULL_MOON, new Date(2011 - 1900, 1, 18, 8, 36)));
        expectedEvents.add(new MoonEvent(MoonEventType.NEW_MOON, new Date(2011 - 1900, 2, 4, 20, 46)));
        expectedEvents.add(new MoonEvent(MoonEventType.FULL_MOON, new Date(2011 - 1900, 2, 19, 18, 10)));
        for (int i = 0; i < events.size(); i++) {
            printEvent(events.get(i), expectedEvents.get(i).getDate());
            assertThat(events.get(i).getType()).isEqualTo(expectedEvents.get(i).getType());
            assertDate(expectedEvents.get(i).getDate(), events.get(i).getDate(), _moonEventDeviationDelta);
            assertThat(events.get(i).getType()).isEqualTo(expectedEvents.get(i).getType());
        }
    }

    @Test
    public void testCalculateMoon() throws Exception {
        List<MoonEvent> events = _algorithm.getNextMoonEvents(new Date(1295129899864L), MoonEventType.values().length, EnumSet.allOf(MoonEventType.class));
        for (MoonEvent moonEvent : events) {
            Moon moon = _algorithm.calculate(moonEvent.getDate());
            System.out.println(moonEvent.getType() + ": " + moon);
            double expectedAge = Constants.SYNODIC_MONTH * moonEvent.getType().getFraction();
            double expectedFraction = moonEvent.getType().getFraction();
            if (moonEvent.getType() == MoonEventType.NEW_MOON && moon.getMoonAge() > 1) {
                expectedAge = Constants.SYNODIC_MONTH;
                expectedFraction = 1.0d;
            }
            assertThat(moon.getMoonAge()).describedAs("moon-age for " + moonEvent).isEqualTo(expectedAge, Delta.delta(_moonAgeDelta));
            assertThat(moon.getFraction()).describedAs("fraction for " + moonEvent).isEqualTo(expectedFraction, Delta.delta(_moonFractionDelta));
        }
    }

    private void printEvent(MoonEvent moonEvent, Date expectedDate) {
        System.out.println(moonEvent + " || " + getDifference(moonEvent, expectedDate));
    }

    private String getDifference(MoonEvent moonEvent, Date date) {
        long difference = date.getTime() - moonEvent.getDate().getTime();
        return (difference > 0 ? "-" : "+") + formatTimeDuration(Math.abs(difference));
    }

}
