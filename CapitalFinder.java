package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class CapitalFinder {

	private final String BY_CODE_URL = "https://restcountries.eu/rest/v2/alpha/%s?fields=capital";

	private final String BY_NAME_URL = "https://restcountries.eu/rest/v2/name/%s?fields=capital";

	public static void main(String[] args) {

		CapitalFinder testClass = new CapitalFinder();
		Scanner reader = new Scanner(System.in);
		
		String doneTesting = "";

		String response = "";

		while (!doneTesting.equalsIgnoreCase("Y")) {
			
			System.out.println("Search by Name(N) or Code(C). Enter your choice");
			String choice = reader.next();
			
			if (choice.equalsIgnoreCase("C")) {
				System.out.println("Enter country code:");
				String code = reader.next();
				response = testClass.getCountryCapitalByCode(code);
			} else if (choice.equalsIgnoreCase("N")) {
				System.out.println("Enter country name:");
				String name = reader.next();
				response = testClass.getCountryCapitalByName(name);
			}

			if (response.contains("ERROR")) {
				System.out.println(response);
			} else {
				System.out.println("Capital is " + response);
			}
			System.out.println("Are you done (Y/N :");
			doneTesting = reader.next();
		}
		
		reader.close();
	}

	private String getCountryCapitalByCode(String code) {
		URL url = null;
		try {

			url = new URL(String.format(BY_CODE_URL, code));

		} catch (MalformedURLException e) {

			e.printStackTrace();

		}
		return getCountryCapital(url);
	}

	private String getCountryCapitalByName(String name) {

		URL url = null;
		try {
			url = new URL(String.format(BY_NAME_URL, name));
		} catch (MalformedURLException e) {

			e.printStackTrace();

		}
		return getCountryCapital(url);

	}

	private String getCountryCapital(URL url) {

		String capital = null;

		try {
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Accept", "application/json");

			if (urlConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + urlConnection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));

			String response = br.readLine();

			if(response ==null || response =="") {
				return "ERROR: entry not found for given input. Please enter a valid input";
			} else if (response.contains(",")) {
				return "ERROR: multiple entries found for given input. Please enter a valid input";
			}

			String[] responseArray = response.split("\"");
			if (responseArray != null && responseArray.length > 1) {
				capital = responseArray[responseArray.length - 2];
			}

			urlConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return capital;
	}
}
