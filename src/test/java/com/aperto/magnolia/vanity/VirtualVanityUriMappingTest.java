package com.aperto.magnolia.vanity;

/*
 * #%L
 * magnolia-vanity-url Magnolia Module
 * %%
 * Copyright (C) 2013 - 2014 Aperto AG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import info.magnolia.cms.beans.config.VirtualURIMapping;
import info.magnolia.cms.core.AggregationState;
import info.magnolia.context.ContextFactory;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.SystemContext;
import info.magnolia.context.WebContext;
import info.magnolia.module.ModuleRegistry;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.objectfactory.Components;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for mapping.
 *
 * @author frank.sommer
 * @since 28.05.14
 */
public class VirtualVanityUriMappingTest {

    private VirtualVanityUriMapping _uriMapping;

    @Test
    public void testRootRequest() {
        VirtualURIMapping.MappingResult mappingResult = _uriMapping.mapURI("/");
        assertThat(mappingResult, nullValue());
    }

    @Test
    public void testPageRequest() {
        VirtualURIMapping.MappingResult mappingResult = _uriMapping.mapURI("/home.html");
        assertThat(mappingResult, nullValue());
    }

    @Test
    public void testVanityUrlWithoutTarget() {
        VirtualURIMapping.MappingResult mappingResult = _uriMapping.mapURI("/home");
        assertThat(mappingResult, nullValue());
    }

 

    @Before
    public void setUp() throws Exception {
        _uriMapping = new VirtualVanityUriMapping();

        VanityUrlModule module = new VanityUrlModule();
        Map<String, String> excludes = new HashMap<String, String>();
        excludes.put("pages", ".*\\..*");
        module.setExcludes(excludes);
        _uriMapping.setVanityUrlModule(module);

        ModuleRegistry moduleRegistry = mock(ModuleRegistry.class);
        _uriMapping.setModuleRegistry(moduleRegistry);

        initWebContext();

        initComponentProvider();
    }

    private void initComponentProvider() {
        ComponentProvider componentProvider = mock(ComponentProvider.class);
        ContextFactory contextFactory = mock(ContextFactory.class);
        SystemContext systemContext = mock(SystemContext.class);
        when(contextFactory.getSystemContext()).thenReturn(systemContext);
        when(componentProvider.getComponent(ContextFactory.class)).thenReturn(contextFactory);
        Components.setComponentProvider(componentProvider);
    }

    private void initWebContext() {
        WebContext webContext = mock(WebContext.class);
        AggregationState aggregationState = mock(AggregationState.class);
        when(webContext.getAggregationState()).thenReturn(aggregationState);
        MgnlContext.setInstance(webContext);
    }

    @After
    public void tearDown() throws Exception {
        MgnlContext.setInstance(null);
        Components.setComponentProvider(null);
    }
}
