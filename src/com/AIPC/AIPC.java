package com.AIPC;

import java.io.IOException;

import com.AIPC.RCCommand.Modes;
import com.AIPC.models.AIResponse;
import com.AIPC.models.FareHandler;
import com.AIPC.models.AIResult.Parameters;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import spark.Spark;

public class AIPC {
	private final static String NXT_MAC = "00165316455E";
	private static final int PORT = 8080;
	private final static String FARE_PAYMENT_URL = "https://farepayment.herokuapp.com/";

	private static String currentSender = "";
	private static String facebookPageToken = "EAAQ0xjo27OkBADjhNio0TZB1IpisjfxaM5caZBRwKAXGeamZAiPSpZCbKQlDzoXz68pzuoqu6gVzQ57vCkL4IMG0l1yOrzL5ovTOgN8aiZBrSLDHno8GjPq0fhFjxLHgTg37WsossLt0VZASxxqCNCPQwY3DuyInNZC1CHtNxHL0wZDZD";
	private static MediaType jsonHead = MediaType.parse("application/json; charset=utf-8");
	static OkHttpClient client = new OkHttpClient();
	static RobotConnection robotConnection;

	public static void main(String[] args) throws NXTCommException, IOException, InterruptedException {
		System.out.println("Connecting to NXT with MAC: " + NXT_MAC);
		NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", "00165316455E");
		robotConnection = new RobotConnection(nxtInfo);

		System.out.println("Attempting Handshake...");
		boolean robotHandshake = robotConnection.handshake();

		if (robotHandshake) {
			System.out.println("Handshake successful");
			startWebServer();

			WaitForPacket waitForPacket = new WaitForPacket(robotConnection);
			waitForPacket.setFareHandler((fare) -> {
				sendFare(fare);
				currentSender = "";
			});
		} else {
			System.out.println("Robot handshake was not successful");
		}

		// robotConnection.close();

	}

	static void startWebServer() {
		System.out.println("Starting web server on port: " + PORT);
		Spark.port(PORT);

		Spark.get("/", (req, res) -> {
			return "Welcome to the robot interface";
		});
		Spark.post("/webhook", (req, res) -> {
			res.type("application/json");
			AIResponse aiResponse = new Gson().fromJson(req.body(), AIResponse.class);
			System.out.println(req.body());
			if (aiResponse.originalRequest != null && aiResponse.originalRequest.source == "facebook"
					&& currentSender == "")
				currentSender = aiResponse.originalRequest.sender.id;
			if (aiResponse.result.action.equals("navigate")) {
				return handleNavigateAction(aiResponse.result.parameters);
			}
			if (aiResponse.result.action.equals("robotState"))
				return handleGetRobotState();
			return ""; // Let api.ai handle all other calls, like a demigod
		});
	}

	private static String handleNavigateAction(Parameters parameters) throws IOException {
		if (currentSender == "") {
			String[] locations = parameters.locations;
			byte[] commands = new byte[locations.length];
			for (int i = 0; i < commands.length; i++) {
				commands[i] = convertFromStringToByte(locations[i]);
			}
			robotConnection.writeRobotPacket(new RobotPacket(Modes.NAVIGATE, commands));
			if (locations.length == 1 && locations.equals("home")) {
				return "{\"speech\": \"Okay, I'm going to head home\"}";
			} else {
				return ""; // Leave API.ai to respond
			}
		} else {
			return "{\"speech\": \"Okay, I'm currently on another job. Try again in a few minutes!\"}";
		}
	}

	public static byte convertFromStringToByte(String string) {
		if (string.equals("the park")) {
			return RobotConnection.PARK;
		} else if (string.equals("the shop")) {
			return RobotConnection.SHOP;
		} else if (string.equals("the office")) {
			return RobotConnection.OFFICE;
		} else if (string.equals("home")) {
			return RobotConnection.HOME;
		} else if (string.equals("the church")) {
			return RobotConnection.CHURCH;
		} else if (string.equals("the hospital")) {
			return RobotConnection.HOSPITAL;
		} else if (string.equals("the supermarket")) {
			return RobotConnection.SUPERMARKET;
		} else if (string.equals("the station")) {
			return RobotConnection.STATION;
		} else {
			return 0x00;
		}
	}

	private static String handleGetRobotState() {
		return "{\"speech\": \"Everything is looking good! Give me something to do!\"}";
	}

	private static void sendFare(int fare) {
		RequestBody body = RequestBody.create(jsonHead,
				"{\"recipient\":{\"id\":\"" + currentSender
						+ "\"},\"message\": {\"attachment\": { \"type\": \"template\", \"payload\": { \"template_type\": \"button\",\r\n"
						+ "        \"text\": \"" + getFareMessage(fare)
						+ "\", \"buttons\": [ { \"type\": \"web_url\", \"url\": \""+FARE_PAYMENT_URL+"?fare="+fare+"\",\r\n"
						+ "            \"title\": \"Pay Fare\" }  ] } } } }");

		Request request = new Request.Builder()
				.url("https://graph.facebook.com/v2.6/me/messages?access_token=" + facebookPageToken).post(body)
				.build();

		try {
			client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getFareMessage(int fare) {
		return String.format("Thank you for riding with TaxiBot, your fare is \u00A3%.2f", (double) fare / 100d);
	}

}
