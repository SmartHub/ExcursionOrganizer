package eo.miner;

import java.util.*;
import java.lang.*;

import org.springframework.beans.factory.InitializingBean;

// ================================================================================

public class Main implements InitializingBean {
    private List<Miner> miners;

    public void setMiners(List<Miner> m) {
        miners = m;
    }

    final public void afterPropertiesSet() {
        for (Miner m : miners) {
            m.run();
        }
    }
}