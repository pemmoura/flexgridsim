# Flexgridsim 2.0 #

This software is licensed with GPLv3

### Changelog ###
* Space division multiplexing support
* Bug fixes

FlexGridSim is a discrete event Flexible Grid Optical Network Simulator (Elastic Optical Network), based on the WDMSim.

It is a simple simulator written in Java that allows the simulation of routing and spectrum scenarios in single/multi-hop Optical FlexGrid/EON networks. Its main advantage is the simplified interface for the introduction of new algorithms.

Created by Pedro Moura in the Computer Networks Laboratory, Institute Of Computing, UNICAMP.


### How do I get set up? ###

Download the source code here.
Recommended usage of a Java IDE (NetBeans or Eclipse).
Create a new project based on existing code on NetBeans or Eclipse.
Build the project to generate a JAR file (may require the definition of the main class "Main.java").

### How do I run it ###

Usage: FlexGridSim.jar xml_file number_of_simulations \[-trace] \[-verbose] [minload maxload step]

The required parameters are:

* xml_file: the XML file containing all the information about the simulation environment. Example xml files are provided.
* number_of_simulations: number of simulations will be ran with a different seed each;

Optional parameters:

* -trace: decides if you want to generate a tracing file. Generating the trace file is not necessary if you plan to gather your simulation statistics directly from your code added to the simulator.
* -verbose: if you want lots of information about the simulator in the runtime. Only required for debugging purposes.

Example: you@computer:~$ java -jar FlexGridSim.jar rsa.xml 10 100 200 10

This example will run 10 simulations with different seeds, using the parameters in the rsa.xml file, for each load between 100 and 200 with step 10.

### Simulator output ###

The output graphs are configured in the xml file as with the tag graphs. 

Example:

     <graphs>

          <graph name="mbbr" dots-file="graph-mbbr.dat"/>

     </graphs>

This will output a dat file, that is compatible with gnuplot, with the following sintax:

     load          average_value          95%_confidence_interval

### Webpage ###

http://www.lrc.ic.unicamp.br/flexgridsim/
