package archs.resteasy.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.GZIP;

import archs.resteasy.api.MimeType;
import archs.resteasy.api.dto.Order;

@Path("/order")
public class OrderController {

	@GET
	@Path("/id/{id}")
	@Produces({ MimeType.Json })
	public Order getOrderInfo(@PathParam("id") String id) {
		Order order = new Order();
		order.setId(id);
		order.setPrice(12.65F);
		order.setProduct("asdfasdf");
		return order;
	}

	@GZIP
	@GET
	@Path("/all")
	@Produces({ MimeType.Json })
	public List<Order> getOrderList() {
		Order order = new Order();
		order.setId("11111");
		order.setPrice(12.65F);
		order.setProduct("product-XXX");
		List<Order> list = new ArrayList<Order>();
		for (int i = 0; i < 9; i++) {
			list.add(order);
		}
		return list;
	}
}
