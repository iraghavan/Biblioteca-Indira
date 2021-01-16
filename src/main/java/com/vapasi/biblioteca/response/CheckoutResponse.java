package com.vapasi.biblioteca.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Represents the response of the Checked out book")
public class CheckoutResponse {

    @ApiModelProperty(notes = "Checkout Message", example = "Thank you! Enjoy the book/movie" , required = true)
    private String checkoutMessage;

    public CheckoutResponse(String checkoutMessage) {
        this.checkoutMessage = checkoutMessage;
    }

    public String getCheckoutMessage() {
        return checkoutMessage;
    }

}
