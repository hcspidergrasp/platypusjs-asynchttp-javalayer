/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcspider.platypusjs.asynchttp;

import org.apache.http.concurrent.FutureCallback;
import org.apache.http.HttpResponse;
import java.util.function.BiConsumer;
import jdk.nashorn.api.scripting.JSObject;

/**
 *
 * @author spidergrasp
 */
public class AsyncHTTPCallbacks {
    
    public static BiConsumer<Object, Throwable> asConsumer(JSObject aOnSuccess, JSObject aOnFailure) {
        return (Object aResult, Throwable aReason) -> {
            if (aReason != null) {
                aOnFailure.call(null, aReason);
            } else {
                aOnSuccess.call(null, aResult);
            }
        };
    }
    
    public static FutureCallback<HttpResponse> asCallback(BiConsumer<Object, Throwable> aWrapped) {
        return new FutureCallback<HttpResponse>(){
            @Override
            public void completed(final HttpResponse response) {
                aWrapped.accept(response,null);
            }
            @Override
            public void failed(final Exception ex) {
                aWrapped.accept(null,ex);
            }
            @Override
            public void cancelled() {
                aWrapped.accept(null,new Exception("Async HTTP requst was cancelled."));
            }
        };
    }
    
}
