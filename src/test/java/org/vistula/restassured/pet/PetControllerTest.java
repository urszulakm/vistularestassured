package org.vistula.restassured.pet;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class PetControllerTest extends RestAssuredTest {

    @Test
    public void shouldGetAll() {
        given().get("/pet")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    public void shouldGetFirstPet() {
        given().get("/pet/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", equalTo("Cow"));
    }

    @Test
    public void shouldGetSecondPet() {
       Object name = given().get("/pet/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(2))
                .body("name", equalTo("Dog"))
                .extract().path("name");

        assertThat(name).isEqualTo("Dog");
    }

    
     @Test
     public void shouldGetSecondPetWithNameId() {
        Pet pet = given().get("/pet/2")
                 .then()
                 .log().all()
                 .statusCode(200)
                 .body("id", is(2))
                 .body("name", equalTo("Dog"))
                 .extract().body().as(Pet.class);

        assertThat(pet.getId()).isEqualTo(2);
        assertThat(pet.getName()).isEqualTo("Dog");

          //test zeby sprawdzic wszystko name and id -




//zaznaczylismy wszystko -> option+cmd+v i wycagnelismy inta ze statusCode
    }

//    @Test
//    public void shouldGetNotExisitngfPet() {
//        String contentType = given().get("/pet/10")
//                .then()
//                .log().all()
//                .statusCode(404)
//                .body(equalTo("There is no Pet with such id"))
//                .extract().statusCode(); /*wyciągamy content type*/
//
//        assertThat(contentType).isEqualTo("text/plain;charset=UTF-8");
//    }

        @Test
        public void shouldCreateNewPet() {
        JSONObject requestParams = new JSONObject();       /*zeby stworzyc jsona musmi stworzyc obiekt klasy json object
        zeby z niego skorzystac w pomie nalezy dodac dependencje -> org.json.json*/
            int value = 4;
            requestParams.put("id", value);              /*korzystamy z metody put, podajemy wartosc obiektu i klucza*/
            requestParams.put("name", "Wolf");           /*wpuszczamy tu obiekt, ze zwyklym stringieim*/

        given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/pet")
                .then()
                .log().all()
                .statusCode(201);

        given().delete("/pet/"+value)
                    .then()
                    .log().all()
                    .statusCode(204);

    }


    @Test
    public void shouldCreateNewPetB() {
        JSONObject requestParams = new JSONObject();
        int value = ThreadLocalRandom.current().nextInt(20, Integer.MAX_VALUE);
        requestParams.put("id", value);
        requestParams.put("name", RandomStringUtils.randomAlphabetic(10));           /*dodajemy randomowa nazwe*/

        given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/pet")
                .then()
                .log().all()
                .statusCode(201);

        given().delete("/pet/"+value)
                .then()
                .log().all()
                .statusCode(204);

    }



    @Test
    public void shouldDeleteFourthPet() {
        given().delete("/pet/4")
                .then()
                .log().all()
                .statusCode(204);
    }



    }

    


