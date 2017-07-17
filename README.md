# Spark-Message-Router
An improved way to handle your Spark socket messages

## Downloading
To use Spark-Message-Router, add it to your package manager with:

Gradle
```groovy
compile "io.github.dszopa:message-router:LATEST"
```

Maven
```xml
<dependency>
    <groupId>io.github.dszopa</groupId>
    <artifactId>message-router</artifactId>
    <version>LATEST</version>
</dependency>
````

## Usage

Inside of your websocket class, create a `MessageRouter` object. 
The objects parameter should be the package path where you plan 
on storing your routes

Then, in your `@OnWebSocketMessage` method, simply call `messageRouter.handle` with the Session & String parameters.
```java
@Websocket
public class SocketHandler {
    
    private MessageRouter messageRouter = new MessageRouter("io.github.dszopa");
    
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

## License
MIT License

Copyright (c) 2017 Daniel Szopa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
