package com.slickqa.junit;

/**
 * An instance of this class is used to fetch configuration for slick from a source.  This allows multiple sources
 * to be used.
 */
public interface SlickConfigurationSource {
    String getConfigurationEntry(String name);
    String getConfigurationEntry(String name, String defaultValue);
}
