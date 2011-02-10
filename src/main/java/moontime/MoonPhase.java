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

/**
 * The phases of the moon. Moon phases to which a single date can be assigned, like new-moon, are
 * defined in {@link MoonEventType}.
 * 
 */
public enum MoonPhase {

    /***/
    WAXING_CRESCENT(MoonEventType.FIRST_QUARTER, 0d, 0.25d, 0, 7),
    /***/
    WAXING_GIBBOUS(MoonEventType.FULL_MOON, 0.25d, 0.5d, 7, 15),
    /***/
    WANING_GIBBOUS(MoonEventType.LAST_QUARTER, 0.5d, 0.75d, 15, 23),
    /***/
    WANING_CRESCENT(MoonEventType.NEW_MOON, 0.75d, 1d, 23, 31);

    private final MoonEventType _culminatingEvent;
    private final double _startFraction;
    private final double _endFraction;
    private final double _startAge;
    private final double _endAge;

    private MoonPhase(MoonEventType culminatingEvent, double startFraction, double endFraction, double startAge, double endAge) {
        _culminatingEvent = culminatingEvent;
        _startFraction = startFraction;
        _endFraction = endFraction;
        _startAge = startAge;
        _endAge = endAge;
    }

    public MoonEventType getCulminatingEventType() {
        return _culminatingEvent;
    }

    public static MoonPhase getMoonPhase(double moonFraction) {
        MoonPhase[] values = values();
        for (MoonPhase moonPhase : values) {
            if (moonFraction > moonPhase._startFraction && moonFraction < moonPhase._endFraction) {
                return moonPhase;
            }
        }
        throw new IllegalStateException("no moon-phase found for fraction '" + moonFraction + "'");
    }

    public static MoonPhase getMoonPhaseByAge(double moonAge) {
        MoonPhase[] values = values();
        for (MoonPhase moonPhase : values) {
            if (moonAge >= moonPhase._startAge && moonAge <= moonPhase._endAge) {
                return moonPhase;
            }
        }
        throw new IllegalStateException("no moon-phase found for fraction '" + moonAge + "'");
    }
}
