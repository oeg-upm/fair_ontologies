/*
 * Copyright 2021-22 Ontology Engineering Group, Universidad Politecnica de Madrid, Spain
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Author: Daniel Garijo and Maria Poveda
 */
package fair;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void isLicenseResolvable_1() {
        String bad_url = "http://bad_url.org/license";
        assertFalse(Utils.isURIResolvable(bad_url));
    }

    @Test
    public void isLicenseResolvable_2() {
        String resolvable_url = "http://creativecommons.org/licenses/by/2.0/";
        assertTrue(Utils.isURIResolvable(resolvable_url));
    }

    @Test
    public void isLicenseResolvable_3() {
        String empty_url = "";
        assertFalse(Utils.isURIResolvable(empty_url));
    }

    @Test
    public void isLicenseResolvable_4() {
        String null_url = null;
        assertFalse(Utils.isURIResolvable(null_url));
    }

    @Test
    public void isLicenseResolvable_5() {
        String random_string = "ñasodnivoev";
        assertFalse(Utils.isURIResolvable(random_string));
    }


    //Meter mas tests


}