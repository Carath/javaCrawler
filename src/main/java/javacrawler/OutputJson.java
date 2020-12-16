package main.java.javacrawler;

import jakarta.json.*;
import java.util.*;


public class OutputJson
{
	public static final Boolean RETURN_HTML_CONTENT = true;


	public static JsonObject jsonOfMetaData(MetaData metaData)
	{
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for (String s : metaData.getDataList()) {
			builder.add(s);
		}

		JsonArray array = builder.build();

		JsonObject obj = Json.createObjectBuilder()
			.add("dataTitle", metaData.getDataTitle())
			.add("dataList", array)
			.build();

		return obj;
	}


	public static JsonObject jsonOfPageData(PageData pageData)
	{
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for (MetaData metaData : pageData.getMetaDataList()) {
			JsonObject metaDataJson = jsonOfMetaData(metaData);
			builder.add(metaDataJson);
		}

		JsonArray array = builder.build();
		String content = RETURN_HTML_CONTENT ? pageData.getHtmlContent() : "";

		JsonObject obj = Json.createObjectBuilder()
			.add("url", pageData.getUrl())
			.add("lang", pageData.getLang())
			.add("title", pageData.getTitle())
			.add("htmlContent", content)
			.add("metaDataList", array)
			.build();

		return obj;
	}


	public static JsonObject jsonWebPages(ArrayList<PageData> pageDataList)
	{
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for (PageData pageData : pageDataList) {
			JsonObject pageDataJson = jsonOfPageData(pageData);
			builder.add(pageDataJson);
		}

		JsonArray array = builder.build();

		JsonObject obj = Json.createObjectBuilder()
			.add("webPagesData", array)
			.build();

		return obj;
	}
}
