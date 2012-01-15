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
import moontime.testsupport.AbstractTest;
import moontime.testsupport.TestConstants;

import org.junit.Test;

public class MoonTimesCalculatorTest extends AbstractTest {

    private LuminaryDayTimesCalculator _calc = new MoonTimesCalculator(TestConstants.LEIPZIG_LONGITUDE, TestConstants.LEIPZIG_LATITUDE);

    @Test
    public void testRiseAndSet() throws Exception {
        Calendar calendar = MoonUtil.newCalendar(2011, 3, 3);
        DayTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getRiseTime().hour).as(times.getRiseTime().toString()).isEqualTo(6);
        assertThat(times.getRiseTime().minute).as(times.getRiseTime().toString()).isGreaterThan(7).isLessThan(12);
        assertThat(times.getSetTime().hour).as(times.getSetTime().toString()).isEqualTo(20);
        assertThat(times.getSetTime().minute).as(times.getSetTime().toString()).isGreaterThan(0).isLessThan(8);
    }

    @Test
    public void testRiseAndSet_NoRise() throws Exception {
        Calendar calendar = MoonUtil.newCalendar(2011, 5, 21);
        DayTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getSetTime().hour).as(times.getSetTime().toString()).isEqualTo(11);
        assertThat(times.getSetTime().minute).as(times.getSetTime().toString()).isGreaterThan(12).isLessThan(18);
        assertThat(times.getRiseTime()).isNull();
    }

    @Test
    public void testRiseAndSet_NoSet() throws Exception {
        Calendar calendar = MoonUtil.newCalendar(2011, 3, 7);
        DayTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getRiseTime().hour).as(times.getRiseTime().toString()).isEqualTo(7);
        assertThat(times.getRiseTime().minute).as(times.getRiseTime().toString()).isGreaterThan(45).isLessThan(51);
        assertThat(times.getSetTime()).isNull();
    }

    @Test
    public void testRiseAndSet_AlwaysUp() throws Exception {
        _calc = new MoonTimesCalculator(TestConstants.KONGSFJORDEN_LONGITUDE, TestConstants.KONGSFJORDEN_LATITUDE);
        Calendar calendar = MoonUtil.newCalendar(2011, 7, 20);
        DayTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getSetTime()).isNull();
        assertThat(times.getRiseTime()).isNull();
        assertThat(times.isAlwaysUp()).isTrue();
        assertThat(times.isAlwaysDown()).isFalse();
    }

    @Test
    public void testRiseAndSet_AlwaysDown() throws Exception {
        _calc = new MoonTimesCalculator(TestConstants.KONGSFJORDEN_LONGITUDE, TestConstants.KONGSFJORDEN_LATITUDE);
        Calendar calendar = MoonUtil.newCalendar(2011, 7, 10);
        DayTimes times = _calc.calculateDayTimes(calendar);
        assertThat(times.getSetTime()).isNull();
        assertThat(times.getRiseTime()).isNull();
        assertThat(times.isAlwaysUp()).isFalse();
        assertThat(times.isAlwaysDown()).isTrue();
    }

}
