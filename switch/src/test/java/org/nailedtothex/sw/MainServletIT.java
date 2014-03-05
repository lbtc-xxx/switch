package org.nailedtothex.sw;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class MainServletIT {

	static Properties testEnvSpecific;
	static Properties testCommon;

	@BeforeClass
	public static void beforeClass() throws Exception {
		testEnvSpecific = new Properties();
		testCommon = new Properties();

		try (InputStream is = MainServletIT.class.getResourceAsStream("/test-env-specific.properties")) {
			testEnvSpecific.load(is);
		}
		try (InputStream is = MainServletIT.class.getResourceAsStream("/test-common.properties")) {
			testCommon.load(is);
		}
		
		System.out.println("testEnvSpecific: " + testEnvSpecific);
		System.out.println("testCommon: " + testCommon);
	}

	@Test
	public void test() throws Exception {
		URL url = new URL(getURL());
		
		System.out.println("test(): environment=" + getEnvironmentId() + ", url=" + url);
		
		try(InputStream is = url.openStream();
			InputStreamReader r = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(r);){
			assertThat(br.readLine(),
					is(String.format("This value came from context-param defined in /src/main/webapp/_%s/WEB-INF/web.xml", getEnvironmentId())));
			assertThat(br.readLine(),
					is("This value came from /src/main/resources/common.properties"));
			assertThat(br.readLine(),
					is(String.format("This value came from /src/main/resources/_%s/env-specific.properties", getEnvironmentId())));
			assertThat(br.readLine(), is(nullValue()));
		}
	}
	
	protected String getEnvironmentId(){
		return testCommon.getProperty("app.environmentId");
	}
	
	protected String getAppHostName(){
		return testEnvSpecific.getProperty("app.hostname");
	}
	
	protected String getAppPort(){
		return testEnvSpecific.getProperty("app.port");
	}
	
	protected String getAppPath(){
		return testCommon.getProperty("app.path");
	}
	
	protected String getURL(){
		return "http://" + getAppHostName() + ":" + getAppPort() + getAppPath() + "/";
	}
}