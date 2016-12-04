package com.AIPC.models;

public class AIResponse {
	public String id;
	public AIResult result;
	public OriginalRequest originalRequest;

	public static class OriginalRequest {
		public String source;
		public Sender sender;

		public static class Sender {
			public String id;
		}
	}
}
