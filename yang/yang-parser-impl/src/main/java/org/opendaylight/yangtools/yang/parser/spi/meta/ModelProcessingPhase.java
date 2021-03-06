/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.spi.meta;

import javax.annotation.Nullable;

public enum ModelProcessingPhase {
    /**
     *
     * Cross-source relationship resolution phase.
     * <p>
     * In this phase of processing only statements which affects
     * cross-source relationship (e.g. imports / includes)
     * are processed.
     * <p>
     * At end of this phase all source related contexts should
     * be bind to their imports and includes to allow
     * visibility of custom defined statements in following
     * phases.
     */
    SOURCE_LINKAGE(null),
    STATEMENT_DEFINITION(SOURCE_LINKAGE),
    FULL_DECLARATION(STATEMENT_DEFINITION),
    EFFECTIVE_MODEL(FULL_DECLARATION);

    private final ModelProcessingPhase previousPhase;

    private ModelProcessingPhase(@Nullable ModelProcessingPhase previous) {
        this.previousPhase = previous;
    }

    public ModelProcessingPhase getPreviousPhase() {
        return previousPhase;
    }
}
