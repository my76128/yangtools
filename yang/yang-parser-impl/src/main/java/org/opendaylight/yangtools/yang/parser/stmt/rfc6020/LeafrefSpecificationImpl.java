/**
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.stmt.rfc6020;

import org.opendaylight.yangtools.yang.model.api.stmt.PathStatement;

import org.opendaylight.yangtools.yang.parser.spi.source.SourceException;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractStatementSupport;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractDeclaredStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;
import org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping;
import org.opendaylight.yangtools.yang.model.api.meta.EffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.TypeStatement;

public class LeafrefSpecificationImpl extends AbstractDeclaredStatement<String>
        implements TypeStatement.LeafrefSpecification {

    protected LeafrefSpecificationImpl(
            StmtContext<String, TypeStatement.LeafrefSpecification, ?> context) {
        super(context);
    }

    public static class Definition
            extends
            AbstractStatementSupport<String, TypeStatement.LeafrefSpecification, EffectiveStatement<String, TypeStatement.LeafrefSpecification>> {

        public Definition() {
            super(Rfc6020Mapping.PATH);
        }

        @Override
        public String parseArgumentValue(StmtContext<?, ?, ?> ctx, String value)
                throws SourceException {
            return value;
        }

        @Override
        public TypeStatement.LeafrefSpecification createDeclared(
                StmtContext<String, TypeStatement.LeafrefSpecification, ?> ctx) {
            return new LeafrefSpecificationImpl(ctx);
        }

        @Override
        public EffectiveStatement<String, TypeStatement.LeafrefSpecification> createEffective(
                StmtContext<String, TypeStatement.LeafrefSpecification, EffectiveStatement<String, TypeStatement.LeafrefSpecification>> ctx) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String getName() {
        return argument();
    }

    @Override
    public PathStatement getPath() {
        return firstDeclared(PathStatement.class);
    }

}
