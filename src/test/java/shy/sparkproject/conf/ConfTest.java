package shy.sparkproject.conf;

import org.junit.Test;

/**
 * Created by Shy on 2016/6/29.
 */
public class ConfTest {

    @Test
    public void testCOnf() {

        String t1 = ConfigurationManager.getProperty("testkey1");
        System.out.print(t1);
    }
}
