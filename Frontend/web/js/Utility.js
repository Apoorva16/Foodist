function getAPIURL() {
    // return "http://127.0.0.1:8080/FoodistAPI/api"; //Local
    return "http://foodistapi.mfhgpuhyiv.us-west-2.elasticbeanstalk.com/api"; //Live
}

function getAuthString() {
    return "Basic " + btoa(getUserId() + ":" + getAPIToken());
}

function getUserId() {
    return readCookie("foodistAccessId");
}

function getAPIToken() {
    return readCookie("foodistAccessToken");
}

function newCookie(name, value, days) {
    var days = 1;   // the number at the left reflects the number of days for the cookie to last
    // modify it according to your needs
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
    }
    else var expires = "";
    document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
    var nameSG = name + "=";
    var nuller = '';
    if (document.cookie.indexOf(nameSG) == -1)
        return nuller;

    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameSG) == 0) return c.substring(nameSG.length, c.length);
    }
    return null;
}

function eraseCookie(name) {
    newCookie(name, "", -1);
}