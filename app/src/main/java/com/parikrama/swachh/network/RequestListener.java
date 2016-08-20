package com.parikrama.swachh.network;

/**
 * @author Anurag
 */
public interface RequestListener<RESULT> {
    void onRequestFailure(Exception e);
    void onRequestSuccess(RESULT jsonObject);
}

