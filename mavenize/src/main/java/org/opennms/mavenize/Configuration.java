//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
package org.opennms.mavenize;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.codehaus.plexus.util.StringUtils;

public class Configuration {
    
    private static Configuration s_configuration;

    private Properties m_properties = new Properties();

    public Configuration(String resource) {
        try {
            InputStream in = getClass().getResourceAsStream(resource);
            m_properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load "+resource, e);
        }
    }

    public String getString(String key) {
        return m_properties.getProperty(key);
    }

    public List getList(String key) {
        String val = m_properties.getProperty(key);
        if (val == null) return Collections.EMPTY_LIST;
        
        return Arrays.asList(StringUtils.stripAll(StringUtils.split(val, ",")));
    }

    public static Configuration get() {
        if (s_configuration == null) {
            Configuration config = new Configuration("/configuration.properties");
            s_configuration = config;
        }
        return s_configuration;
    }

    public Class getClass(String key, Class dflt) {
        String className = getString(key);
        if (className == null) return dflt;
        
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load class "+className, e);
        }
    }

}
