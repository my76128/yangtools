/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.stmt.test;

import static org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping.CONTAINER;
import static org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping.IMPORT;
import static org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping.MODULE;
import static org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping.NAMESPACE;
import static org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping.PREFIX;
import static org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping.REVISION;
import static org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping.REVISION_DATE;
import java.util.Arrays;
import java.util.List;
import org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping;
import org.opendaylight.yangtools.yang.model.api.meta.StatementSource;
import org.opendaylight.yangtools.yang.parser.spi.source.PrefixToModule;
import org.opendaylight.yangtools.yang.parser.spi.source.QNameToStatementDefinition;
import org.opendaylight.yangtools.yang.parser.spi.source.SourceException;
import org.opendaylight.yangtools.yang.parser.spi.source.StatementSourceReference;
import org.opendaylight.yangtools.yang.parser.spi.source.StatementStreamSource;
import org.opendaylight.yangtools.yang.parser.spi.source.StatementWriter;

class TestStatementSource implements StatementStreamSource {

    public static class ModuleEntry {

        private final String name;
        private final String revision;

        public ModuleEntry(String name, String revision) {

            this.name = name;
            this.revision = revision;
        }

        public String getName() {

            return name;
        }

        public String getRevision() {

            return revision;
        }
    }

    private static final String NS_PREFIX = "urn:org:opendaylight:yangtools:test:";

    private final String name;
    private final String revision;
    private final List<ModuleEntry> imports;
    private String container;
    private StatementWriter writer;
    private StatementSourceReference REF = new StatementSourceReference() {

        @Override
        public StatementSource getStatementSource() {
            return StatementSource.DECLARATION;
        }
    };

    public TestStatementSource(ModuleEntry module, ModuleEntry... imports) {

        this.name = module.getName();
        this.revision = module.getRevision();
        this.imports = Arrays.asList(imports);
    }

    public void setContainer(String container) {

        this.container = container;
    }

    @Override
    public void writeFull(StatementWriter writer, QNameToStatementDefinition stmtDef, PrefixToModule prefixes)
            throws SourceException {
        this.writer = writer;
        header();
        extensions();
        body();
        end();
    }

    @Override
    public void writeLinkage(StatementWriter writer, QNameToStatementDefinition stmtDef) throws SourceException {
        this.writer = writer;
        header().end();
    }

    @Override
    public void writeLinkageAndStatementDefinitions(StatementWriter writer, QNameToStatementDefinition stmtDef,
            PrefixToModule prefixes) throws SourceException {
        this.writer = writer;
        header();
        extensions();
        end();
    }

    protected void extensions() throws SourceException {

    }

    protected void body() throws SourceException {

        if (container != null)
            stmt(CONTAINER).arg(container).end();
    }

    TestStatementSource header() throws SourceException {

        stmt(MODULE).arg(name);
        {
            stmt(NAMESPACE).arg(getNamespace()).end();
            stmt(PREFIX).arg(name).end();

            if (revision != null) {
                stmt(REVISION).arg(revision).end();
            }

            for (ModuleEntry impEntry : imports) {

                String imp = impEntry.getName();
                String rev = impEntry.getRevision();

                stmt(IMPORT).arg(imp);
                {
                    stmt(PREFIX).arg(imp).end();

                    if (rev != null) {
                        stmt(REVISION_DATE).arg(rev).end();
                    }
                }
                end();
            }
        }
        return this;
    }

    private String getNamespace() {
        return NS_PREFIX + name;
    }

    protected TestStatementSource arg(String arg) throws SourceException {
        writer.argumentValue(arg, REF);
        return this;
    }

    protected TestStatementSource stmt(Rfc6020Mapping stmt) throws SourceException {
        writer.startStatement(stmt.getStatementName(), REF);
        return this;
    }

    protected TestStatementSource end() throws SourceException {
        writer.endStatement(REF);
        return this;
    }
}
