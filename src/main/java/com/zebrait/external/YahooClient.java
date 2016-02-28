package com.zebrait.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.zebrait.exceptions.JobFailException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class YahooClient {
	public String getData(String code, int year1, int month1, int day1, int year2, int month2, int day2)
			throws JobFailException {
		StringBuilder content = new StringBuilder();
		--day1;
		--day2;
		try {
			URL url = null;
			String urlString = "http://finance.yahoo.com/q/hp?s=" + code + ".SS&a=" + month1 + "&b=" + day1 + "&c="
					+ year1 + "&d=" + month2 + "&e=" + day2 + "&f=" + year2 + "&g=d";
			url = new URL(urlString);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(false);
			huc.setConnectTimeout(15 * 1000);
			huc.setRequestMethod("GET");
			huc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			huc.connect();
			InputStream input = huc.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(huc.getInputStream()));
			// BufferedReader bufferedReader = new BufferedReader(new
			// InputStreamReader((InputStream) url.getContent()));

			String line = null;
			while ((line = bufferedReader.readLine()) != null)
				if (line.contains("yfnc_tabledata1"))
					content.append(line + "\n");
			log.info("XXXXX---" + content);
		} catch (IOException e) {
			log.error("Get data from Yahoo for " + code + " failed!" + e);
			throw new JobFailException("Job Failed", e);
		}
		return content.toString();
	}
}
