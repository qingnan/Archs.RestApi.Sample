package archs.resteasy.api;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PreDestroy;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.embedded.EmbeddedJaxrsServer;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import com.google.inject.Injector;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;

import archs.resteasy.api.controller.*;

@SuppressWarnings("deprecation")
public class RestServer {

	int port = 8089;

	String rootPath = "";

	EmbeddedJaxrsServer<?> restServer;

	public void start(boolean useNetty) {

		ResteasyDeployment dp = new ResteasyDeploymentImpl();

		Injector injector = RestApplication.injector;
		Collection<Object> providers = new ArrayList<Object>();
		providers.add(injector.getInstance(JsonContextResolver.class));

		Collection<Object> controllers = new ArrayList<Object>();
		controllers.add(injector.getInstance(OrderController.class));

		dp.getProviders().addAll(providers);
		dp.getResources().addAll(controllers);

		if (useNetty) {
			NettyJaxrsServer netty = new NettyJaxrsServer();
			netty.setExecutorThreadCount(Runtime.getRuntime().availableProcessors() * 4);
			netty.setDeployment(dp);
			netty.setHostname("0.0.0.0");
			netty.setPort(port);
			netty.setRootResourcePath(rootPath);
			netty.setSecurityDomain(null);
			netty.start();
			restServer = netty;
		} else {
			restServer = new UndertowJaxrsServer();
			restServer.setDeployment(dp);
			restServer.setHostname("0.0.0.0");
			restServer.setPort(port);
			restServer.setRootResourcePath(rootPath);
			restServer.setSecurityDomain(null);
			restServer.deploy();
			restServer.start();
		}

		EurekaInstanceConfig instanceConfig = new SimpleEurekaInstanceConfig();
		DefaultEurekaClientConfig clientConfig = new DefaultEurekaClientConfig();
		DiscoveryManager.getInstance().initComponent(instanceConfig, clientConfig);
	}

	@PreDestroy
	public void cleanUp() {
		DiscoveryManager.getInstance().shutdownComponent();
		restServer.stop();
	}

	public String getRootResourcePath() {
		return rootPath;
	}

	public int getPort() {
		return port;
	}

}