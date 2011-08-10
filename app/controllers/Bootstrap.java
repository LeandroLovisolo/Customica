package controllers;

import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends play.jobs.Job {

	@Override
	public void doJob() {
		/*
		if(TShirt.count() == 0) {
			System.out.println("loading data...");
			Fixtures.loadModels("data.yml");
			System.out.println("done");
		}
		*/
	}
	
}
