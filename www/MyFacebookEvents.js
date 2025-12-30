var exec = require('cordova/exec');

// SERVICE = Java class name (MyFacebookEvents)
// ACTION = method name handled in Java execute()
// ARGS    = data sent to Java

var MyFacebookEvents = {

  // 🔹 NEW: Initialize Facebook SDK dynamically
  init: function(appId, clientToken, success, error) {
    exec(success, error,
      "MyFacebookEvents",
      "init",
      [appId, clientToken]
    );
  },

  logEvent: function(name, params, success, error) {
    exec(success, error,
      "MyFacebookEvents",
      "logEvent",
      [name, params || {}]
    );
  },

  logPurchase: function(amount, currency, success, error) {
    exec(success, error,
      "MyFacebookEvents",
      "logPurchase",
      [amount, currency]
    );
  },

  setUserId: function(userId, success, error) {
    exec(success, error,
      "MyFacebookEvents",
      "setUserId",
      [userId]
    );
  }

};

module.exports = MyFacebookEvents;
