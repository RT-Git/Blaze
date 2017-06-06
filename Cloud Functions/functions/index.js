'use strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
exports.sendPush = functions.database.ref('/users/{uid}').onWrite(event => {
    var topic = "arc";
    var payload = {
        data: {
            title: "New Photo Uploaded"
            , body: "Tap to view"
        , }
    };
    // Set the message as high priority and have it expire after 24 hours.
    //    var options = {
    //        priority: "high"
    //        , timeToLive: 60 * 60 * 24
    //    };
    admin.messaging().sendToTopic(topic, payload).then(function (response) {
        console.log("Successfully sent message:", response);
    }).catch(function (error) {
        console.log("Error sending message:", error);
    });
});