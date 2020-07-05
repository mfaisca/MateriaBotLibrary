package com.materiabot.IO.JSON;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParser {
	public static class MyJSONObject{
		private JSONObject json;

		public MyJSONObject(JSONObject o) { json = o; }

		public Integer getInt(String name) { try { return json.getInt(name); } catch(Exception e) { return null; }}
		public String getString(String name) { try { return json.getString(name); } catch(Exception e) { return null; }}
		public MyJSONObject getObject(String name) { try { return new MyJSONObject(json.getJSONObject(name)); } catch(Exception e) { return null; }}
		public String[] getStringArray(String name) { 
			try {
				JSONArray arr = name == null ? (JSONArray)(Object)json : json.getJSONArray(name);
				String[] ret = new String[arr.length()];
				for(int i = 0; i < ret.length; i++)
					ret[i] = arr.getString(i);
				return ret;
			} catch(Exception e) {
				return null; 
			}
		}
		@SuppressWarnings("unchecked")
		@Deprecated
		/**
		 * To be used only in names and descriptions to make it easier to find a valid text, following the order: GL > EN > JP
		 */
		public String[] getStringArray(MyJSONObject obj) {
			String[] ret = new String[obj.json.keySet().size()];
			int i = 0;
			for(Object o : ((Set<String>)obj.json.keySet()).stream().sorted().collect(Collectors.toList()))
				ret[i++] = obj.getString(o.toString());
			return ret;
		}
		public Integer[] getIntArray(String name) { 
			try {
				JSONArray arr = name == null ? (JSONArray)(Object)json : json.getJSONArray(name);
				Integer[] ret = new Integer[arr.length()];
				for(int i = 0; i < ret.length; i++)
					ret[i] = arr.getInt(i);
				return ret;
			} catch(Exception e) {
				return null; 
			}
		}
		public MyJSONObject[] getObjectArray(String name) {
			try {
				JSONArray arr = json.getJSONArray(name);
				MyJSONObject[] ret = new MyJSONObject[arr.length()];
				for(int i = 0; i < ret.length; i++)
					ret[i] = new MyJSONObject(arr.getJSONObject(i));
				return ret;
			} catch(Exception e) {
				return null;
			}
		}
		public MyJSONObject[][] getArrayArray(String name) {
			try {
				JSONArray arr = json.getJSONArray(name);
				MyJSONObject[][] ret = new MyJSONObject[arr.length()][];
				for(int i = 0; i < ret.length; i++) {
					ret[i] = new MyJSONObject[arr.getJSONArray(i).length()];
					for(int ii = 0; ii < ret[i].length; ii++)
						ret[i][ii] = new MyJSONObject(arr.getJSONArray(i).getJSONObject(ii));
				}
				return ret;
			} catch(Exception e) {
				return null; 
			}
		}
		
		public JSONObject obtainJSON() { return json; }
	}
	
	public static class ValueGrouping<R>{
		public R type;
		public int id;
		public Integer[] values = new Integer[3];
		
		public ValueGrouping(R t, Integer... vals) {
			type = t;
			values = vals;
		}
		public ValueGrouping(int i, Integer... vals) {
			id = i;
			values = vals;
		}
	}

	private static final JSONObject parse(InputStream is) throws IOException{
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		StringBuilder sb = new StringBuilder();
		do{
			int cp = rd.read();
			if(cp == -1) break;
			sb.append((char) cp);
			if(cp == '\'')
				sb.append('\'');
		}while(true);
		is.close();
		return new JSONObject(sb.toString());
	}
	private static final JSONObject parseFile(String path) throws IOException {
		try(FileInputStream fis = new FileInputStream(new File(path))) {
			return parse(fis);
		} catch(FileNotFoundException e) {
			throw new IOException(e);
		}
	}
	private static final JSONObject parseURL(String path) throws IOException{
		try {
			return parse(new URL(path).openStream());
		} catch(MalformedURLException e) {
			throw new IOException(e);
		}
	}
	public static final MyJSONObject loadContent(String path, boolean urlTrue_fileFalse) {
		try {
			return new MyJSONObject(urlTrue_fileFalse ? parseURL(path) : parseFile(path));
		} catch (IOException e) {
			return null;
		}
	}
}