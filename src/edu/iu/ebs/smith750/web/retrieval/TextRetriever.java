package edu.iu.ebs.smith750.web.retrieval;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class TextRetriever {
	public static void retrieveFullText(List<Page> pages, Consumer<PageContents> pageHandler) {
		AsyncHttpClient client = new AsyncHttpClient();

	    try {
			final CountDownLatch latch = new CountDownLatch(pages.size());
			pages.parallelStream().forEach((Page page) -> {
				client.prepareGet(page.getUrl()).execute(new AsyncCompletionHandler<Response>() {

					@Override
					public Response onCompleted(Response response) {
						ByteArrayOutputStream contents = new ByteArrayOutputStream();
						try {
							contents.write(response.getResponseBodyAsBytes());
							pageHandler.accept(new PageContents(page, contents.toString()));
						} catch (IOException ioe) {
							pageHandler.accept(new PageContents(page, ioe));
						} finally {
							latch.countDown();
							try {
								contents.close();
							} catch (IOException ioe) {
								contents = null;
							}
						}
						return response;
					}

					@Override
					public void onThrowable(Throwable t) {
						if (t instanceof Exception) {
							pageHandler.accept(new PageContents(page, (Exception)t));
							latch.countDown();
						}
					}
					
				});
			});
			latch.await();
		} catch (InterruptedException ie) {
		}
	}
}
