# twootoot [Android]
_Acheiving Twitter excellence 140 chars at a time._

## Breakdown
_[a frameworkless-ish twitter client in Android]_
### Info
This project is aimed at achieving a basic twitter integration complete with some basic user interaction listed below using no frameworks.
Why no frameworks? Well just to show some good'ol Java knowledge.
* No use of OAuth or official Twitter libs, no HTTP libs.. and no dependency injection.
* Initial goal is to duplicate this repo once it's final using _State of the Art_ libs available for Android - like RxJava and Squares awesome libs. If I can find the time.
* Feature a design fit for 2016, just to differ from the official Twitter client.

### Features & functionality
* MVC-ish separation (to the extent viable using vanilla Android)
* Login
* View feed
* Retweet
* Answer tweet
* Keep position in feed (list) on orientation change
* Portrait
  * Swipe retweet/answer
* Landscape
  * Buttons for retweet/answer

## Process
Planning in iterations where the result of each finished iteration is presentable to the (hypothetical) client.

* __Iteration 1__
  * MVC boilerplate
  * Login simple UX/UI
* __Iteration 2__
  * HTTP boilerplate
  * Login
    * Twitter integration
* __Iteration 3__
  * TBD
