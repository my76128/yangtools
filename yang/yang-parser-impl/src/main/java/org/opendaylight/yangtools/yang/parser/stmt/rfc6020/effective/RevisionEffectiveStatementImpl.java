/**
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.stmt.rfc6020.effective;

import java.util.Date;
import org.opendaylight.yangtools.yang.model.api.stmt.RevisionStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;

public class RevisionEffectiveStatementImpl extends
        EffectiveStatementBase<Date, RevisionStatement> {

    public RevisionEffectiveStatementImpl(
            StmtContext<Date, RevisionStatement, ?> ctx) {
        super(ctx);
    }

}