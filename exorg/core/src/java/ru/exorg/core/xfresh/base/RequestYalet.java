package ru.exorg.core.xfresh.base;

import ru.exorg.core.xfresh.core.Yalet;
import ru.exorg.core.xfresh.core.InternalRequest;
import ru.exorg.core.xfresh.core.InternalResponse;

/**
 * Date: 29.08.2007
 * Time: 8:49:53
 *
 * Shows content of request.
 *
 * @author Nikolay Malevanny nmalevanny@yandex-team.ru
 */
public class RequestYalet implements Yalet {
    public void process(final InternalRequest req, final InternalResponse res) {
        res.add(new Request(req));
    }
}
