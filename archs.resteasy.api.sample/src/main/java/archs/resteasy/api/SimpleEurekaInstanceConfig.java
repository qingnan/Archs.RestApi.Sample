package archs.resteasy.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.netflix.appinfo.MyDataCenterInstanceConfig;

import archs.resteasy.api.utils.Utils;


public class SimpleEurekaInstanceConfig extends MyDataCenterInstanceConfig {

	@Override
	public String getHostName(boolean refresh) {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			Utils.logError(e.toString());
		}
		return null;
	}

	@Override
	public String getInstanceId() {
		return getAppname() + "-" + getIpAddress() + ":" + getNonSecurePort();
	}

	@Override
	public String getStatusPageUrlPath() {
		return "/html/f5";
	}
}
