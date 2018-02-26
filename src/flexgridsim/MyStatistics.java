package flexgridsim;

/**
 * The Class MyStatistics.
 */
public class MyStatistics {
	private static MyStatistics singletonObject;
	private OutputManager plotter;
	private PhysicalTopology pt;
	private TrafficGenerator traffic;
    private int minNumberArrivals;
    private int numberArrivals;
    private int arrivals;
    private int departures;
    private int accepted;
    private int blocked;
    private int requiredBandwidth;
    private int blockedBandwidth;
    private int numNodes;
    private int[][] arrivalsPairs;
    private int[][] blockedPairs;
    private int[][] requiredBandwidthPairs;
    private int[][] blockedBandwidthPairs;
    private double load;
    private double totalPowerConsumed;
    private double simTime;
    private double dataTransmitted;
    private double avgBitsPerSymbol;
    private int avgBitsPerSymbolCount;
    // Diff
    private int[] arrivalsDiff;
    private int[] blockedDiff;
    private int[] requiredBandwidthDiff;
    private int[] blockedBandwidthDiff;
    private int[][][] arrivalsPairsDiff;
    private int[][][] blockedPairsDiff;
    private int[][][] requiredBandwidthPairsDiff;
    private int[][][] blockedBandwidthPairsDiff;
    private int[][] numberOfUsedTransponders;
    
    /**
     * A private constructor that prevents any other class from instantiating.
     */
    private MyStatistics() {
    	
        numberArrivals = 0;

        arrivals = 0;
        departures = 0;
        accepted = 0;
        blocked = 0;

        requiredBandwidth = 0;
        blockedBandwidth = 0;
    }
    
    /**
     * Creates a new MyStatistics object, in case it does'n exist yet.
     * 
     * @return the MyStatistics singletonObject
     */
    public static synchronized MyStatistics getMyStatisticsObject() {
        if (singletonObject == null) {
            singletonObject = new MyStatistics();
        }
        return singletonObject;
    }
    
