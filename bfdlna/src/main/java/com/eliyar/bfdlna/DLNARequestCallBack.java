package com.eliyar.bfdlna;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by brikerman on 2017/5/14.
 */

public interface DLNARequestCallBack {
    /**
     * 请求失败回调
     */
    void onFailure();

    /**
     * 请求成功回调
     * @param info 附带信息字典
     */
    void onSuccess(HashMap<String,String> info);
}