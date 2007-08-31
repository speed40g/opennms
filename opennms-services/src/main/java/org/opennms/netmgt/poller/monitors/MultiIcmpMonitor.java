//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2007 The OpenNMS Group, Inc.  All rights reserved.
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
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
// Tab Size = 8
//

package org.opennms.netmgt.poller.monitors;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.opennms.core.utils.CollectionMath;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.ping.Pinger;
import org.opennms.netmgt.poller.Distributable;
import org.opennms.netmgt.poller.DistributionContext;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.poller.NetworkInterface;
import org.opennms.netmgt.poller.NetworkInterfaceNotSupportedException;
import org.opennms.netmgt.utils.ParameterMap;

/**
 * <P>
 * This class is designed to be used by the service poller framework to test the
 * availability of the ICMP service on remote interfaces. The class implements
 * the ServiceMonitor interface that allows it to be used along with other
 * plug-ins by the service poller framework.
 * </P>
 * 
 * @author <A HREF="mailto:tarus@opennms.org">Tarus Balog</A>
 * @author <A HREF="mailto:ranger@opennms.org">Benjamin Reed</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 * 
 */

// this is marked not distributable because it relies on a shared library
@Distributable(DistributionContext.DAEMON)
final public class MultiIcmpMonitor extends IPv4Monitor {
    private static final int DEFAULT_MULTI_PING_COUNT = 20;
    private static final long DEFAULT_PING_INTERVAL = 50;

    /**
     * Constructs a new monitor.
     */
    public MultiIcmpMonitor() throws IOException {
    }

    /**
     * <P>
     * Poll the specified address for ICMP service availability.
     * </P>
     * 
     * <P>
     * The ICMP service monitor relies on Discovery for the actual generation of
     * IMCP 'ping' requests. A JSDT session with two channels (send/receive) is
     * utilized for passing poll requests and receiving poll replies from
     * discovery. All exchanges are SOAP/XML compliant.
     * </P>
     * 
     * @param parameters
     *            The package parameters (timeout, retry, etc...) to be used for
     *            this poll.
     * @param iface
     *            The network interface to test the service on.
     * @return The availability of the interface and if a transition event
     *         should be suppressed.
     * 
     */
    public PollStatus poll(MonitoredService svc, Map parameters) {
        NetworkInterface iface = svc.getNetInterface();

        // Get interface address from NetworkInterface
        //
        if (iface.getType() != NetworkInterface.TYPE_IPV4)
            throw new NetworkInterfaceNotSupportedException("Unsupported interface type, only TYPE_IPV4 currently supported");

        Category log = ThreadCategory.getInstance(this.getClass());
        PollStatus serviceStatus = PollStatus.unavailable();
        InetAddress host = (InetAddress) iface.getAddress();
        List<Number> responseTimes = null;

        try {

            // get parameters
            //
            long timeout = ParameterMap.getKeyedLong(parameters, "timeout", Pinger.DEFAULT_TIMEOUT);
            int count = ParameterMap.getKeyedInteger(parameters, "ping-count", DEFAULT_MULTI_PING_COUNT);
            long pingInterval = ParameterMap.getKeyedLong(parameters, "wait-interval", DEFAULT_PING_INTERVAL);

            responseTimes = new ArrayList<Number>(Pinger.parallelPing(host, count, timeout, pingInterval));

            if (CollectionMath.countNotNull(responseTimes) == 0) {
                return PollStatus.unavailable("no packets returned within the timeout");
            }

            serviceStatus = PollStatus.available();
            Collections.sort(responseTimes, new Comparator<Number>() {

                public int compare(Number arg0, Number arg1) {
                    if (arg0 == null) {
                        return -1;
                    } else if (arg1 == null) {
                        return 1;
                    } else if (arg0.doubleValue() == arg1.doubleValue()) {
                        return 0;
                    } else {
                        return arg0.doubleValue() < arg1.doubleValue() ? -1 : 1;
                    }
                }

            });

            Map<String, Number> returnval = new LinkedHashMap<String, Number>();
            for (int i = 0; i < responseTimes.size(); i++) {
                returnval.put("ping" + (i + 1), responseTimes.get(i));
            }
            returnval.put("loss", CollectionMath.countNull(responseTimes));
            returnval.put("median", CollectionMath.median(responseTimes));
            returnval.put("response-time", CollectionMath.average(responseTimes));

            serviceStatus.setProperties(returnval);
        } catch (Exception e) {
            log.debug("failed to ping " + host, e);
        }

        return serviceStatus;
    }

}