    /**
     * Throws an exception to stop a cloned MyStatistics object from
     * being created.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    
    /**
     * Attributes initializer.
     *
     * @param plotter the graph plotter
     * @param pt the pt
     * @param traffic 
     * @param numNodes number of nodes in the network
     * @param numClasses number of classes of service
     * @param minNumberArrivals minimum number of arriving events
     * @param load the load of the network
     */
    public void statisticsSetup(OutputManager plotter, PhysicalTopology pt, TrafficGenerator traffic, int numNodes, int numClasses, int minNumberArrivals, double load) {
    	this.plotter = plotter;
    	this.pt = pt;
    	this.traffic = traffic;
        this.numNodes = numNodes;
        this.load = load;
        this.arrivalsPairs = new int[numNodes][numNodes];
        this.blockedPairs = new int[numNodes][numNodes];
        this.requiredBandwidthPairs = new int[numNodes][numNodes];
        this.blockedBandwidthPairs = new int[numNodes][numNodes];
        this.avgBitsPerSymbol = 0;
        this.avgBitsPerSymbolCount = 0;
        this.minNumberArrivals = minNumberArrivals;
        numberOfUsedTransponders = new int[numNodes][numNodes];
        //Diff
        this.arrivalsDiff = new int[numClasses];
        this.blockedDiff = new int[numClasses];
        this.requiredBandwidthDiff = new int[numClasses];
        this.blockedBandwidthDiff = new int[numClasses];
        for (int i = 0; i < numClasses; i++) {
            this.arrivalsDiff[i] = 0;
            this.blockedDiff[i] = 0;
            this.requiredBandwidthDiff[i] = 0;
            this.blockedBandwidthDiff[i] = 0;
        }
        this.arrivalsPairsDiff = new int[numNodes][numNodes][numClasses];
        this.blockedPairsDiff = new int[numNodes][numNodes][numClasses];
        this.requiredBandwidthPairsDiff = new int[numNodes][numNodes][numClasses];
        this.blockedBandwidthPairsDiff = new int[numNodes][numNodes][numClasses];
        this.totalPowerConsumed = 0;
        this.simTime = 0;
        this.dataTransmitted = 0;
    }
	/**
	 * Calculate last statistics for the graph generation.
	 */
	public void calculateLastStatistics(){
		//bandwidth block graph
		avgBitsPerSymbol = avgBitsPerSymbol/avgBitsPerSymbolCount;
		plotter.addDotToGraph("avgbps", load, avgBitsPerSymbol);
		plotter.addDotToGraph("mbbr", load, ((float) blockedBandwidth) / ((float) requiredBandwidth));
		plotter.addDotToGraph("bp", load, ((float) blocked) / ((float) arrivals) * 100);
		int count = 0;
        float bbr, jfi, sum1 = 0, sum2 = 0;
        if (blocked == 0) {
            bbr = 0;
        } else {
            bbr = ((float) blockedBandwidth) / ((float) requiredBandwidth) * 100;
        }
        for (int i = 0; i < numNodes; i++) {
            for (int j = i + 1; j < numNodes; j++) {
                if (i != j) {
                    if (blockedPairs[i][j] == 0) {
                        bbr = 0;
                    } else {
                        bbr = ((float) blockedBandwidthPairs[i][j]) / ((float) requiredBandwidthPairs[i][j]) * 100;
                    }
                    count++;
                    sum1 += bbr;
                    sum2 += bbr * bbr;
                }
            }
        }
        jfi = (sum1 * sum1) / ((float) count * sum2);
        plotter.addDotToGraph("jfi", load, jfi);
       // System.out.println("jfi="+jfi);
    	//POWE CONSUPTION
    	double PCoxc = 0;
    	for (int i = 0; i < pt.getNumNodes(); i++) {
			PCoxc += pt.getNodeDegree(i) * 85 + 150; //Wss= 40 Wmems=150
		}
    	double PCedfa = pt.getNumLinks() * 200;
    	totalPowerConsumed += simTime * (PCoxc + PCedfa);
    	plotter.addDotToGraph("pc", load, (totalPowerConsumed)/(simTime*1000));
    	plotter.addDotToGraph("ee", load, dataTransmitted/( totalPowerConsumed / 1000));
    	plotter.addDotToGraph("data", load, dataTransmitted);
    	plotter.addDotToGraph("ee2", load, (((float) blockedBandwidth) / ((float) requiredBandwidth)) / (totalPowerConsumed/(simTime*1000)));
    	
	}
	
	/**
	 * Calculate periodical statistics.
	 */
	public void calculatePeriodicalStatistics(){
		//fragmentation graph
		double fragmentationMean = 0;
		double averageCrosstalk = 0;
    	for (int i = 0; i < pt.getNumLinks(); i++) {
    			fragmentationMean += pt.getLink(i).getFragmentationRatio(traffic.getCallsTypeInfo(), 12.5);//pt.getSlotCapacity());
    			averageCrosstalk += pt.getLink(i).averageCrosstalk();
		}
    	averageCrosstalk /= pt.getNumLinks();
    	plotter.addDotToGraph("avgcrosstalk", load, averageCrosstalk);
    	fragmentationMean = fragmentationMean / pt.getNumLinks();
    	plotter.addDotToGraph("fragmentation", load, fragmentationMean);
    	double meanTransponders = 0;
    	for (int i = 0; i < numberOfUsedTransponders.length; i++) {
			for (int j = 0; j < numberOfUsedTransponders[i].length; j++) {
				if (numberOfUsedTransponders[i][j]>0){
					meanTransponders += numberOfUsedTransponders[i][j];
				}
			}
		}
    	
//    	meanTransponders = meanTransponders / size;
    	if (Double.compare(meanTransponders, Double.NaN)!=0){
    		plotter.addDotToGraph("transponders", load, meanTransponders);
    	}
    	double xtps = 0;
    	int linksXtps = 0;
    	for (int i = 0; i < pt.getNumLinks(); i++) {
    		try {
    			double xt = pt.getLink(i).getCrossTalkPerSlot();
    			if (xt>0){
    				xtps += xt;
    				linksXtps++;
    			}
    		} catch (NullPointerException e) {
    			
    		}
		}
    	if (xtps!=0)
    		plotter.addDotToGraph("xtps", load, xtps/ linksXtps);
    	
    	//BFR
//    	double BFR = 0;
//    	for (int i = 0; i < pt.getNumLinks(); i++) {
//			FlexGridLink link = pt.getLink(i);
//			double sumbe = link.getNumFreeSlots();
//			int B = link.getSlots();
//			double psi =0;
//			
//			if (sumbe < B) {
//				psi = (double)1.0 - (link.maxNumberOfContiguousSlots()/sumbe);
//			} else {
//				psi = 0;
//			}
//			BFR += psi;
//		}
//    	BFR = BFR/pt.getNumLinks();
//    	plotter.addDotToGraph("bfr", load, BFR);
	}
	
