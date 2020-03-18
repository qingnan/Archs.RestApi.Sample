package archs.resteasy.api;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import archs.resteasy.api.controller.OrderController;

public class RestModule implements Module {

	public void configure(Binder binder) {
		binder.bind(OrderController.class).in(Scopes.SINGLETON);
	}

}