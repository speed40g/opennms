/*******************************************************************************
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *     along with OpenNMS(R).  If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information contact: 
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.netmgt.dao.castor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.siteStatusViews.View;
import org.opennms.netmgt.config.siteStatusViews.Views;
import org.opennms.netmgt.dao.SiteStatusViewConfigDao;
import org.springframework.dao.DataRetrievalFailureException;

/**
 * <p>DefaultSiteStatusViewConfigDao class.</p>
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class DefaultSiteStatusViewConfigDao implements SiteStatusViewConfigDao {
    
    static {
        try {
            SiteStatusViewsFactory.init();
        } catch (MarshalException e) {
            throw new DataRetrievalFailureException("Syntax error in site status view config file", e);
        } catch (ValidationException e) {
            throw new DataRetrievalFailureException("Syntax error in site status view config file", e);
        } catch (FileNotFoundException e) {
            throw new DataRetrievalFailureException("Unable to locate site status view config file", e);
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Error load site status view config file", e);
        }
    }
    
    /**
     * <p>Constructor for DefaultSiteStatusViewConfigDao.</p>
     */
    public DefaultSiteStatusViewConfigDao() {
    }

    /** {@inheritDoc} */
    public View getView(String viewName) {
        try {
            return SiteStatusViewsFactory.getInstance().getView(viewName);
        } catch (MarshalException e) {
            throw new DataRetrievalFailureException("Syntax error in site status view config file", e);
        } catch (ValidationException e) {
            throw new DataRetrievalFailureException("Syntax error in site status view config file", e);
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Error load site status view config file", e);
        }
    }

    /**
     * Use this method to get the generated Views class generated by the XSD.
     *
     * @return a {@link org.opennms.netmgt.config.siteStatusViews.Views} object.
     */
    public Views getViews() {
        return SiteStatusViewsFactory.getConfig().getViews();
    }
    
    /**
     * Use this method to get a Map of view names to marshalled classes based on the generated View class
     * from the XSD.
     *
     * @return <code>Map</> of View classes.
     */
    public Map<String, View> getViewMap() {
        return SiteStatusViewsFactory.getViewsMap();
    }

    /**
     * <p>getDefaultView</p>
     *
     * @return a {@link org.opennms.netmgt.config.siteStatusViews.View} object.
     */
    public View getDefaultView() {
        final String defaultView = SiteStatusViewsFactory.getConfig().getDefaultView();
        return getView(defaultView);
    }

}
