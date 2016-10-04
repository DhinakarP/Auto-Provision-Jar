package com.ipa.service;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class status_service extends Thread {

	public void run() {
		try {
			System.out.println("sleeping.....");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
	}

	public void status() {
		Properties prop1 = new Properties();
		try {
			prop1.load(new FileInputStream("resource/jdbc.properties"));
			String myDriver = prop1.getProperty("jdbc.driverClassName");
			String myUrl = prop1.getProperty("jdbc.url");
			String username = prop1.getProperty("jdbc.username");
			String Password = prop1.getProperty("jdbc.password");
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, username,
					Password);

			String query = "SELECT claimed FROM ipa";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			Integer id = 0;
			String app = null;

			while (rs.next()) {
				String status = rs.getString("claimed");
				if (status.equals("TRUE")) {
					String query2 = "SELECT * FROM ipa WHERE claimed LIKE 'TRUE' ";
					Statement st1 = conn.createStatement();
					ResultSet rs1 = st1.executeQuery(query2);
					while (rs1.next()) {
						 app = rs1.getString("APP_Name");
						id = rs1.getInt("id");

						System.out.format("%s, %s\n", id, app+"---It's claimed is true");
					}
					st1.close();
				}
				run();
				if (status.equals("TRUE")) {
					String query3 = "update ipa set claimed = 'FALSE' where id = "
							+ id;
					Statement st2 = conn.prepareStatement(query3);
					st2.executeUpdate(query3);
					System.out.println(app+"--claimed is updated to FALSE");

				}
			}
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}
}
