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

public interface Constants {

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~ EARTH ~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /** Eccentricity of Earth's orbit */
    double ECCENTRICITY = 0.016718d;
    /** 1980 January */
    double EPOCH = 2444238.5d;
    /** January 1, 1970 at 00:00:00 UTC */
    double JULIAN_1_1_1970 = 2440587.5d;
    double SECONDS_PER_DAY = 86400.0d;

    //
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~ SUN ~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /** Elliptic longitude of the Sun at epoch 1980.0 */
    double SUN_ECLIPTIC_LONGITUDE_AT_EPOCH = 278.833540d;
    /** Elliptic longitude of the Sun at perigee */
    double SUN_ECLIPTIC_LONGITUDE_AT_EPOCH_AT_PERIGREE = 282.596403d;
    double SUN_ALTITUDE_UPPER_LIMB_TOUCHING_HORIZON = -0.833d;
    double SUN_ALTITUDE_CIVIL_TWIGHLIGHT = -6.0d;

    //
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~ MOON ~~~~~~~~~~~~~ */
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    /** Moon's mean longitude at the epoch */
    double MOONS_MEAN_LONGITUDE = 64.975464d;
    /** Mean longitude of the perigee at the epoch */
    double MOONS_MEAN_LONGITUDE_AT_EPOCH_AT_PERIGREE = 349.383063d;
    /** (new Moon to new Moon) in days */
    double SYNODIC_MONTH = 29.530587962962958d;
    double MOONS_ALTITUDE_CENTER = 8 / 60;

}
