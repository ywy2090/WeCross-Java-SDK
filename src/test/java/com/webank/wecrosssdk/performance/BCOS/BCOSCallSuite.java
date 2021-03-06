package com.webank.wecrosssdk.performance.BCOS;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;

public class BCOSCallSuite implements PerformanceSuite {
    private Resource resource;
    private String data = "aa";

    public BCOSCallSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        try {
            String[] ret = resource.sendTransaction("set", data);
        } catch (WeCrossSDKException e) {
            throw new WeCrossSDKException(
                    ErrorCode.INVALID_CONTRACT, "Invalid contract or user: " + e.getMessage());
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "BCOS Call Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback) {
        try {
            String[] ret = resource.call("get");
            if (ret[0].equals(data)) {
                callback.onSuccess(ret[0]);
            } else {
                callback.onFailed(ret[0]);
            }

        } catch (WeCrossSDKException e) {
            callback.onFailed(e.getMessage());
        }
    }
}
