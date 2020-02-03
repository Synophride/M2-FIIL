package pawa.tpnote;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class URLInfo {

	private final String url;
	private final int hash;
	private final String pass;
	private final int count;
	
	public URLInfo(String u, int h, int c, String p) {
		this.url = u;
		this.hash = h;
		this.count = c;
		this.pass  = p;
	}

	public String getUrl() {
		return url;
	}

	public int getHash() {
		return hash;
	}

	public int getCount() {
		return count;
	}
	
	public String getPass() {
		return pass;
	}

	/**
	 * QUESTION 1.4
	 */
	public JsonObject asJSON() {
		JsonObject json_retour = Json.createObjectBuilder()
		  .add("url", url)
		  .add("hash", hash)
		  .add("count", count)
		  .add("pass", pass)
		  .build();
		return json_retour; 
	}
	
}
