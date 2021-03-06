/*
 * Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yangtools.yang.data.impl.test.codecs;

import static org.junit.Assert.*;
import static org.opendaylight.yangtools.yang.data.impl.test.codecs.TypeDefinitionAwareCodecTestHelper.deserializeWithExpectedIllegalArgEx;
import static org.opendaylight.yangtools.yang.data.impl.test.codecs.TypeDefinitionAwareCodecTestHelper.getCodec;

import org.junit.Test;
import org.opendaylight.yangtools.yang.data.api.codec.Int16Codec;
import org.opendaylight.yangtools.yang.model.util.Int16;

/**
 * Unit tests for Int8CodecString.
 *
 * @author Thomas Pantelis
 */
public class Int16CodecStringTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialize() {
        Int16Codec<String> codec = getCodec(Int16.getInstance(), Int16Codec.class);

        assertEquals("serialize", "10", codec.serialize(Short.valueOf( (short) 10 )));
        assertEquals("serialize", "", codec.serialize(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize() {
        final String hexa = "+0X45c";
        final String negHexa = "-0X45c";
        final String octal = "02134";
        final String negOctal = "-02134";
        final String integer = "+1116";
        final String negInteger = "-1116";

        Int16Codec<String> codec = getCodec(Int16.getInstance(), Int16Codec.class);

        assertEquals("deserialize", codec.deserialize(hexa), Short.valueOf("+045c", 16));
        assertEquals("deserialize", codec.deserialize(negHexa), Short.valueOf("-045c", 16));
        assertEquals("deserialize", codec.deserialize(octal), Short.valueOf(octal, 8));
        assertEquals("deserialize", codec.deserialize(negOctal), Short.valueOf(negOctal, 8));
        assertEquals("deserialize", codec.deserialize(integer), Short.valueOf(integer, 10));
        assertEquals("deserialize", codec.deserialize(negInteger), Short.valueOf(negInteger, 10));

        deserializeWithExpectedIllegalArgEx(codec, "1o");
        deserializeWithExpectedIllegalArgEx(codec, "");
        deserializeWithExpectedIllegalArgEx(codec, null);
    }
}
