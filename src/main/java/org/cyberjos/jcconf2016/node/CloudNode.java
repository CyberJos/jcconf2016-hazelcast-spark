/*
 * @(#)CloudNode.java 2016/10/13
 *
 * Copyright (c) 2016 Joseph S. Kuo
 * All Rights Reserved.
 *
 * --LICENSE NOTICE--
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * --LICENSE NOTICE--
 */
package org.cyberjos.jcconf2016.node;

import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import com.hazelcast.core.MessageListener;

/**
 * The cloud node interface. A node has the following statuses:
 * <ul>
 * <li>waiting</li>
 * <li>active</li>
 * <li>stopped</li>
 * </ul>
 * <p>
 * The change path of statuses is the following:
 * </p>
 *
 * <pre>
 * (Node initialized)
 *          |
 *          v
 *       waiting  &lt;--------
 *          |             |
 *          v             |
 *     (activate)         |
 *          |             |
 *          v             |
 *      activate --&gt; (deactivate)
 *          |
 *          v
 *     (shutdown)
 *          |
 *          v
 *       stopped
 * </pre>
 *
 * @author Joseph S. Kuo
 * @since 0.0.1, 2016/10/13
 */
public interface CloudNode extends MessageListener<CloudNodeMessage>, MembershipListener {
    /**
     * Activates this node to accept tasks. Every node joins a cluster while
     * launching, but it will be available to work only after activating.
     */
    public void activate();

    /**
     * Deactivates this node. It removes this node from the active list of
     * cluster.
     */
    public void deactivate();

    /**
     * Sends the given content to the specified node.
     *
     * @param toNode the name of the node who receives the given content
     * @param content the content to be sent
     * @throws NullPointerException if any of the given argument is {@code null}
     */
    public void send(String toNode, String content);

    /**
     * Returns the node name.
     *
     * @return the node name
     */
    public String getName();

    /**
     * {@inheritDoc}
     */
    @Override
    default public void memberAdded(@SuppressWarnings("unused") final MembershipEvent membershipEvent) {
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default public void memberRemoved(@SuppressWarnings("unused") final MembershipEvent membershipEvent) {
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default public void memberAttributeChanged(@SuppressWarnings("unused") final MemberAttributeEvent memberAttributeEvent) {
        // Do nothing.
    }
}
