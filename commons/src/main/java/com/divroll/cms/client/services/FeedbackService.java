package com.divroll.cms.client.services;

import com.divroll.cms.client.model.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FeedbackService {

    @Inject
    UserProducer loggedInUser;

    public void sendMessage(User user, String title, String message, final AsyncCallback<String> callback) {
//        Browser.getWindow().getConsole().log("Sending message title: " + title + " message: " + message);
//        ParseObject messageObject = new ParseObject("Message");
//        messageObject.putString("title", title);
//        messageObject.putString("message", message);
//        messageObject.putString("email", user.getUsername());
//        Parse.Objects.create(messageObject, new AsyncCallback<ParseResponse>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                callback.onFailure(throwable);
//            }
//            @Override
//            public void onSuccess(ParseResponse parseResponse) {
//                String objectId = parseResponse.getObjectId();
//                callback.onSuccess(objectId);
//            }
//        });
    }

    public void sendFeedback(String email, String message, final AsyncCallback<String> callback) {
//        ParseObject messageObject = new ParseObject("Feedback");
//        messageObject.putString("email", email);
//        messageObject.putString("message", message);
//        Parse.Objects.create(messageObject, new AsyncCallback<ParseResponse>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                callback.onFailure(throwable);
//            }
//            @Override
//            public void onSuccess(ParseResponse parseResponse) {
//                String objectId = parseResponse.getObjectId();
//                callback.onSuccess(objectId);
//            }
//        });
    }
}
