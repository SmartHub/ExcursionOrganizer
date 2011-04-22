package ru.exorg.core.xfresh.ext;

import ru.exorg.core.xfresh.core.DefaultYaletSupport;
import ru.exorg.core.xfresh.core.InternalRequest;
import ru.exorg.core.xfresh.core.InternalResponse;
import ru.exorg.core.xfresh.core.SaxGenerator;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.XMLFilter;

/**
 * Date: Nov 24, 2010
 * Time: 11:14:42 PM
 *
 * @author Nikolay Malevanny nmalevanny@yandex-team.ru
 */
public class ExtYaletSupport extends DefaultYaletSupport {

    protected String resourceBase;
    protected SaxGenerator saxGenerator;

    @Required
    public void setResourceBase(final String resourceBase) {
        this.resourceBase = resourceBase;
    }

    @Required
    public void setSaxGenerator(SaxGenerator saxGenerator) {
        this.saxGenerator = saxGenerator;
    }

    @Override
    public XMLFilter createFilter(final InternalRequest request, final InternalResponse response) {
        return new ExtYaletFilter(singleYaletProcessor, authHandler, request, response, resourceBase, saxGenerator);
    }
}
