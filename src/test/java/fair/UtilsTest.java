package fair;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void isLicenseResolvable_1() {
        String bad_url = "http://bad_url.org/license";
        assertFalse(Utils.isLicenseResolvable(bad_url));
    }

    @Test
    public void isLicenseResolvable_2() {
        String resolvable_url = "http://creativecommons.org/licenses/by/2.0/";
        assertTrue(Utils.isLicenseResolvable(resolvable_url));
    }
}