# Sunny Reader for Hacker News [![Build Status](https://travis-ci.org/setiawanp/sunny-reader.svg?branch=master)](https://travis-ci.org/setiawanp/sunny-reader) [![Coverage Status](https://coveralls.io/repos/github/setiawanp/sunny-reader/badge.svg?branch=master)](https://coveralls.io/github/setiawanp/sunny-reader?branch=master)
[Hacker News](https://news.ycombinator.com/) Reader Client for Android. All news are retrieved using [HackerNews API](https://github.com/HackerNews/API) and all images which used in the application are taken from [Google's Material Design Icons](https://design.google.com/icons/).

The application can be installed from Android 2.3 (Gingerbread - API 9) to Android 6.0 (Marshmallow - API 23). Currently, the features are:
- List Top Stories
- Show comments and replies for each News/Job/Ask/Poll in a tree alike structure
- Expand and collapse replies
- Open news article in external browser
- Support offline reading. If you have retrieved top stories or seen comment in a story previously, it will be cached locally to support offline reading. **Note that:** cache will only be used if you don't have internet connection.

Third-party libraries used:
- [Android Support Libraries](http://developer.android.com/tools/support-library/index.html)
- [Dagger 2](http://google.github.io/dagger/) - Dependency Injector
- [Retrofit](http://square.github.io/retrofit/) - REST Client
- [RxJava](https://github.com/ReactiveX/RxJava) - Reactive Functional Programming Framework
- [RxAndroid](https://github.com/ReactiveX/RxAndroid) - RxJava Bindings for Android
- [Gson](https://github.com/google/gson) - JSON Serialization Library
- [ButterKnife](http://jakewharton.github.io/butterknife/) - Android View Injector
- [DbFlow](https://github.com/Raizlabs/DBFlow) - Sqlite ORM

Third-party libraries used specific for unit tests and instrumentation tests:
- [JUnit](http://junit.org/) - Java Unit Test Framework
- [Hamcrest](http://hamcrest.org/JavaHamcrest/) - Matcher Library for Unit Test
- [Mockito](http://mockito.org/) - Mocking Framework
- [Robolectric](http://robolectric.org/) - Android Unit Test Framework
- [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/) - Instrumentation Test Framework

To build the app in Debug mode, please run this in the command line of the application directory:
```
./gradlew assembleDebug
```
**Note**: Currently, the application had been tested to run only in Debug mode. Issues may appear if it was run in Release mode, since `proguard-rules` may strip and obfuscate some classes and methods.


To execute unit tests and instrumentation test including its code coverage report generation, please run:
```
./gradlew jacocoTestReport
```
Code coverage report could be found in `{appDirectory}/app/build/reports/jacoco/jacocoTestReport/html/index.html`
**Note**: Both unit tests and instrumentation tests will not make any actual network requests.
