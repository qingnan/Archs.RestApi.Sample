package archs.resteasy.api;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class RestApplication {

	public static Injector injector;
	
	public static void main(String[] args) {
		
		//start service
		injector = Guice.createInjector(new RestModule());
		RestServer rest = injector.getInstance(RestServer.class);
		rest.start(false);
	}
	
}
