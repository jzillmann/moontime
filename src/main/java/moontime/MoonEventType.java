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
 * A {@link MoonPhase} but with a single date assignable.
 */
public enum MoonEventType {

    NEW_MOON(0.0d) {
        @Override
        public MoonEventType opposite() {
            return FULL_MOON;
        }
    },
    FIRST_QUARTER(0.25d) {
        @Override
        public MoonEventType opposite() {
            return LAST_QUARTER;
        }
    },
    FULL_MOON(0.5d) {
        @Override
        public MoonEventType opposite() {
            return NEW_MOON;
        }
    },
    LAST_QUARTER(0.75d) {
        @Override
        public MoonEventType opposite() {
            return FIRST_QUARTER;
        }
    };

    private final double _fraction;

    private MoonEventType(double fraction) {
        _fraction = fraction;
    }

    /**
     * @return value between 0..1
     */
    public double getFraction() {
        return _fraction;
    }

    public String getDisplayName() {
        char[] chars = name().toCharArray();
        StringBuilder displayName = new StringBuilder(chars.length);
        boolean initial = true;
        for (char c : chars) {
            if (initial) {
                initial = false;
                displayName.append(c);
            } else {
                if (c == '_') {
                    initial = true;
                    displayName.append(' ');
                } else {
                    displayName.append(Character.toLowerCase(c));
                }
            }
        }

        return displayName.toString();
    }

    public abstract MoonEventType opposite();

}
