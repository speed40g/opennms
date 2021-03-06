/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.threshd;

import java.io.File;
import java.util.Date;

import org.opennms.netmgt.collection.api.CollectionResource;
import org.opennms.netmgt.collection.api.CollectionSetVisitor;
import org.opennms.netmgt.collection.api.ServiceParameters;
import org.opennms.netmgt.collection.api.TimeKeeper;
import org.opennms.netmgt.rrd.RrdRepository;

public class MockCollectionResourceWrapper extends CollectionResourceWrapper {

    public MockCollectionResourceWrapper(final String instance) {
        super(new Date(), 0, null, null, null, new CollectionResource() {
            @Override
            public String getInstance() {
                return instance;
            }
            @Override
            public String getInterfaceLabel() {
                return null;
            }
            @Override
            public String getResourceTypeName() {
                return null;
            }
            @Override
            public boolean rescanNeeded() {
                return false;
            }
            @Override
            public boolean shouldPersist(ServiceParameters params) {
                return false;
            }
            @Override
            public void visit(CollectionSetVisitor visitor) {
            }
            @Override
            public String getOwnerName() {
                return null;
            }
            @Override
            public File getResourceDir(RrdRepository repository) {
                return null;
            }
            @Override
            public String getParent() {
                return null;
            }
            @Override
            public TimeKeeper getTimeKeeper() {
                return null;
            }
        }, null);
    }

}