    /**
     * Adds an accepted flow to the statistics.
     * 
     * @param flow the accepted Flow object
     * @param lightpath lightpath of the flow
     */
    public void acceptFlow(Flow flow, LightPath lightpath) {
        if (this.numberArrivals > this.minNumberArrivals){
        	this.avgBitsPerSymbol+=ModulationsMuticore.numberOfSymbols[flow.getModulationLevel()];
        	this.avgBitsPerSymbolCount++;
	        this.accepted++;
        	int links =  flow.getLinks().length+1;
        	plotter.addDotToGraph("modulation", load, flow.getModulationLevel());
            plotter.addDotToGraph("hops", load, links);
            dataTransmitted += flow.getRate();
            for (int i = 0; i < pt.getCores(); i++) {
            	totalPowerConsumed += flow.getDuration() * flow.getSlotList().size() * Modulations.getPowerConsumption(flow.getModulationLevel());
            }
            numberOfUsedTransponders[flow.getSource()][flow.getDestination()]++;
        }
    }
    
    /**
     * Groomed flow.
     *
     * @param flow the flow
     */
    public void groomedFlow(Flow flow){
    	if (this.numberArrivals > this.minNumberArrivals){
            dataTransmitted += flow.getRate();
            for (int i = 0; i < pt.getCores(); i++) {
            	totalPowerConsumed += flow.getDuration() * flow.getSlotList().size() *Modulations.getPowerConsumption(flow.getModulationLevel());
            }
        }
    }
    /**
     * Adds a blocked flow to the statistics.
     * 
     * @param flow the blocked Flow object
     */
    public void blockFlow(Flow flow) {
        if (this.numberArrivals > this.minNumberArrivals) {
	        this.blocked++;
            int cos = flow.getCOS();
            this.blockedDiff[cos]++;
            this.blockedBandwidth += flow.getRate();
            this.blockedBandwidthDiff[cos] += flow.getRate();
            this.blockedPairs[flow.getSource()][flow.getDestination()]++;
            this.blockedPairsDiff[flow.getSource()][flow.getDestination()][cos]++;
            this.blockedBandwidthPairs[flow.getSource()][flow.getDestination()] += flow.getRate();
            this.blockedBandwidthPairsDiff[flow.getSource()][flow.getDestination()][cos] += flow.getRate();
        }
    }
    
