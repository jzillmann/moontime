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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

import moontime.Constants;
import moontime.Moon;
import moontime.MoonEvent;
import moontime.MoonEventType;
import moontime.MoonPhase;
import moontime.MoonPhaseAlgorithm;

/**
 * This is based on a 'do it in your head' algorithm by John Conway. In its current form, it's only
 * valid for the 20th and 21st centuries.
 * 
 * See http://www.ben-daglish.net/moon.shtml
 */
public class ConwayMoonPhaseAlgorithm implements MoonPhaseAlgorithm, Constants {

    @Override
    public Moon calculate(Calendar calendar) {
        double moonAge = calculateAge(calendar);
        return new Moon(moonAge, moonAge / SYNODIC_MONTH);
    }

    @Override
    public List<MoonEvent> getNextMoonEvents(Calendar calender, int count, EnumSet<MoonEventType> includedTypes) {
        List<MoonEvent> moonEventsData = new ArrayList<MoonEvent>();
        int countedEvents = 0;
        double moonAge = calculate(calender).getMoonAge();
        MoonPhase currentPhase = MoonPhase.getMoonPhaseByAge(moonAge);
        while (countedEvents < count) {
            calender.add(Calendar.HOUR_OF_DAY, 1);
            moonAge = calculate(calender).getMoonAge();
            if (MoonPhase.getMoonPhaseByAge(moonAge) != currentPhase) {
                if (includedTypes.contains(currentPhase.getCulminatingEventType())) {
                    moonEventsData.add(new MoonEvent(currentPhase.getCulminatingEventType(), calender.getTime()));
                    countedEvents++;
                }
                currentPhase = MoonPhase.getMoonPhaseByAge(moonAge);
            }
        }
        return moonEventsData;
    }

    public double calculateAge(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // TODO how to get hours, minutes, seconds into this algorithm ?
        // int hours = cal.get(Calendar.HOUR_OF_DAY);
        // int minutes = cal.get(Calendar.MINUTE);
        // int seconds = cal.get(Calendar.SECOND);
        double result = year % 100;
        result %= 19;
        if (result > 9) {
            result -= 19;
        }
        result = ((result * 11) % SYNODIC_MONTH) + month + day;
        if (month < 3) {
            result += 2;
        }
        result -= ((year < 2000) ? 4 : 8.3);
        result = Math.floor(result + 0.5) % SYNODIC_MONTH;
        result = (result < 0) ? result + SYNODIC_MONTH : result;
        return result;
    }

}