package com.prutha.DeduplicationCloud;

import com.prutha.deduplication.action.DeduplicatioRun;
import com.prutha.deduplication.action.ReportGeneration;

public class App 
{
	public static void main(String[] args) {
		String directory = "F:\\Prutha";
		DeduplicatioRun deduplicatioRun = new DeduplicatioRun();
		deduplicatioRun.applyAlgorithm(directory);
		
		ReportGeneration rg = new ReportGeneration();
		rg.run();
		System.out.println("Size of directory before deuplication (bytes): "+rg.getDirSize());
		System.out.println("Size of directory after deuplication (bytes): "+rg.getHashSize());
		System.out.println("Total number of files: "+rg.getTotalFiles());
		System.out.println("Total number of hash files: "+rg.getTotalHashFiles());
		System.out.println("ThroughPut: "+rg.getThroughPut());
		System.out.println("De-duplication ratio (%):"+rg.getDedupRatio());
	}
}
