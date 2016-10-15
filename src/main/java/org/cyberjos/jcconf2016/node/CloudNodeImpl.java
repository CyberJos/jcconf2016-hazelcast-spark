/*
 * @(#)CloudNodeImpl.java 2016/10/13
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

import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.Message;

/**
 * The implementation of {@link CloudNode}.
 *
 * @author Joseph S. Kuo
 * @since 0.0.1, 2016/10/13
 */
@Component
public class CloudNodeImpl implements CloudNode {
    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(CloudNodeImpl.class);

    /**
     * The node name.
     */
    private final String nodeName;

    /**
     * The Hazelcast facade.
     */
    @Autowired
    private HazelcastHelper hazelcastHelper;

    /**
     * Constructor.
     */
    public CloudNodeImpl() {
        this.nodeName = "Node-" + LocalDateTime.now().format(HazelcastHelper.NAME_FORMATTER);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("[{}] Shutdown hook is invoked.", this.nodeName);
            this.deactivate();
            try {
                Thread.currentThread().join();
            } catch (final Exception ex) {
                logger.warn(String.format("[%s] An exception occurs druing shutdown.", this.nodeName), ex);
            }
            Hazelcast.shutdownAll();
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostConstruct
    public void activate() {
        this.hazelcastHelper.registerNode(this);

        logger.info("[{}] Activated successfully.", this.nodeName);

        if (logger.isDebugEnabled()) {
            logger.debug("[{}] Online active node list: ", this.nodeName);
            this.hazelcastHelper.getActiveNodes()
                    .stream()
                    .forEach(name -> logger.debug("==> Found: {}", name));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreDestroy
    public void deactivate() {
        this.hazelcastHelper.unregisterNode(this.nodeName);

        // Release resources here if necessary.

        logger.info("[{}] Deactivated successfully.", this.nodeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final String toNode, final String content) {
        final CloudNodeMessage message = CloudNodeMessage.newMessage(content)
                .from(this.nodeName)
                .to(toNode)
                .build();

        this.hazelcastHelper.send(message);
        logger.info("[{}] Sent message to [{}]: {}", this.nodeName, toNode, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(final Message<CloudNodeMessage> message) {
        final CloudNodeMessage nodeMessage = message.getMessageObject();

        logger.info("[{}] Incoming message: {}", this.nodeName, nodeMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.nodeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void memberRemoved(final MembershipEvent membershipEvent) {
        final String removedId = membershipEvent.getMember().getUuid();

        logger.info("[{}] Found a node removed from cluster: {}", this.nodeName, removedId);

        final Optional<NodeRecord> masterNode = this.hazelcastHelper.getMasterNodeRecord();
        if (!masterNode.isPresent() || StringUtils.equals(removedId, masterNode.get().getMemberId())) {
            logger.info("[{}] The master is removed. Trying to be the master node.", this.nodeName);
            this.hazelcastHelper.setMaster(this);
        } else {
            logger.info("[{}] The current master node: {}", this.nodeName, masterNode.get());
        }
    }
}
