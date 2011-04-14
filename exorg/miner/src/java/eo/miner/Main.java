package eo.miner;

import java.util.*;
import java.lang.*;

import org.springframework.beans.factory.InitializingBean;

import eo.db.*;

// ================================================================================

public class Main implements InitializingBean {
    private List<Miner> miners;
    private DataProvider dataProvider;

    final public void setMiners(List<Miner> m) {
        this.miners = m;
    }

    final public void setDataProvider(DataProvider p) {
        this.dataProvider = p;
    }

    final public void afterPropertiesSet() {
        for (Miner miner : this.miners) {
            miner.setDataProvider(dataProvider);
        }

        for (Miner m : miners) {
            m.run();
        }
    }
}