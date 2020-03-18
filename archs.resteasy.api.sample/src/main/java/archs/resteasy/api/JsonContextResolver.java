package archs.resteasy.api;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import archs.resteasy.api.utils.Utils;

@Provider
public class JsonContextResolver implements ContextResolver<ObjectMapper> {

	final ObjectMapper mapper = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));

	public ObjectMapper getContext(Class<?> type) {
		return Utils.mapper;
	}
}