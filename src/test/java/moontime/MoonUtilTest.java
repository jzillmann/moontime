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

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import moontime.testsupport.AbstractTest;

import org.junit.Test;

public class MoonUtilTest extends AbstractTest {

    @Test
    public void testConvertTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        System.out.println(MoonUtil.toJulian(calendar));
        System.out.println(calendar.getTime());
        System.out.println(MoonUtil.toGregorian(MoonUtil.toJulian(calendar)).getTime());
        assertDate(MoonUtil.toGregorian(MoonUtil.toJulian(calendar)).getTime(), calendar.getTime(), TimeUnit.MINUTES.toMillis(1));
    }

}
