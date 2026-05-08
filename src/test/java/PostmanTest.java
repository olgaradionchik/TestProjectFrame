import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class PostmanTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://postman-echo.com";
    }
    @Test
    @DisplayName("GET Request")
    void testGetRequest() {
        given()
                .queryParam("foo1", "bar1")
                .queryParam("foo2", "bar2")
                .when()
                .get("/get")
                .then()
                .statusCode(HttpStatus.SC_OK) // Проверка статус-кода
                // Сравниваем значения всех полей в объекте args (аргументы запроса)
                .body("args.foo1", equalTo("bar1"))
                .body("args.foo2", equalTo("bar2"))
                // Проверяем, что в ответе нет лишних аргументов
                .body("args.size()", is(2))
                .log().all();
    }
    @Test
    @DisplayName("POST Raw Text")
    void testPostRawText() {
        String myText = "Testing Postman Echo";
        given()
                .contentType(ContentType.TEXT)
                .body(myText)
                .when()
                .post("/post")
                .then()
                .statusCode(HttpStatus.SC_OK)
                //Возвращает отправленный текст в поле data
                .body("data", equalTo(myText))
                .log().all();
    }
    @Test
    @DisplayName("POST Form Data")
    void testPostFormData() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "admin")
                .formParam("secret", "12345")
                .when()
                .post("/post")
                .then()
                .statusCode(HttpStatus.SC_OK)
                // Проверяем все значения в объекте form
                .body("form.username", equalTo("admin"))
                .body("form.secret", equalTo("12345"))
                .log().all();
    }
    @Test
    @DisplayName("PUT Request")
    void testPutRequest() {
        String jsonBody = "{\"status\": \"updated\"}";
        given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put("/put")
                .then()
                .statusCode(HttpStatus.SC_OK)
                // Проверяем через 'json.', так как Echo оборачивает ответ
                .body("json.status", equalTo("updated"))
                .log().all();
    }
    @Test
    @DisplayName("PATCH Request")
    void testPatchRequest() {
        given()
                .body("patching data")
                .when()
                .patch("/patch")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data", equalTo("patching data"))
                .log().all();
    }
    @Test
    @DisplayName("DELETE Request")
    void testDeleteRequest() {
        given()
                .when()
                .delete("/delete")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all();
    }
}
