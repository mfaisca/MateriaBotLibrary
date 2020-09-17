package com.materiabot.IO.Spreadsheet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import Shared.BotException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SpreadsheetLoader {
	public static final class SpreadsheetBox{
		public static final class SpreadsheetLine{
			List<Object> values;
			
			public SpreadsheetLine(List<Object> vals) { values = vals; }
			
			public String getString(String column) { return getString(letterToColumnNumber(column)); }
			public String getString(int columnNum) { try{ return values.get(columnNum).toString().trim(); } catch(Exception e) {return ""; }}
			public int getInt(String column) { return Integer.parseInt(getString(column)); }
			public int getInt(int columnNum) { return Integer.parseInt(getString(columnNum)); }
			
			private static int letterToColumnNumber(String column){
				column = column.toUpperCase();
				if(column.chars().noneMatch(c -> Character.isAlphabetic(c)))
					return -1;
				final int[] mult = {0};
				return new StringBuilder(column).reverse().chars().reduce(0, 
						(i1, i2) -> i1 + (mult[0]++ == 0 ? i2 - 'A' : (((i2 - 'A') + (mult[0] - 1)) * ('Z' - 'A' + 1))));
			}
		}
		private List<SpreadsheetLine> values;

		public SpreadsheetBox(String spreadsheetId, String pageName) throws BotException {
			values = loadFile(spreadsheetId, pageName).stream().map(l -> new SpreadsheetLine(l)).collect(Collectors.toList());
		}
		
		public List<SpreadsheetLine> getLines(){ return values; }
	}

	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "MateriaBot";
	private static final String CREDENTIALS_FOLDER = "credentials";
	private static final String CLIENT_SECRET = "client_secret.json";

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		FileInputStream in = new FileInputStream(CREDENTIALS_FOLDER + "/" + CLIENT_SECRET);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	private static List<List<Object>> loadFile(String spreadsheetId, String pageName) throws BotException{
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME)
					.build();
			ValueRange response = service.spreadsheets().values()
					.get(spreadsheetId, pageName)
					.execute();
			return response.getValues();
		} catch (IOException | GeneralSecurityException e) {
			throw new BotException("Error loading spreadsheet.", e);
		}
	}
}