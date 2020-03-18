package archs.resteasy.api.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	public final static String Encoding = "UTF-8";
	public final static Logger logger;
	public final static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		logger = LoggerFactory.getLogger("Category");
	}

	public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass,
			Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static String serializeJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logError(e.toString());
			return null;
		}
	}

	public static final <T> T deserializeJson(String jsonString, Class<T> clazz) {
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (JsonMappingException e1) {
			logError(e1.toString());
		} catch (JsonProcessingException e2) {
			logError(e2.toString());
		}
		return null;
	}

	public static final <T> List<T> deserializeList(String jsonString, Class<T> clazz) {
		try {
			return mapper.readValue(jsonString, getCollectionType(mapper, List.class, clazz));
		} catch (JsonMappingException e1) {
			logError(e1.toString());
		} catch (JsonProcessingException e2) {
			logError(e2.toString());
		}
		return null;
	}

	/**
	 * 获取本机IP
	 */
	public static String getAllLocalHostIP() {
		List<String> res = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				Enumeration<InetAddress> nii = ni.getInetAddresses();
				while (nii.hasMoreElements()) {
					ip = (InetAddress) nii.nextElement();
					if (ip != null && ip.getHostAddress().indexOf(":") == -1
							&& !ip.getHostAddress().equals("127.0.0.1")) {
						res.add(ip.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		String ipString = "";
		int len = res.size() - 1;
		for (int i = 0; i < len; i++) {
			ipString += res.get(i) + ",";
		}
		ipString += res.get(len);
		return ipString;
	}

	public static String UrlEncode(String s) {
		try {
			return URLEncoder.encode(s, Encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	public static String UrlDecode(String s) {
		try {
			return URLDecoder.decode(s, Encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	public static String DateFormat(Date date, String formatString) {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(date);
	}

	public static String DateFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static String DateTimeFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * Transform the specified byte into a Hex String form.
	 */
	public static final String bytesToHexStr(byte[] bcd) {
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++) {
			s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
			s.append(bcdLookup[bcd[i] & 0x0f]);
		}

		return s.toString();
	}

	/**
	 * Transform the specified Hex String into a byte array.
	 */
	public static final byte[] hexStrToBytes(String s) {
		byte[] bytes;

		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}

	private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static void logInfo(String message) {
		logger.info(message);
	}

	public static void logDebug(String message) {
		logger.debug(message);
	}

	public static void logWarn(String message) {
		logger.warn(message);
	}

	public static void logError(String message) {
		logger.error(message);
	}

	/*
	 * 动态创建代理
	 */
	public static <T> T getProxy(Class<?> interfaceType, final Object target) throws Exception {
		if (!interfaceType.isInterface()) {
			throw new Exception("接口类型不正确！");
		}
		@SuppressWarnings("unchecked")
		T targerInstance = (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("开始" + method.getName());
						Object value = method.invoke(target, args);
						System.out.println("结束" + method.getName());
						return value;
					}
				});
		return targerInstance;
	}
}