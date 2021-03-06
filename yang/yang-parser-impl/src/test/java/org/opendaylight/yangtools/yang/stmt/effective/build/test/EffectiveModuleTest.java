package org.opendaylight.yangtools.yang.stmt.effective.build.test;


import org.junit.BeforeClass;
import org.junit.Test;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.QNameModule;
import org.opendaylight.yangtools.yang.common.SimpleDateFormatUtil;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchema;
import org.opendaylight.yangtools.yang.model.api.Deviation;
import org.opendaylight.yangtools.yang.model.api.ExtensionDefinition;
import org.opendaylight.yangtools.yang.model.api.IdentitySchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.ModuleImport;
import org.opendaylight.yangtools.yang.model.api.NotificationDefinition;
import org.opendaylight.yangtools.yang.model.api.RpcDefinition;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.parser.spi.meta.ReactorException;
import org.opendaylight.yangtools.yang.parser.spi.source.SourceException;
import org.opendaylight.yangtools.yang.parser.stmt.reactor.CrossSourceStatementReactor;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.YangInferencePipeline;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.YangStatementSourceImpl;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.effective.EffectiveSchemaContext;

import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EffectiveModuleTest {

    private static final YangStatementSourceImpl ROOT_MODULE = new YangStatementSourceImpl(
            "/semantic-statement-parser/effective-module/root.yang");
    private static final YangStatementSourceImpl IMPORTED_MODULE = new YangStatementSourceImpl(
            "/semantic-statement-parser/effective-module/imported.yang");
    private static final YangStatementSourceImpl SUBMODULE = new YangStatementSourceImpl(
            "/semantic-statement-parser/effective-module/submod.yang");

    private static final QNameModule ROOT_MODULE_QNAME = QNameModule.create(URI.create("root-ns"), null);

    private static final QName cont = QName.create(ROOT_MODULE_QNAME, "cont");

    private static final SchemaPath contSchemaPath = SchemaPath.create(true, cont);

    private static Date revision;

    @BeforeClass
    public static void init() {
        try {
            revision = SimpleDateFormatUtil.getRevisionFormat()
                    .parse("2000-01-01");
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Test
    public void effectiveBuildTest() throws SourceException, ReactorException {
        CrossSourceStatementReactor.BuildAction reactor = YangInferencePipeline.RFC6020_REACTOR.newBuild();
        addSources(reactor, ROOT_MODULE, IMPORTED_MODULE, SUBMODULE);
        EffectiveSchemaContext result = reactor.buildEffective();

        assertNotNull(result);

        Module rootModule = result.findModuleByName("root", null);
        assertNotNull(rootModule);

        assertEquals("root-pref", rootModule.getPrefix());
        assertEquals("1", rootModule.getYangVersion());
        assertEquals("kisko", rootModule.getOrganization());
        assertEquals("kisko email", rootModule.getContact());

        final Set<AugmentationSchema> augmentations = rootModule.getAugmentations();
        assertEquals(1, augmentations.size());
        assertEquals(contSchemaPath, augmentations.iterator().next().getTargetPath());

        final Set<ModuleImport> imports = rootModule.getImports();
        assertEquals(1, imports.size());
        final ModuleImport importStmt = imports.iterator().next();
        assertNotNull(importStmt);
        assertEquals("imported", importStmt.getModuleName());
        assertEquals(revision, importStmt.getRevision());
        assertEquals("imp-pref", importStmt.getPrefix());

        final Set<Module> submodules = rootModule.getSubmodules();
        //assertEquals(1, submodules.size());
        //assertEquals("submod", submodules.iterator().next().getName());

        final Set<NotificationDefinition> notifications = rootModule.getNotifications();
        assertEquals(1, notifications.size());
        assertEquals("notif1", notifications.iterator().next().getQName().getLocalName());

        final Set<RpcDefinition> rpcs = rootModule.getRpcs();
        assertEquals(1, rpcs.size());
        assertEquals("rpc1", rpcs.iterator().next().getQName().getLocalName());

        final Set<Deviation> deviations = rootModule.getDeviations();
        assertEquals(1, deviations.size());
        final Deviation deviationStmt = deviations.iterator().next();
        assertNotNull(deviationStmt);
        assertEquals(contSchemaPath, deviationStmt.getTargetPath());
        assertEquals(Deviation.Deviate.ADD, deviationStmt.getDeviate());
        assertEquals("deviate reference", deviationStmt.getReference());

        final Set<IdentitySchemaNode> identities = rootModule.getIdentities();
        assertEquals(1, identities.size());
        assertEquals("identity1", identities.iterator().next().getQName().getLocalName());

//        final Set<FeatureDefinition> features = rootModule.getFeatures();
//        assertEquals(1, features.size());
//        assertEquals("feature1", features.iterator().next().getQName().getLocalName());

        final List<ExtensionDefinition> extensionSchemaNodes = rootModule.getExtensionSchemaNodes();
        assertEquals(1, extensionSchemaNodes.size());
        assertEquals("ext1", extensionSchemaNodes.iterator().next().getQName().getLocalName());
    }

    private void addSources(CrossSourceStatementReactor.BuildAction reactor, YangStatementSourceImpl... sources) {
        for (YangStatementSourceImpl source : sources) {
            reactor.addSource(source);
        }
    }
}
