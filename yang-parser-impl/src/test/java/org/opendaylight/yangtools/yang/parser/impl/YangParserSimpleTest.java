/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AnyXmlSchemaNode;
import org.opendaylight.yangtools.yang.model.api.ConstraintDefinition;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.MustDefinition;
import org.opendaylight.yangtools.yang.model.api.Status;

public class YangParserSimpleTest {

    private final URI snNS = URI.create("urn:opendaylight:simple-nodes");
    private Date snRev;

    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Set<Module> modules;

    @Before
    public void init() throws FileNotFoundException, ParseException {
        snRev = simpleDateFormat.parse("2013-07-30");
        modules = TestUtils.loadModules(getClass().getResource("/simple-test").getPath());
    }

    @Test
    public void testAnyXml() {
        Module testModule = TestUtils.findModule(modules, "simple-nodes");
        AnyXmlSchemaNode data = (AnyXmlSchemaNode) testModule.getDataChildByName("data");
        assertNotNull("'anyxml data not found'", data);

        // test SchemaNode args
        QName qname = data.getQName();
        assertEquals("data", qname.getLocalName());
        assertEquals("sn", qname.getPrefix());
        assertEquals(snNS, qname.getNamespace());
        assertEquals(snRev, qname.getRevision());
        assertEquals("anyxml desc", data.getDescription());
        assertEquals("data ref", data.getReference());
        assertEquals(Status.OBSOLETE, data.getStatus());
        assertEquals(0, data.getUnknownSchemaNodes().size());
        // test DataSchemaNode args
        assertFalse(data.isAugmenting());
        assertFalse(data.isConfiguration());
        ConstraintDefinition constraints = data.getConstraints();
        assertEquals("class != 'wheel'", constraints.getWhenCondition().toString());
        Set<MustDefinition> mustConstraints = constraints.getMustConstraints();
        assertEquals(2, constraints.getMustConstraints().size());

        String must1 = "\"ifType != 'ethernet' or (ifType = 'ethernet' and ifMTU = 1500)\"";
        String errMsg1 = "An ethernet MTU must be 1500";
        String must2 = "\"ifType != 'atm' or (ifType = 'atm' and ifMTU <= 17966 and ifMTU >= 64)\"";
        String errMsg2 = "An atm MTU must be  64 .. 17966";

        boolean found1 = false;
        boolean found2 = false;

        for (MustDefinition must : mustConstraints) {
            System.out.println(must);
            if (must1.equals(must.toString())) {
                found1 = true;
                assertEquals(errMsg1, must.getErrorMessage());
            } else if (must2.equals(must.toString())) {
                found2 = true;
                assertEquals(errMsg2, must.getErrorMessage());
                assertEquals("anyxml data error-app-tag", must.getErrorAppTag());
                assertEquals("an error occured in data", must.getDescription());
                assertEquals("data must ref", must.getReference());
            }
        }
        assertTrue(found1);
        assertTrue(found2);

        assertTrue(constraints.isMandatory());
        assertNull(constraints.getMinElements());
        assertNull(constraints.getMaxElements());
    }

}