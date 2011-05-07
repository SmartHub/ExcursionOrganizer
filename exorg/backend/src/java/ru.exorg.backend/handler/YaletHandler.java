package ru.exorg.backend.handler;

import net.sf.xfresh.core.YaletProcessor2;
import org.eclipse.jetty.server.handler.AbstractHandler;

public abstract class YaletHandler extends AbstractHandler {
    /* When genericity is an evil... */

    protected YaletProcessor2 processor;

    public void setYaletProcessor(YaletProcessor2 yp) {
        this.processor = yp;
    }
}