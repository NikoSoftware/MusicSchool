/*
 * Copyright (c) 2013. 1010.am
 *
 * You may obtain a copy of the License at
 *
 *      http://1010.am
 */
package cn.sanfast.xmutils.network;

/**
 * @author koudejian
 */
public interface HttpTaskFinishListener {

    void finish(String obj);

    void error(String obj);

}
