package trainingxyz;

import models.Product;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void getCategories() {
        String endpoint = "http://localhost:8888/api_testing/category/read.php";
        given().when().get(endpoint).then();
    }

    @Test
    public void getProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
            given().
                queryParam("id", 2).
            when().
                get(endpoint).
            then().
                assertThat().
                    statusCode(200).
                    body("id", equalTo("2")).
                    body("name", equalTo("Cross-Back Training Tank")).
                    body("description", equalTo("The most awesome phone of 2013!")).
                    body("price", equalTo("299.00")).
                    body("category_id", equalTo("2")).
                    body("category_name", equalTo("Active Wear - Women"));
    }

    @Test
    public void getProducts() {
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        given().
        when().
            get(endpoint).
        then().
            log().
                body().
                assertThat().
                    statusCode(200).
                    header("Content-Type", equalTo("application/json")).
                    body("records.size()", greaterThan(0)).
                    body("records.id", everyItem(notNullValue())).
                    body("records.name", everyItem(notNullValue())).
                    body("records.description", everyItem(notNullValue())).
                    body("records.price", everyItem(notNullValue())).
                    body("records.category_id", everyItem(notNullValue())).
                    body("records.category_name", everyItem(notNullValue())).
                    body("records.id[0]", equalTo("25"));


        // body is an object with key "records" and value is the array of objects (example below)
        /**
         *
         * {
         *     "records": [
         *          {
         *              "id": "25",
         *              "name": "Water Bottle",
         *              "description": "Blue water bottle...",
         *              "price": "12.00",
         *              "category_id": "3",
         *              "category_name": "Active Wear - Unisex"
         *          },
         *          {
         *              "id": "26",
         *              "name": "Water Bottle",
         *              "description": "White water bottle...",
         *              "price": "15.00",
         *              "category_id": "4",
         *              "category_name": "Active Wear - Women"
         *          }
         *
         *     ]
         * }
         *
         */
    }

    @Test
    public void createSerializedProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Product product = new Product(
                "Water Bottle",
                "Blue water bottle. Holds 64 ounces",
                12,
                3
        );

            given().body(product).when().post(endpoint).then().log().body();
    }

    @Test
    public void getDeserializedProduct() {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        Product expectedProduct = new Product(
                25,
                "Water Bottle",
                "Blue water bottle...",
                12.00,
                3,
                "Active Wear - Unisex"
        );

        Product actualProduct =
                given().
                    queryParam("id", "2").
                when().
                    get(endpoint).
                        as(Product.class);

        // In this case we use the assertThat as standalone method; we are not chaining it.
        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }
}
