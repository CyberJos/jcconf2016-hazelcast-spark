/*
 * @(#)HazelcastNodeMain.java 2016/10/13
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
package org.cyberjos.jcconf2016;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cyberjos.jcconf2016.node.CloudNode;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Node program entry point.
 *
 * @author Joseph S. Kuo
 * @since 0.0.1, 2016/10/13
 */
public class HazelcastNodeMain {
    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(HazelcastNodeMain.class);

    /**
     * Application context.
     */
    private static ConfigurableApplicationContext context;

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        logger.info("Starting to launch application...");

        context = new AnnotationConfigApplicationContext(Application.class);
        final CloudNode cloudNode = context.getBean(CloudNode.class);

        context.registerShutdownHook();
        if (cloudNode != null) {
            context.registerShutdownHook();
            logger.info("[{}] The node has been started.", cloudNode.getName());
        } else {
            logger.warn("There is NO node running!! Please check the argument.");
        }
    }
}
