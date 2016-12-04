package com.AIPC.models;

public class AIResult {
	public String resolvedQuery;
	public String action;
	public Parameters parameters;
	public static class Parameters {
		public String[] locations;
		public String hailFrom;
	}
}
