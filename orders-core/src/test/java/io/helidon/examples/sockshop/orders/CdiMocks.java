/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.orders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.WithAnnotations;

import lombok.extern.java.Log;

/**
 * @RegisterRestClient annotation is processed by Jersey to create a Bean that implements
 * the given interface. Adding test instances that mock interaction with the service causes
 * ambiguity. This Extension finds InjectionPoints and modifies the set of Qualifiers, so
 * the one annotated with @Mock is selected by CDI.
 */
@Log
public class CdiMocks implements Extension {
   protected Set annotated = new HashSet();

   public void collectCdiMocks(@Observes
                               @WithAnnotations({Mock.class})
                               ProcessAnnotatedType<?> pat) {
       Class<?> clazz = pat.getAnnotatedType().getJavaClass();
       if (clazz.getInterfaces().length == 0) {
          log.warning("CdiMocks: FOUND ANNOTATED CLASS, BUT IT DOES NOT IMPLEMENT ANYTHING - WILL NOT MOCK " + clazz);
       }

       for(Class<?> iface: clazz.getInterfaces()) {
          log.info("CdiMocks: FOUND MOCK FOR: " + iface);
          annotated.add(iface);
       }
   }

   public void injectMockQualifier(@Observes ProcessInjectionPoint pip) {
       InjectionPoint ip = pip.getInjectionPoint();
       Type t = ip.getAnnotated().getBaseType();
       if (!annotated.contains(t)) {
           return;
       }

       log.info("CdiMocks: INJECTING MOCK AT: " + ip);
       pip.setInjectionPoint(new InjectionPoint() {
           public Annotated getAnnotated() {
               return ip.getAnnotated();
           }

           public Bean<?> getBean() {
               return ip.getBean();
           }

           public Member getMember() {
               return ip.getMember();
           }

           public Set<Annotation> getQualifiers() {
               Set<Annotation> s = new HashSet<>();
               s.add(Mock.INSTANCE);
               return s;
           }

           public Type getType() {
               return ip.getType();
           }

           public boolean isDelegate() {
               return ip.isDelegate();
           }

           public boolean isTransient() {
               return ip.isTransient();
           }
       });
   }
}
