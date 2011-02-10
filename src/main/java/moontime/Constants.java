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

import java.util.concurrent.TimeUnit;

public interface Constants {

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~ EARTH ~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /** Eccentricity of Earth's orbit */
    double ECCENTRICITY = 0.016718d;
    /** 1980 January 0.0 */
    double EPOCH = 2444238.5d;

    //
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~ SUN ~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /** Elliptic longitude of the Sun at epoch 1980.0 */
    double SUN_ECLIPTIC_LONGITUDE_AT_EPOCH = 278.833540d;
    /** Elliptic longitude of the Sun at perigee */
    double SUN_ECLIPTIC_LONGITUDE_AT_EPOCH_AT_PERIGREE = 282.596403d;

    //
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~ MOON ~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /** Moon's mean longitude at the epoch */
    double MOONS_MEAN_LONGITUDE = 64.975464d;
    /** Mean longitude of the perigee at the epoch */
    double MOONS_MEAN_LONGITUDE_AT_EPOCH_AT_PERIGREE = 349.383063d;
    /** (new Moon to new Moon) in days */
    public static double SYNODIC_MONTH = (double) (TimeUnit.DAYS.toMillis(29) + TimeUnit.HOURS.toMillis(12) + TimeUnit.MINUTES.toMillis(44) + 2800) / 1000 / 60 / 60 / 24;

}
