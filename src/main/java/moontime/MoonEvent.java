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

import java.util.Date;

public class MoonEvent {

    private final MoonEventType _type;
    private final Date _date;

    public MoonEvent(MoonEventType type, Date date) {
        _type = type;
        _date = date;
    }

    public MoonEventType getType() {
        return _type;
    }

    public Date getDate() {
        return _date;
    }

    @Override
    public String toString() {
        return getType() + ": " + getDate();
    }

}
