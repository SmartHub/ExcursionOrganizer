package ru.exorg.core.xfresh.ext;

import ru.exorg.core.xfresh.core.InternalRequest;
import ru.exorg.core.xfresh.core.InternalResponse;
import ru.exorg.core.xfresh.core.xml.Xmler;
import org.xml.sax.ContentHandler;

/**
 * Author: Olga Bolshakova (obolshakova@yandex-team.ru)
 * Date: 02.01.11 22:04
 */
public final class AlwaysNoAuthHandler implements AuthHandler {

    public void processAuth(final InternalRequest req, final InternalResponse res, final ContentHandler handler) {
        Xmler.tag("no-auth").writeTo(handler);
    }

    public Long getUserId(final InternalRequest req) {
        return null;
    }
}
