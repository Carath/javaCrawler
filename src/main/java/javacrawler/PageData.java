package main.java.javacrawler;

import java.io.*;
import java.util.ArrayList;
import java.net.URL;

public class PageData
{
	private String url;
	private String lang;
	private String title;
	private String htmlContent;
	private ArrayList<MetaData> metaDataList; // will include lang, pageTitle, links.
	private Boolean urlValidity;


	public PageData(String url) {
		this.url = url;
		this.lang = "";
		this.title = "";
		this.urlValidity = true;
		this.htmlContent = getURLcontent(url);
		this.metaDataList = new ArrayList<MetaData>();

		if (this.htmlContent == null) {
			this.htmlContent = "";
			this.urlValidity = false;
		}
	}

	public void setMissingFields(String lang, String title) {
		this.lang = lang;
		this.title = title;
	}

	public String getUrl() {
		return this.url;
	}

	public String getLang() {
		return this.lang;
	}

	public String getTitle() {
		return this.title;
	}

	public Boolean getUrlValidity() {
		return this.urlValidity;
	}

	public String getHtmlContent() {
		return this.htmlContent;
	}

	public ArrayList<MetaData> getMetaDataList() {
		return this.metaDataList;
	}

	public void print()
	{
		System.out.printf("\n\n> Page data:\n\n- URL: %s\n- Language: %s\n- Title: %s\n- URL validity: %s\n" +
			"- Meta data list (length = %d):\n", this.url, this.lang, this.title, this.urlValidity, this.metaDataList.size());

		// System.out.printf("\nHTML content:\n\n%s\n\n", this.htmlContent);

		for (MetaData metaData : this.metaDataList) {
			metaData.print();
		}
	}


	public static URL getUrlObject(String url)
	{
		try {
			return new URL(url);
		}
		catch (Exception e) {
			System.err.printf("\nInvalid url: '%s'.\n", url);
			return null;
		}	
	}


	// Get the html content of the given web page.
	// Returns null on invalid url, and "" on 404.
	public static String getURLcontent(String url)
	{
		URL urlObject = getUrlObject(url);
		String content = "";

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlObject.openStream(), "UTF-8"))) {
			for (String line; (line = reader.readLine()) != null;) {
				content += line + "\n";
			}

			reader.close();
			return content;
		}
		catch (Exception e) {
			System.err.printf("\nPage '%s' not found.\n", url);
			return "";
		}
	}
}
