<h1>Open Library API RxClient</h1>

The Open Library API RxClient is a Java library for easily connecting to https://openlibrary.org/developers/api/
and getting the received data as RxJava observables. The client is optimized for Android but can also be used elsewhere.  

<img src="https://travis-ci.com/rickardzettervall/openlibrary-api-rxclient.svg?branch=master"> <a href="https://jitpack.io/#rickardzettervall/openlibrary-api-rxclient/"><img src="https://jitpack.io/v/rickardzettervall/openlibrary-api-rxclient.svg"></a>

<h3>Supported APIs</h3>
<li>Books (<a href="https://openlibrary.org/dev/docs/api/books">https://openlibrary.org/dev/docs/api/books</a>)</li>
<li>Covers (<a href="https://openlibrary.org/dev/docs/api/covers">https://openlibrary.org/dev/docs/api/covers</a>)</li>
<li>Read (<a href="https://openlibrary.org/dev/docs/api/read">https://openlibrary.org/dev/docs/api/read</a>)</li>
<li>RecentChanges (<a href="https://openlibrary.org/dev/docs/api/recentchanges">https://openlibrary.org/dev/docs/api/recentchanges</a>)</li>
<li>Search (<a href="https://openlibrary.org/dev/docs/api/search">https://openlibrary.org/dev/docs/api/search</a>)</li>
<li>Subjects (<a href="https://openlibrary.org/dev/docs/api/subjects">https://openlibrary.org/dev/docs/api/subjects</a>)</li>

<h2>Setup (gradle)</h2>

Add jitpack.io to your root build.gradle file:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Add the dependency to your project build.gradle file:

    dependencies {
            ...
	        implementation 'com.github.rickardzettervall:openlibrary-api-rxclient:0.2'
	}

More setup options can be found over at <a href="https://jitpack.io/#rickardzettervall/openlibrary-api-rxclient/">jitpack.io</a>.

<h2>Getting started</h2>

    // Get instance
    OpenLibraryClient client = OpenLibraryClient.getInstance();
    
    // Implement your own RxJava observer, e.g.
    DisposableSingleObserver<List<BookView>> disposable =
                client.getRepository().getBooks(BookView.class, "ISBN:0385472579", "LCCN:62019420")
                        .observeOn(Schedulers.from(ContextCompat.getMainExecutor(this)))
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableSingleObserver<List<BookView>>() {
                            @Override
                            public void onSuccess(@NonNull List<BookView> bookViews) {
                                // Do stuff
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                // Do stuff
                            }
                        });

<h3>Android</h3>

Add internet permission to your AndroidManifest.xml file:

    <uses-permission android:name="android.permission.INTERNET" />

<h2>License</h2>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this repository except in compliance with the License.
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
