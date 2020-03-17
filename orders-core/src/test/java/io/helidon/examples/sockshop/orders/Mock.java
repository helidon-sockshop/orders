package io.helidon.examples.sockshop.orders;

import java.lang.annotation.Annotation;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Qualifier
@Retention(RUNTIME)
public @interface Mock {
    public static Mock INSTANCE = new Mock(){
        @Override
        public Class<? extends Annotation> annotationType() {
            return Mock.class;
        }
    };
}