    /**
     * Adds an event to the statistics.
     * 
     * @param event the Event object to be added
     */
    public void addEvent(Event event) {
    	simTime = event.getTime();
        try {
            if (event instanceof FlowArrivalEvent) {
                this.numberArrivals++;
                if (this.numberArrivals > this.minNumberArrivals) {
                    int cos = ((FlowArrivalEvent) event).getFlow().getCOS();
                    this.arrivals++;
                    this.arrivalsDiff[cos]++;
                    this.requiredBandwidth += ((FlowArrivalEvent) event).getFlow().getRate();
                    this.requiredBandwidthDiff[cos] += ((FlowArrivalEvent) event).getFlow().getRate();
                    this.arrivalsPairs[((FlowArrivalEvent) event).getFlow().getSource()][((FlowArrivalEvent) event).getFlow().getDestination()]++;
                    this.arrivalsPairsDiff[((FlowArrivalEvent) event).getFlow().getSource()][((FlowArrivalEvent) event).getFlow().getDestination()][cos]++;
                    this.requiredBandwidthPairs[((FlowArrivalEvent) event).getFlow().getSource()][((FlowArrivalEvent) event).getFlow().getDestination()] += ((FlowArrivalEvent) event).getFlow().getRate();
                    this.requiredBandwidthPairsDiff[((FlowArrivalEvent) event).getFlow().getSource()][((FlowArrivalEvent) event).getFlow().getDestination()][cos] += ((FlowArrivalEvent) event).getFlow().getRate();
                }
                if (Simulator.verbose && Math.IEEEremainder((double) arrivals, (double) 10000) == 0) {
                    System.out.println(Integer.toString(arrivals));
                }
            }
            else if (event instanceof FlowDepartureEvent) {
                if (this.numberArrivals > this.minNumberArrivals) {
                    this.departures++;
                }
                Flow f = ((FlowDepartureEvent)event).getFlow();
                if (f.isAccepeted()){
                	this.numberOfUsedTransponders[f.getSource()][f.getDestination()]--;
                }
            }
            if (this.numberArrivals % 100 == 0){
            	calculatePeriodicalStatistics();
            	
            }
            if (this.numberArrivals % 5000 == 0){
            	
//            	System.out.println(event.getTime()+","+BFR);
            }
        }
        
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * This function is called during the simulation execution, but only if
     * verbose was activated.
     * 
     * @return string with the obtained statistics
     */
    public String fancyStatistics() {
        float acceptProb, blockProb, bbr;
        if (accepted == 0) {
            acceptProb = 0;
        } else {
            acceptProb = ((float) accepted) / ((float) arrivals) * 100;
        }
        if (blocked == 0) {
            blockProb = 0;
            bbr = 0;
        } else {
            blockProb = ((float) blocked) / ((float) arrivals) * 100;
            bbr = ((float) blockedBandwidth) / ((float) requiredBandwidth) * 100;
        }

        String stats = "Arrivals \t: " + Integer.toString(arrivals) + "\n";
        stats += "Required BW \t: " + Integer.toString(requiredBandwidth) + "\n";
        stats += "Departures \t: " + Integer.toString(departures) + "\n";
        stats += "Accepted \t: " + Integer.toString(accepted) + "\t(" + Float.toString(acceptProb) + "%)\n";
        stats += "Blocked \t: " + Integer.toString(blocked) + "\t(" + Float.toString(blockProb) + "%)\n";
        stats += "BBR     \t: " + Float.toString(bbr) + "%\n";
        stats += "\n";
        stats += "Blocking probability per s-d pair:\n";
        for (int i = 0; i < numNodes; i++) {
            for (int j = i + 1; j < numNodes; j++) {
                stats += "Pair (" + Integer.toString(i) + "->" + Integer.toString(j) + ") ";
                stats += "Calls (" + Integer.toString(arrivalsPairs[i][j]) + ")";
                if (blockedPairs[i][j] == 0) {
                    blockProb = 0;
                    bbr = 0;
                } else {
                    blockProb = ((float) blockedPairs[i][j]) / ((float) arrivalsPairs[i][j]) * 100;
                    bbr = ((float) blockedBandwidthPairs[i][j]) / ((float) requiredBandwidthPairs[i][j]) * 100;
                }
                stats += "\tBP (" + Float.toString(blockProb) + "%)";
                stats += "\tBBR (" + Float.toString(bbr) + "%)\n";
            }
        }

        return stats;
    }
	
    
    
    /**
     * Terminates the singleton object.
     */
    public void finish()
    {
        singletonObject = null;
    }
}
