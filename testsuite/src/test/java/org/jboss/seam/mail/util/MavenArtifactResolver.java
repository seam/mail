/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * A crude resolver that converts a Maven artifact reference
 * into a {@link java.io.File} object.
 * <p/>
 * <p>This approach is an interim solution for Maven projects
 * until the open feature request to add formally add artifacts
 * to a test (ARQ-66) is implementated.</p>
 * <p/>
 * <p>The testCompile goal will resolve any test dependencies and
 * put them in your local Maven repository. By the time the test
 * executes, you can be sure that the JAR files you need will be
 * in your local repository.</p>
 * <p/>
 * <p>Example usage:</p>
 * <p/>
 * <pre>
 * WebArchive war = ShrinkWrap.create("test.war", WebArchive.class)
 *     .addLibrary(MavenArtifactResolver.resolve("commons-lang:commons-lang:2.5"));
 * </pre>
 *
 * @author Dan Allen
 */
public class MavenArtifactResolver {

    public static Collection<JavaArchive> resolve(String qualifiedArtifactId) {
        return DependencyResolvers.use(MavenDependencyResolver.class)
                .configureFrom("../settings.xml")
                .loadMetadataFromPom("pom.xml")
                .artifact(qualifiedArtifactId)
                .resolveAs(JavaArchive.class);
    }

    public static Collection<JavaArchive> resolve(String ... coords) {
        final Set<JavaArchive> libs = new HashSet<JavaArchive>();

        for (String coord : coords) {
            libs.addAll(MavenArtifactResolver.resolve(coord));
        }

        final Iterator<JavaArchive> libIter = libs.iterator();

        while (libIter.hasNext()) {
            final JavaArchive jar = libIter.next();

            if (jar.getName().contains("slf4j-api-1.5.6")) {
                libIter.remove();
            }
        }

        return libs;
    }
}
