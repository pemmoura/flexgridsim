package flexgridsim.rsa;

import org.w3c.dom.Element;

import flexgridsim.Flow;
import flexgridsim.PhysicalTopology;
import flexgridsim.TrafficGenerator;
import flexgridsim.VirtualTopology;
import flexgridsim.util.WeightedGraph;

/**
 * @author pedrom
 *
 */
public class RCSA implements RSA{
	protected PhysicalTopology pt;
	protected VirtualTopology vt;
	protected ControlPlaneForRSA cp;
	protected WeightedGraph graph;
	@Override
	public void simulationInterface(Element xml, PhysicalTopology pt,
			VirtualTopology vt, ControlPlaneForRSA cp, TrafficGenerator traffic) {
		this.pt = pt;
		this.vt = vt;
		this.cp = cp;
		this.graph = pt.getWeightedGraph();
	}
	@Override
	public void flowArrival(Flow flow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flowDeparture(Flow flow) {
	}

}
