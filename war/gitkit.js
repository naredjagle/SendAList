/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/10/12
 * Time: 11:33 PM
 * To change this template use File | Settings | File Templates.
 */
google.load("identitytoolkit", "1", {packages: ["ac"], language:"en"});

$(function() {
        window.google.identitytoolkit.setConfig({
            developerKey: "AIzaSyCk5yd7Qt3vQGAuXaUpeLdD2yKgx5enmis",
            companyName: "The Nagle Code",
            callbackUrl: "http://thenaglecode.broke-it.net:8080/callback",
            realm: "",
            userStatusUrl: "/status",
            loginUrl: "/login",
            signupUrl: "/signup.jsp",
            homeUrl: "/secured/SendAList.jsp",
            logoutUrl: "/logout",
            idps: ["Gmail", "GoogleApps", "Yahoo", "Hotmail"],
            tryFederatedFirst: true,
            useCachedUserStatus: false,
            useContextParam: true
        });
        $("#navbar").accountChooser();
      });