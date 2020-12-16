package main.java.javacrawler;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.json.*;
import java.util.*;


// N.B: Do not put a main function here, it can't be run.

@Path("/")
public class RequestHandler
{
	@GET
	public Response helloWorld()
	{
		JsonObject obj = Json.createObjectBuilder().add("hello", "world").build();
		return Response.ok(obj).build();
	}


	@GET
	@Path("get/{param}")
	public Response answerGETrequest(@PathParam("param") String request)
	{
		String answer = "GET answer: " + request;
		return Response.ok(answer).build();
	}


	@POST
	@Path("post")
	// @Consumes(MediaType.TEXT_PLAIN)
	// @Produces(MediaType.TEXT_PLAIN)
	public Response answerPOSTrequest(String request)
	{
		String answer = "POST answer: " + request;
		return Response.ok().entity(answer).build(); // alternative syntax
	}


	@GET
	@Path("file")
	public Response answerFileRequest()
	{
		String filename = "some_text_file.txt";
		// String filename = "not_existing_file.txt";

		String content = FileContent.getFileContent(filename);
		String answer = "File content: " + content; // all '\n' are ignored!

		// Equivalent to 404 / 200:
		Response.Status status = content == null ? Response.Status.NOT_FOUND : Response.Status.OK;
		return Response.status(status).entity(answer).build();
	}


	@GET
	@Path("mock")
	public Response mockCrawlQuery()
	{
		// String givenUrl = "invalid_url"; // 404 test: FAILURE!
		// String givenUrl = "http://www.example.com/";
		String givenUrl = "https://en.wikipedia.org/wiki/Main_Page";

		int maxWebPagesToVisit = 3;

		ArrayList<SearchQuery> userQueryList = new ArrayList<SearchQuery>();
		ArrayList<PageData> pageDataList = Crawler.crawl(givenUrl, maxWebPagesToVisit, userQueryList);

		JsonObject obj = OutputJson.jsonWebPages(pageDataList);
		return Response.ok(obj).build();
	}


	@POST
	@Path("crawler")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	// public Response crawlerAnswer(String givenUrl)
	public Response crawlerAnswer(JsonObject jsonQuery)
	{
		String givenUrl = jsonQuery.getString("url"); // can fail!
		// int maxWebPagesToVisit = Integer.parseInt(jsonQuery.getString("maxWebPagesToVisit")); // 2 failures possible!
		int maxWebPagesToVisit = jsonQuery.getInt("maxWebPagesToVisit"); // 2 failures possible!
		// int id = Integer.parseInt(jsonObj.get("id"));
		// int id = jsonObj.getInt("id");


		ArrayList<SearchQuery> userQueryList = new ArrayList<SearchQuery>();
		ArrayList<PageData> pageDataList = Crawler.crawl(givenUrl, maxWebPagesToVisit, userQueryList);

		JsonObject obj = OutputJson.jsonWebPages(pageDataList);
		return Response.ok(obj).build();
	}
}
