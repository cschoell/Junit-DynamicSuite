package org.junit.extensions.dynamicsuite;

import java.lang.annotation.*;

/**
 *  @author Christof Schoell
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DirectoryFilter {
    public Class<? extends TestDirectoryFilter> value();
}
