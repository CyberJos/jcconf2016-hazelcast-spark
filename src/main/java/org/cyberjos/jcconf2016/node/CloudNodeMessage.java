/*
 * @(#)CloudNodeMessage.java 2016/10/13
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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Message transferred among nodes. All nodes should listen its own topic and
 * receive node messages sent from other nodes. We use this class to record the
 * sender, receiver, content and the creation time.
 *
 * @author Joseph S. Kuo
 * @since 0.0.1, 2016/10/13
 */
public class CloudNodeMessage implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4491318090819813435L;

    /**
     * Field: the sender of this message
     */
    private final String sender;

    /**
     * Field: the receiver of this message
     */
    private final String receiver;

    /**
     * Field: the message content
     */
    private final String content;

    /**
     * Field: the creation time
     */
    private final LocalDateTime creationTime;

    /**
     * Constructor with the given builder.
     *
     * @param builder the builder
     */
    @SuppressWarnings("synthetic-access")
    private CloudNodeMessage(final Builder builder) {
        this.sender = builder.sender;
        this.receiver = builder.receiver;
        this.content = builder.content;
        this.creationTime = LocalDateTime.now();
    }

    /**
     * Returns the node name of the sender who sent this message.
     *
     * @return the node name of the sender
     */
    public String getFrom() {
        return this.sender;
    }

    /**
     * Returns the node name of the receiver who receives this message.
     *
     * @return the node name of the receiver
     */
    public String getTo() {
        return this.receiver;
    }

    /**
     * Returns the message content.
     *
     * @return the message content
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Returns the creation time of this message.
     *
     * @return the creation time
     */
    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("From", this.sender)
                .append("To", this.receiver)
                .append("Time", this.creationTime)
                .append("Message", this.content)
                .build();
    }

    /**
     * Creates and returns a new builder to build a node message.
     *
     * @param message the message content to be sent
     * @return a new builder
     * @throws NullPointerException if the given argument is {@code null}
     */
    @SuppressWarnings("synthetic-access")
    public static Builder newMessage(final String message) {
        return new Builder(message);
    }

    /**
     * The builder class for {@link CloudNodeMessage}.
     *
     * @author Joseph S. Kuo
     * @since 0.0.1, 2016/10/13
     */
    public static final class Builder {
        /**
         * Field: the sender.
         */
        private String sender;

        /**
         * Field: the receiver.
         */
        private String receiver;

        /**
         * Field: the message content.
         */
        private final String content;

        /**
         * Constructor with message content.
         *
         * @param theContent the message content
         * @throws NullPointerException if the given argument is {@code null}
         */
        private Builder(final String theContent) {
            Objects.requireNonNull(theContent, "The given content must not be null.");
            this.content = theContent;
        }

        /**
         * Sets the sender and returns this builder.
         *
         * @param theSender the sender
         * @return this builder
         * @throws NullPointerException if the given argument is {@code null}
         */
        public Builder from(final String theSender) {
            Objects.requireNonNull(theSender, "The given sender must not be null.");
            this.sender = theSender;
            return this;
        }

        /**
         * Sets the receiver and returns this builder.
         *
         * @param theReceiver the receiver
         * @return this builder
         * @throws NullPointerException if the given argument is {@code null}
         */
        public Builder to(final String theReceiver) {
            Objects.requireNonNull(theReceiver, "The given receiver must not be null.");
            this.receiver = theReceiver;
            return this;
        }

        /**
         * Creates and returns a new node message with the given settings.
         *
         * @return a new node message
         */
        @SuppressWarnings("synthetic-access")
        public CloudNodeMessage build() {
            return new CloudNodeMessage(this);
        }
    }
}
