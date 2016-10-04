package com.geppetto.installrwebscrapper;

import java.io.FileInputStream;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class Main {

	public static void main(String[] args) {
		JSONObject jsonObject = null;
		try {
			Properties prop1 = new Properties();
			prop1.load(new FileInputStream("gradle.properties"));
			String installrUsername = prop1.getProperty("installrUsername");
			String appleUserID = prop1.getProperty("appleAccountUsername");
			String applePassword = prop1.getProperty("applePassword");
			String installrPassword = prop1.getProperty("installrPassword");
			
			System.out.println("installr Email -->" + installrUsername);
			System.out.println(" appleUserID -->" + appleUserID);
			System.out.println(" applePassword -->" + applePassword);
			System.out.println("installrPassword---->"+installrPassword); 
			
			String loginUrl = "https://www.installrapp.com/j_spring_security_check";

			Connection.Response res = Jsoup.connect(loginUrl).data("j_username", installrUsername)
					.data("j_password", installrPassword).referrer("https://www.installrapp.com/")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.POST).timeout(10000).execute();

			String s = Jsoup.connect("https://www.installrapp.com/apps.json")
					.cookie("JSESSIONID", res.cookie("JSESSIONID"))
					.referrer("https://www.installrapp.com/dashboard/index")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.GET).timeout(12000).ignoreContentType(true).execute().body();

			JSONObject obj = new JSONObject(s);
			JSONArray arr = obj.getJSONArray("appList");
			String appid = "";
			Object post_id = arr.getJSONObject(0).get("id");
			appid = post_id.toString();

			String json = Jsoup.connect("https://www.installrapp.com/adprobot/validate.json")
					.data("email", appleUserID, "password", applePassword, "app", appid)
					.cookie("JSESSIONID", res.cookie("JSESSIONID"))
					.referrer("https://www.installrapp.com/dashboard/index")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.POST).timeout(15000).ignoreContentType(true).execute().body();

			jsonObject = new JSONObject(json);

			String cc = "https://www.installrapp.com/apps/" + appid + ".json";

			String appidtest = Jsoup.connect(cc).cookie("JSESSIONID", res.cookie("JSESSIONID"))
					.referrer("https://www.installrapp.com/dashboard/index")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.GET).timeout(12000).ignoreContentType(true).execute().body();

			if (jsonObject.get("result").equals("failed")) {
				System.err.println("looping");
				main(args);

			} else {
				System.out.println("!!!~~~~~~~~~~~~~~~~~AUTOPROVISION DONE ~~~~~~~~~~~~~~~~~~~~~~~~~!!!");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			main(args);

		}
	}
}