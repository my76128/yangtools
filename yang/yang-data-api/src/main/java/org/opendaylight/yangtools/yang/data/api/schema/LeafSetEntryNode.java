/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.api.schema;

import org.opendaylight.yangtools.yang.data.api.AttributesContainer;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeWithValue;

/**
 *
 * Leaf node with multiplicity 0...n
 *
 * Leaf node has a value, but no child nodes in the data tree, schema
 * for leaf node and its value is described by
 * {@link org.opendaylight.yangtools.yang.model.api.LeafListSchemaNode}.
 *
 * @param <T>
 *            Value type
 */
public interface LeafSetEntryNode<T> extends AttributesContainer, NormalizedNode<NodeWithValue, T> {

    /**
     * Returns {@link NodeWithValue} which identifies this leaf set entry.
     *
     * Returned {@link NodeWithValue} contains same value as this node.
     *
     * <h3>Implementation notes</h3> Invocation of
     * {@link NodeWithValue#getValue()} on returned instance of
     * {@link NodeWithValue} must returns
     * same value as invocation of {@link #getValue()}, such as
     * following condition is allways met:
     * <code>true == this.getIdentifier().getValue().equals(this.getValue())</code>
     *
     *
     * @return {@link NodeWithValue} which identifies this leaf set entry.
     */
    @Override
    NodeWithValue getIdentifier();

    /**
     *
     * Returns value of this leaf node
     *
     * <h3>Implementation notes</h3> Invocation of {@link #getValue()} must
     * provides same value as value in {@link #getIdentifier()}.
     * <code>true == this.getIdentifier().getValue().equals(this.getValue())</code>
     *
     * @return Returned value of this leaf node. Value SHOULD meet criteria
     *         defined by schema.
     *
     */
    @Override
    T getValue();
}
