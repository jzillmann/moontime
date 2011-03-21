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
package moontime;

import static org.fest.assertions.Assertions.*;

import moontime.MoonEvent.EventAllocation;

import org.junit.Test;

public class MoonEventTest {

    @Test
    public void testEventAllocation() throws Exception {
        long eventTime = 20;
        long eventPhaseMin = 5;
        long eventPhaseMax = 7;

        assertThat(EventAllocation.getEventAllocation(-1, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_FUTURE);
        assertThat(EventAllocation.getEventAllocation(0, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_FUTURE);
        assertThat(EventAllocation.getEventAllocation(14, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_FUTURE);

        assertThat(EventAllocation.getEventAllocation(15, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_PRESENT);
        assertThat(EventAllocation.getEventAllocation(20, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_PRESENT);
        assertThat(EventAllocation.getEventAllocation(27, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_PRESENT);

        assertThat(EventAllocation.getEventAllocation(28, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_PAST);
        assertThat(EventAllocation.getEventAllocation(Long.MAX_VALUE, eventTime, eventPhaseMin, eventPhaseMax)).isEqualTo(EventAllocation.IN_PAST);
    }

}
