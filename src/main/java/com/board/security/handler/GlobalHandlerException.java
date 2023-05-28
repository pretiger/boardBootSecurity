package com.board.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.board.security.domain.ResponseDTO;

@ControllerAdvice
@RestController
public class GlobalHandlerException {

	@ExceptionHandler(value=Exception.class)
	public ResponseDTO<String> handleArgumentException(Exception e) {
		return new ResponseDTO<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}
}
