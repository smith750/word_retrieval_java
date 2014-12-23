package edu.iu.ebs.smith750.web.retrieval;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;



public class TextRetriever {
	public static void retrieveFullText(List<Page> pages, Consumer<PageContents> pageHandler) {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
	    CloseableHttpAsyncClient client = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();

	    try {
			client.start();
			final CountDownLatch latch = new CountDownLatch(pages.size());
			pages.stream().forEach((Page page) -> {
				client.execute(page.buildGetRequest(), new FutureCallback<HttpResponse>() {
					@Override
					public void cancelled() {
						latch.countDown();
					}
					@Override
					public void completed(HttpResponse response) {
						ByteArrayOutputStream contents = new ByteArrayOutputStream();
						try {
							response.getEntity().writeTo(contents);
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
					}
					@Override
					public void failed(Exception ex) {
						pageHandler.accept(new PageContents(page, ex));
						latch.countDown();
					}
				});
			});
			latch.await();
		} catch (InterruptedException ie) {
		} finally {
			try {
				client.close();
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}
	}
}
