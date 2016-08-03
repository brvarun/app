package automate.com.automate.util;

import com.squareup.otto.Bus;

/**
 * Created by varun on 7/30/2016.
 */
public class BusProvider {

    private static final Bus BUS = new Bus();

    public static Bus getInstance(){
        return BUS;
    }

    public BusProvider(){}
}
