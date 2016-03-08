package com.zebrait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class TestSmall {
	@Test
	public void test() {
		// for (int i = 0; i < 100000; ++i)
		// System.out.println("" + (600000 + i) + ": " +
		// testURL(i).trim().substring(100, 200));
	}

	private String testURL(int i) {
		try {
			URL url = null;
			String urlString = "http://finance.yahoo.com/q/hp?s=" + (600000 + i)
					+ ".SS&a=05&b=11&c=2010&d=00&e=29&f=2016&g=d";
			url = new URL(urlString);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(false);
			huc.setConnectTimeout(15 * 1000);
			huc.setRequestMethod("GET");
			huc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			huc.connect();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(huc.getInputStream()));

			String line = null;
			while ((line = bufferedReader.readLine()) != null)
				if (line.contains("yfnc_tabledata1"))
					return line;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)";
	}
}
