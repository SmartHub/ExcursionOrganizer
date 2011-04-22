package ru.exorg.core.xfresh.ext;

import ru.exorg.core.xfresh.core.InternalRequest;
import ru.exorg.core.xfresh.core.InternalResponse;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.ContentHandler;

/**
 * Author: Olga Bolshakova (obolshakova@yandex-team.ru)
 * Date: 02.01.11 21:59
 */
public interface AuthHandler {

    void processAuth(InternalRequest req, InternalResponse res, ContentHandler handler);

    @Nullable
    Long getUserId(final InternalRequest req);
}
