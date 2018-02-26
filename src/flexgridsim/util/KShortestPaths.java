package flexgridsim.util;

import java.util.ArrayList;

/**
 * The Class KShortestPaths.
 */
public class KShortestPaths {
	
	/**
	 * Dijkstra k shortest paths.
	 *
	 * @param graph the graph
	 * @param s the s
	 * @param t the t
	 * @param K the k
	 * @return the int[][]
	 */
	public int[][] dijkstraKShortestPaths(WeightedGraph graph, int s, int t, int K){
		ArrayList<CostedPath> P = new ArrayList<CostedPath>();
		int count[] = new int[graph.getNumNodes()]; 
		ArrayList<CostedPath> B = new ArrayList<CostedPath>();
		CostedPath Ps = new CostedPath(graph);
		Ps.add(s);
		B.add(Ps);
		while (!B.isEmpty() && count[t] < K){
			CostedPath Pu = minCost(B);
			int u = Pu.get(Pu.size()-1);
			B.remove(Pu);
			count[u]++;
			if (u==t){
				P.add(Pu);
			}
			if (count[u]<=K){
				final int[] n = graph.neighbors(u);
				for (int v : n) {
					CostedPath Pv = new CostedPath(graph, Pu);
					Pv.add(v);
					B.add(Pv);
				}
			}
		}
		int[][] kPaths = new int[K][];
		for (int i = 0; i < K; i++) {
			kPaths[i] = new int[P.get(i).size()];
			for (int j = 0; j < P.get(i).size(); j++) {
				kPaths[i][j] = P.get(i).get(j);
			}
		}
		return kPaths;
	}
	
	
	/**
	 * Min cost.
	 *
	 * @param paths the paths
	 * @return the k path
	 */
	public static CostedPath minCost(ArrayList<CostedPath> paths){
		if (paths.isEmpty()){
			return null;
		}
		CostedPath minCost = paths.get(0);
		for (CostedPath p : paths) {
			if (p.getCost()<minCost.getCost()){
				minCost = p;
			}
		}
		return minCost;
	}
	/**
	 * The Class KPath.
	 */
	class CostedPath extends ArrayList<Integer>{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3721778618427546839L;
		int cost;
		WeightedGraph graph;
		public CostedPath(WeightedGraph graph){
			super();
			this.graph = graph;
		}
		public CostedPath(WeightedGraph graph, CostedPath path){
			super();
			this.graph = graph;
			cost = 0;
			for (Integer u : path) {
				this.add(u);
			}
		}
		public CostedPath(WeightedGraph graph, int[] path){
			super();
			this.graph = graph;
			for (Integer u : path) {
				this.add(u);
			}
		}
		public int getCost(){
			return cost;
		}
		public boolean add(Integer v){
			if (this.isEmpty()){
				super.add(v);
			} else {
				Integer u = this.get(this.size()-1);
				cost += graph.getWeight(u, v);
				super.add(v);
			}
			return true;
		}
		public boolean equals(CostedPath p){
			if (this.isEmpty() || p.isEmpty()){
				return false;
			}
			if (this.size()!=p.size()){
				return false;
			}
			for (int i = 0; i < this.size(); i++) {
				if (this.get(i)!=p.get(i)){
					return false;
				}
			}
			return true;
		}
	}
}
