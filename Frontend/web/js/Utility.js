/**
 * Created by csuser1234 on 4/25/16.
 */

function getAPIURL() {
    return "http://127.0.0.1:8080/FoodistAPI/api";
}

function getAuthString() {
    return "Basic " + btoa(getUserId() + ":" + getAPIToken());
}

function getUserId() {
    return "1";
}

function getAPIToken() {
    return "helloworld";
}