package com.example.loan.controller;

import com.example.loan.dto.ResponseDTO;
import com.example.loan.dto.ResultObject;

public abstract class AbstractController { // 요청에 대한 응답 값을 통일화하기 위해서 설정

  protected <T> ResponseDTO<T> ok() {
    return ok(null, ResultObject.getSuccess());
  }

  protected <T> ResponseDTO<T> ok(T data) {
    return ok(data, ResultObject.getSuccess());
  }

  protected <T> ResponseDTO<T> ok(T data, ResultObject result) {
    ResponseDTO<T> obj = new ResponseDTO<>();
    obj.setResult(result);
    obj.setData(data);

    return obj;
  }
}
