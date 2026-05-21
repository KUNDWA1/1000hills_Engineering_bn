package com.thousandhills.backend.dto;

import java.util.List;

public class QuotationRequestCreate {

    private String description;
    private List<OrderItemRequest> items;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
