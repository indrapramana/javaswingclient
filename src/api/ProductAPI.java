package api;

import java.util.List;
import entities.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductAPI {

	@GET("product/findall")
	Call<List<Product>> findAll();
	
	@GET("product/find/{id}")
	Call<Product> find(@Path("id") String id);
	
	@POST("product/create")
	Call<Void> create(@Body Product product);
	
	@PUT("product/update")
	Call<Void> update(@Body Product product);
	
	@DELETE("product/delete/{id}")
	Call<Void> delete(@Path("id") String id);
	
}
