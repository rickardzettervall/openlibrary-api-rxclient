<h2>Open Library API RxClient</h2>

The Open Library API RxClient is a Java library for easily connecting to https://openlibrary.org/developers/api/
and getting the received data as RxJava observables.

<a href="https://dev.odenapps.com/job/Rickard%20Zettervall/job/openlibrary-api-rxclient/job/master/" target="_blank"><img src="https://dev.odenapps.com/buildStatus/icon?job=Rickard+Zettervall%2Fopenlibrary-api-rxclient%2Fmaster"></a>
<a href="https://jitpack.io/#rickardzettervall/openlibrary-api-rxclient/" target="_blank"><img src="https://jitpack.io/v/rickardzettervall/openlibrary-api-rxclient.svg"></a>

<b><u>Supported APIs:</u></b>
<li>Books (<a href="https://openlibrary.org/dev/docs/api/books" target="_blank">https://openlibrary.org/dev/docs/api/books</a>)</li>
<li>Covers (<a href="https://openlibrary.org/dev/docs/api/covers" target="_blank">https://openlibrary.org/dev/docs/api/covers</a>)</li>
<li>Search (<a href="https://openlibrary.org/dev/docs/api/search" target="_blank">https://openlibrary.org/dev/docs/api/search</a>)</li>
<li>Subjects (<a href="https://openlibrary.org/dev/docs/api/subjects" target="_blank">https://openlibrary.org/dev/docs/api/subjects</a>)</li>

<h3>Setup (gradle)</h3>

Add the following to your root build.gradle file:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Add the dependency to your project build.gradle file:

    dependencies {
            ...
	        implementation 'com.github.rickardzettervall:openlibrary-api-rxclient:0.1-beta'
	}

More setup options can be found over at <a href="https://jitpack.io/#rickardzettervall/openlibrary-api-rxclient/">jitpack.io</a>.

<h3>Getting started</h3>

    // Get instance
    OpenLibraryClient client = OpenLibraryClient.getInstance();
    
    // Get observable from Respository
    Single<List<BookView>> observableSingle = client.getRepository().getBooks(BookView.class, "ISBN:0385472579", "LCCN:62019420");
    
    // Implement your own RxJava observer, e.g.
    // (please note that this example blocks the main thread)
    observableSingle.blockingSubscribe(
                new DisposableSingleObserver<List<BookView>>() {
                    @Override
                    public void onSuccess(@NonNull List<BookView> bookViews) {
                        // Do stuff
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // Do stuff
                    }
                });

<h3>License</h3>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this repository except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
