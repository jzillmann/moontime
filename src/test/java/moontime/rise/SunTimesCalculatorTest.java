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

import static org.fest.assertions.Assertions.*;

import java.util.Calendar;

import moontime.MoonUtil;
import moontime.rise.LuminaryDayTimesCalculator.DayTimes;
import moontime.rise.SunTimesCalculator.SunTimes;
import moontime.testsupport.AbstractTest;
import moontime.testsupport.TestConstants;

import org.junit.Test;

public class SunTimesCalculatorTest extends AbstractTest {

    private SunTimesCalculator _calc = new SunTimesCalculator(TestConstants.LEIPZIG_LONGITUDE, TestConstants.LEIPZIG_LATITUDE);

    @Test
    public void testRiseAndSet() throws Exception {
        Calendar calendar = MoonUtil.newCalendar(2011, 3, 3);
        SunTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getRiseTime().hour).as(times.getRiseTime().toString()).isEqualTo(6);
        assertThat(times.getRiseTime().minute).as(times.getRiseTime().toString()).isGreaterThan(40).isLessThan(46);
        assertThat(times.getSetTime().hour).as(times.getSetTime().toString()).isEqualTo(19);
        assertThat(times.getSetTime().minute).as(times.getSetTime().toString()).isGreaterThan(43).isLessThan(49);
        assertThat(times.getDawnStart().hour).as(times.getDawnStart().toString()).isEqualTo(6);
        assertThat(times.getDawnStart().minute).as(times.getDawnStart().toString()).isGreaterThan(6).isLessThan(12);
        assertThat(times.getDuskEnd().hour).as(times.getDuskEnd().toString()).isEqualTo(20);
        assertThat(times.getDuskEnd().minute).as(times.getDuskEnd().toString()).isGreaterThan(17).isLessThan(23);
    }

    @Test
    public void testRiseAndSet_AlwaysUp() throws Exception {
        _calc = new SunTimesCalculator(TestConstants.KONGSFJORDEN_LONGITUDE, TestConstants.KONGSFJORDEN_LATITUDE);
        Calendar calendar = MoonUtil.newCalendar(2011, 7, 4);
        DayTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getSetTime()).isNull();
        assertThat(times.getRiseTime()).isNull();
        assertThat(times.isAlwaysUp()).isTrue();
        assertThat(times.isAlwaysDown()).isFalse();
    }

    @Test
    public void testRiseAndSet_AlwaysDown() throws Exception {
        _calc = new SunTimesCalculator(TestConstants.KONGSFJORDEN_LONGITUDE, TestConstants.KONGSFJORDEN_LATITUDE);
        Calendar calendar = MoonUtil.newCalendar(2011, 10, 2);
        DayTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getSetTime()).isNull();
        assertThat(times.getRiseTime()).isNull();
        assertThat(times.isAlwaysUp()).isFalse();
        assertThat(times.isAlwaysDown()).isTrue();
    }
}
