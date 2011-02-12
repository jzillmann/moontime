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
import java.util.EnumSet;
import java.util.List;

/**
 * An algorithm to calculate the fraction of the moon to a given date.
 */
public interface MoonPhaseAlgorithm {

    public Moon calculate(Calendar date);

    public List<MoonEvent> getNextMoonEvents(Calendar upFromDate, int count, EnumSet<MoonEventType> includedTypes);

    // public double calculateAge(Date date);

    // TODO min date / max date

}
