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
package moontime.testsupport;

import static org.junit.Assert.*;

import java.util.Date;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class AbstractTest {

    @Rule
    public PrintTestNameRule _printPrintTestNameRule = new PrintTestNameRule();

    static class PrintTestNameRule implements MethodRule {
        public Statement apply(Statement base, FrameworkMethod method, Object target) {
            System.out.println("~~~~~~~~~~~~~~~ " + target.getClass().getName() + "#" + method.getName() + "() ~~~~~~~~~~~~~~~");
            return base;
        }
    }

    public void assertDate(Date date1, Date date2, long allowedDeviationInMs) {
        assertThat(date1.getTime(), almostEquals(date2.getTime(), allowedDeviationInMs, "expected " + date1.toString() + " with deviation of " + allowedDeviationInMs + " ms but was " + date2));
    }

    protected Matcher<Long> almostEquals(final long value1, final long aberration, final String descriptionText) {
        return new BaseMatcher<Long>() {
            @Override
            public boolean matches(Object value2) {
                Long long2 = (Long) value2;
                return Math.abs(value1 - long2) < aberration;
            }

            @Override
            public void describeTo(Description description) {
                if (descriptionText == null) {
                    description.appendText(" equals " + value1 + " with aberration of " + aberration);
                } else {
                    description.appendText(descriptionText);
                }
            }
        };
    }

    public static String formatTimeDuration(long timeDuration) {
        StringBuilder builder = new StringBuilder();
        long hours = timeDuration / (60 * 60 * 1000);
        long rem = (timeDuration % (60 * 60 * 1000));
        long minutes = rem / (60 * 1000);
        rem = rem % (60 * 1000);
        long seconds = rem / 1000;

        if (hours != 0) {
            builder.append(hours);
            builder.append(" hrs, ");
        }
        if (minutes != 0) {
            builder.append(minutes);
            builder.append(" mins, ");
        }
        // return "0sec if no difference
        builder.append(seconds);
        builder.append(" sec");
        return builder.toString();
    }

}
