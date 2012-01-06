/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.mail.util;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Creates base deployments with all the required dependencies for the current container
 *
 * @author Marek Schmidt
 *
 */
public class Deployments {
    public static WebArchive baseDeployment() {

        WebArchive archive = ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addAsWebInfResource("beans.xml", "beans.xml");
        if ("weld-ee-embedded-1.1".equals(System.getProperty("arquillian"))) {
            archive.addAsResource("seam-beans.xml", "META-INF/seam-beans.xml");
            archive.addAsLibraries(MavenArtifactResolver.resolve("org.jboss.solder:solder-impl"));
        }
        else {
            archive.addAsResource("seam-beans.xml", "META-INF/seam-beans.xml");
            archive.addAsLibraries(MavenArtifactResolver.resolve("org.subethamail:subethasmtp",
                    "org.jboss.solder:solder-impl", "org.slf4j:slf4j-simple", "com.google.guava:guava"));
        }

        return archive;
    }

    public static WebArchive baseVelocityDeployment() {

        WebArchive archive = ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addAsWebInfResource("beans.xml", "beans.xml");
        if ("weld-ee-embedded-1.1".equals(System.getProperty("arquillian"))) {
            archive.addAsResource("seam-beans.xml", "META-INF/seam-beans.xml");
            archive.addAsLibraries(MavenArtifactResolver.resolve("org.jboss.solder:solder-impl"));
        }
        else {
            archive.addAsResource("seam-beans.xml", "META-INF/seam-beans.xml");
            archive.addAsLibraries(MavenArtifactResolver.resolve("org.subethamail:subethasmtp",
                    "org.apache.velocity:velocity", "org.jboss.solder:solder-impl", "org.slf4j:slf4j-simple", "com.google.guava:guava"));
        }

        return archive;
    }

    public static WebArchive baseFreeMarkerDeployment() {

        WebArchive archive = ShrinkWrap
            .create(WebArchive.class, "test.war")
            .addAsWebInfResource("beans.xml", "beans.xml");
        if ("weld-ee-embedded-1.1".equals(System.getProperty("arquillian"))) {
            archive.addAsResource("seam-beans.xml", "META-INF/seam-beans.xml");
            archive.addAsLibraries(MavenArtifactResolver.resolve("org.jboss.solder:solder-impl"));
        }
        else {
            archive.addAsResource("seam-beans.xml", "META-INF/seam-beans.xml");
            archive.addAsLibraries(MavenArtifactResolver.resolve("org.subethamail:subethasmtp",
                    "org.freemarker:freemarker", "org.jboss.solder:solder-impl", "org.slf4j:slf4j-simple", "com.google.guava:guava"));
        }

        return archive;
    }
}
