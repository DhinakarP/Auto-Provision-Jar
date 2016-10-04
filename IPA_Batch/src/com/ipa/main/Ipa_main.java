package com.ipa.main;

import com.ipa.service.status_service;

public class Ipa_main extends status_service {

	public static void main(String[] args) {

		status_service status = new status_service();
		//status.status();
		System.out.println("fetching data ......");
		while (true) {
			
			System.out.println("searching...... ");

			status.status();
		}

	}

}
