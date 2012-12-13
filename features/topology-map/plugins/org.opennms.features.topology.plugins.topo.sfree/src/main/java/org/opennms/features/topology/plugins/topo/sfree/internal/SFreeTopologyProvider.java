package org.opennms.features.topology.plugins.topo.sfree.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.opennms.features.topology.api.EditableGraphProvider;
import org.opennms.features.topology.api.SimpleEdge;
import org.opennms.features.topology.api.SimpleVertexContainer;
import org.opennms.features.topology.api.topo.Vertex;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;

public class SFreeTopologyProvider implements EditableGraphProvider {

	private static final String TOPOLOGY_NAMESPACE_SFREE = "sfree";
	public static final String ERDOS_RENIS = "ErdosReniy";
	public static final String BARABASI_ALBERT = "BarabasiAlbert";
    private final SimpleVertexContainer m_vertexContainer;
    private final BeanContainer<String, SimpleEdge> m_edgeContainer;

    public SFreeTopologyProvider() {
        m_vertexContainer = new SimpleVertexContainer();
        m_edgeContainer = new BeanContainer<String, SimpleEdge>(SimpleEdge.class);
        m_edgeContainer.setBeanIdProperty("id");
    }

	@Override
	public void setParent(Object vertexId, Object parentId) {
		m_vertexContainer.setParent(vertexId, parentId);
	}

	@Override
	public Vertex addGroup(String groupName, String groupIcon) {
		return null;
	}

	@Override
	public void save(String filename) {
	}

	@Override
	public void load(String filename) {
		
		m_vertexContainer.removeAllItems();
		m_edgeContainer.removeAllItems();

		if (filename.equals(ERDOS_RENIS))
			createERRandomTopology(100,3);		
		else if (filename.equals(BARABASI_ALBERT))
			createBARandomTopology(100,3);
	}

	private void createBARandomTopology(Integer numberOfNodes, Integer averageNumberofNeighboors) {
		Map<Integer,SimpleLeafVertex> nodes = new HashMap<Integer, SimpleLeafVertex>();
		List<SimpleEdge> edges = new ArrayList<SimpleEdge>();

		for(int i=0; i<2*averageNumberofNeighboors; i++){
			int j=(i+1)%(2*averageNumberofNeighboors);
			SimpleLeafVertex vertexi = new SimpleLeafVertex(Integer.toString(i),0,0);
			vertexi.setIconKey("sfree:system");
			vertexi.setLabel("BarabasiAlbertNode"+i);
			SimpleLeafVertex vertexj = new SimpleLeafVertex(Integer.toString(j),0,0);
			vertexj.setIconKey("sfree:system");
			vertexj.setLabel("BarabasiAlbertNode"+j);
			nodes.put(i, vertexi);
			nodes.put(j, vertexj);
			System.err.println("Creating First Cluster: " + i);
			System.err.println("Creating First Cluster: " + j);
			edges.add(new SimpleEdge(TOPOLOGY_NAMESPACE_SFREE, "link:"+i+"-"+j, nodes.get(i), nodes.get(j)));
		}
		
		Random r = new Random((new Date()).getTime());
		for(int i=2*averageNumberofNeighboors;i<numberOfNodes;i++){
			SimpleLeafVertex vertexi = new SimpleLeafVertex(Integer.toString(i),0,0);
			vertexi.setIconKey("sfree:system");
			vertexi.setLabel("BarabasiAlbertNode"+i);
			nodes.put(i, vertexi);
			System.err.println("Adding Node: " + i);
			for(int times=0; times<averageNumberofNeighboors; times++){
				SimpleEdge edge;
				double d = r.nextDouble()*nodes.size(); // choose node to attach to
				System.err.println("Generated random position: " + d);
				Long j = (long)d;
				System.err.println("Try Adding edge: " + j + "--->" + i);
				edge = new SimpleEdge(TOPOLOGY_NAMESPACE_SFREE, "link:"+i+"-"+j, nodes.get(i), nodes.get(j.intValue()));
				if( i==j.intValue() ) continue;
				edges.add(edge);
			}// m links added
		}

		m_vertexContainer.addAll(nodes.values());
		m_edgeContainer.addAll(edges);

	}

	private void createERRandomTopology(Integer numberOfNodes, Integer averageNumberofNeighboors) {
		Map<Integer,SimpleLeafVertex> nodes = new HashMap<Integer, SimpleLeafVertex>();
		List<SimpleEdge> edges = new ArrayList<SimpleEdge>();
		for (Integer i=0; i< numberOfNodes ;i++) {
			SimpleLeafVertex vertex = new SimpleLeafVertex(Integer.toString(i),0,0);
			vertex.setIconKey("sfree:system");
			vertex.setLabel("ErdosReniyNode"+i);
			
			nodes.put(i,vertex);
		}

		Double z = 0.5*(numberOfNodes-1);
//		Double p = averageNumberofNeighboors/z;
		
		Random r = new Random((new Date()).getTime());
		
		for (Integer start=0; start < numberOfNodes; start++) {
			for (Integer end = start+1; end<numberOfNodes;end++) {
				if (z*r.nextDouble()<averageNumberofNeighboors)  {
					edges.add(new SimpleEdge(TOPOLOGY_NAMESPACE_SFREE, "link:"+start+"-"+end, nodes.get(start), nodes.get(end)));
				}
			}
		}
		
		m_vertexContainer.addAll(nodes.values());
		m_edgeContainer.addAll(edges);
		
	}

    private Vertex getRequiredVertex(Object vertexId) {
        return getVertex(vertexId, true);
    }

    private Vertex getVertex(Object vertexId, boolean required) {
        BeanItem<Vertex> item = m_vertexContainer.getItem(vertexId);
        if (required && item == null) {
            throw new IllegalArgumentException("required vertex " + vertexId + " not found.");
        }
        
        return item == null ? null : item.getBean();
    }

    private SimpleEdge getRequiredEdge(Object edgeId) {
        return getEdge(edgeId, true);
    }

    private SimpleEdge getEdge(Object edgeId, boolean required) {
        BeanItem<SimpleEdge> item = m_edgeContainer.getItem(edgeId);
        if (required && item == null) {
            throw new IllegalArgumentException("required edge " + edgeId + " not found.");
        }
        
        return item == null ? null : item.getBean();
    }

	@Override
	public String getNamespace() {
		return "vertex"; 
	}

}
