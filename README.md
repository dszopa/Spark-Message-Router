# Spark-Message-Router
An improved way to handle your Spark socket messages

### Downloading
To use Spark-Message-Router, add it to your package manager with:

Gradle
```groovy
compile "io.dszopa.github:message-router:LATEST"
```

Maven
```xml
<dependency>
    <groupId>io.github.dszopa</groupId>
    <artifactId>message-router</artifactId>
    <version>LATEST</version>
</dependency>
````

### Usage

Inside of your websocket class, create a MessageRouter object. 
The objects parameter should be the package path where you plan 
on storing your routes

Then, in your @OnWebSocketMessage method, simply call `messageRouter.handle` with the Session & String parameters.
```java
@Websocket
public class SocketHandler {
    
    private MessageRouter messageRouter = new MessageRouter("io.dszopa.github");
    
    ...
    
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        messageRouter.handle(user, message);
    }
}
```

To configure routes, annotate a class with `@MessageController` and the method to be routed to with `@Route`
```java
@MessageController
public class SimpleMessageRouter {
    
    @Route("/simpleMessage")
    public void simpleMessageHandler(Session user, String message) {
        System.out.println("Message received, Method routed!");
    }
}
```

In order to make sure that the method is properly routed to, websocket messages should be sent as JSON and include a `route` field
```javascript
var webSocket = new Websocket('ws://localhost:4567/');

var message = JSON.stringify({
    route: "/simpleMessage"
});

webSocket.send(message);
```
With the code we have written, we will send a message with a route of `/simpleRoute` and have it mapped to the method `simpleMessageHandler` which will output its message.