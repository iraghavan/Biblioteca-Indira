package com.vapasi.biblioteca.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Represents a response to check if book is returned")
public class ReturnResponse {

    @ApiModelProperty(notes = "Return Message", example = "Thank you for returning the book." , required = true)
    private String returnMessage;

    public ReturnResponse(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getReturnMessage() {
        return returnMessage;
    }
}




