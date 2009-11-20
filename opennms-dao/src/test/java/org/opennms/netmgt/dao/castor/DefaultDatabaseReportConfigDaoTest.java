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
// Modifications:
// 
// Created: October 5th, 2009 jonathan@opennms.org
//
// Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
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
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
package org.opennms.netmgt.dao.castor;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;
import org.opennms.netmgt.config.databaseReports.DateOffset;
import org.opennms.netmgt.config.databaseReports.ReportParm;
import org.opennms.test.ConfigurationTestUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

public class DefaultDatabaseReportConfigDaoTest {
    
    private static final String NAME = "defaultCalendarReport";
    private static final String DESCRIPTION = "default calendar report";
    private static final String DATE_DISPLAY_NAME = "end date";
    private static final String DATE_NAME = "endDate";
    

    @Test
    public void testGetParmsByName() throws Exception {
        
        DefaultDatabaseReportConfigDao dao = new DefaultDatabaseReportConfigDao();
        Resource resource = new ClassPathResource("/database-reports-testdata.xml");
        dao.setConfigResource(resource);
        dao.afterPropertiesSet();
        
        ReportParm[] dates = dao.getDates(NAME);
        
        assertEquals(dates.length,1);
        assertEquals(dates[0].getName(),DATE_NAME);
        assertEquals(dates[0].getDisplayName(),DATE_DISPLAY_NAME);
        
        DateOffset[] offset = dao.getDateOffests(NAME);
        assertEquals(offset.length,1);
        assertEquals(offset[0].getName(),DATE_NAME);
        assertEquals(offset[0].getDisplayName(),DATE_DISPLAY_NAME);
        assertEquals(offset[0].getDefaultCount(),1);
        assertEquals(offset[0].getDefaultInterval(),"day");
        
        
        
    }

}
