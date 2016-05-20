package com.geppetto.installrwebscrapper;

import org.json.JSONArray;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class Main {

	public static void main(String[] args) {
		JSONObject jsonObject = null;
		try {

			String loginUrl = "https://www.installrapp.com/j_spring_security_check";

			// First login. Take the cookies
			Connection.Response res = Jsoup.connect(loginUrl).data("j_username", "rashmis19oct@gmail.com")
					.data("j_password", "webw0rld").referrer("https://www.installrapp.com/")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.POST).timeout(10000).execute();

		/*	JSONObject data = new JSONObject("data.json");
			System.out.println("---data.json-----"+data.toString());*/
			
			System.out.println("----login session id---"+res.cookie("JSESSIONID"));
			System.out.println("---url---" + res.parse().title());

			String s = Jsoup.connect("https://www.installrapp.com/apps.json")
					.cookie("JSESSIONID", res.cookie("JSESSIONID"))
					.referrer("https://www.installrapp.com/dashboard/index")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.GET).timeout(12000).ignoreContentType(true).execute().body();

			System.out.println("----app.json---" + s);

			JSONObject obj = new JSONObject(s);
			JSONArray arr = obj.getJSONArray("appList");
			String appid = "";
			Object post_id = arr.getJSONObject(0).get("id");
			appid = post_id.toString();
			
			
			System.out.println("------appid----" + appid);
			

			String json = Jsoup.connect("https://www.installrapp.com/adprobot/validate.json")
					.data("email", "venkat86.careers@gmail.com", "password", "Boss643ace43*", "app", appid)
					.cookie("JSESSIONID", res.cookie("JSESSIONID"))
					.referrer("https://www.installrapp.com/dashboard/index")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.POST).timeout(15000).ignoreContentType(true).execute().body();
			System.out.println("--autoprovisioning validate.json--" + json);

			jsonObject = new JSONObject(json);

			System.out.println("---validation response status--" + jsonObject.get("result"));
			String cc="https://www.installrapp.com/apps/"+appid+".json";
			
			String appidtest = Jsoup.connect(cc)
					.cookie("JSESSIONID", res.cookie("JSESSIONID"))
					.referrer("https://www.installrapp.com/dashboard/index")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
					.method(Method.GET).timeout(12000).ignoreContentType(true).execute().body();
			System.out.println("------app details---"+appidtest);
			

			if (jsonObject.get("result").equals("failed")) {
				main(args);
			}

		} catch (Exception e) {
			main(args);

		}
	}
}