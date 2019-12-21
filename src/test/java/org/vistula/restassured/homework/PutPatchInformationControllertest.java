package org.vistula.restassured.homework;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.vistula.restassured.RestAssuredTest;
import org.vistula.restassured.pet.Information;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class PutPatchInformationControllertest extends RestAssuredTest {

    @Test
    public void shouldPutNewInformation() {
        String myName = RandomStringUtils.randomAlphabetic(10);
        Information player = createRandomPlayer(myName);

        JSONObject requestParams = new JSONObject();
        String randomLetter = RandomStringUtils.randomAlphabetic(1);
        requestParams.put("name", myName + randomLetter);
        String oldNationality = player.getNationality();
        requestParams.put("nationality", oldNationality);
        int raise = player.getSalary() * 2;
        requestParams.put("salary", raise);

        Information updatedPlayer = given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .put("/information/" + player.getId())
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo(myName + randomLetter))
                .body("nationality", equalTo(oldNationality))
                .body("salary", equalTo(raise))
                .extract().as(Information.class);

        assertThat(updatedPlayer.getName()).isEqualTo(myName + randomLetter);
        assertThat(updatedPlayer.getNationality()).isEqualTo(oldNationality);
        assertThat(updatedPlayer.getSalary()).isEqualTo(raise);
        assertThat(updatedPlayer.getId()).isEqualTo(player.getId());

        deletePlayer(player.getId());
    }

    @Test
    public void shouldPatchPlayersNationality() {
        String myName = RandomStringUtils.randomAlphabetic(10);
        Information player = createRandomPlayer(myName);

        String newNationality = "Russia";
        JSONObject requestParams = new JSONObject();
        requestParams.put("nationality", newNationality);

        Information updatedPlayer = given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .patch("/information/" + player.getId())
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo(myName))
                .body("nationality", equalTo(newNationality))
                .body("salary", equalTo(player.getSalary()))
                .extract().as(Information.class);

        assertThat(updatedPlayer.getName()).isEqualTo(myName );
        assertThat(updatedPlayer.getNationality()).isEqualTo(newNationality);
        assertThat(updatedPlayer.getSalary()).isEqualTo(player.getSalary());
        assertThat(updatedPlayer.getId()).isEqualTo(player.getId());

        deletePlayer(player.getId());
    }

    @Test
    public void shouldPatchPlayersSalary() {
        String myName = RandomStringUtils.randomAlphabetic(10);
        Information player = createRandomPlayer(myName);

        int newSalary = 9876;
        JSONObject requestParams = new JSONObject();
        requestParams.put("salary", newSalary);

        Information updatedPlayer = given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .patch("/information/" + player.getId())
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo(myName))
                .body("nationality", equalTo(player.getNationality()))
                .body("salary", equalTo(newSalary))
                .extract().as(Information.class);

        assertThat(updatedPlayer.getName()).isEqualTo(myName );
        assertThat(updatedPlayer.getNationality()).isEqualTo(player.getNationality());
        assertThat(updatedPlayer.getSalary()).isEqualTo(newSalary);
        assertThat(updatedPlayer.getId()).isEqualTo(player.getId());

        deletePlayer(player.getId());
    }

    private void deletePlayer(long value) {
        given().delete("/information/"+value)
                .then()
                .log().all()
                .statusCode(204);
    }

    private Information createRandomPlayer(String myName) {
        JSONObject requestParams = new JSONObject();

        requestParams.put("name", myName);

        String myNationality = "Poland";
        requestParams.put("nationality", myNationality);

        int mySalary = 9000;
        requestParams.put("salary", mySalary);

        return given().header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/information")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", equalTo(myName))
                .body("nationality", equalTo(myNationality))
                .body("salary", equalTo(mySalary))
                .extract().as(Information.class);
    }

}
