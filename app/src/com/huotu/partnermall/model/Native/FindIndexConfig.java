package com.huotu.partnermall.model.Native;

/**
 * Created by Administrator on 2016/3/8.
 */
public class FindIndexConfig {
    private String title;
    private String description;
    private boolean enabled;
    private boolean indexed;
    private int merchantId;
    private LinkConfig _links;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public LinkConfig get_links() {
        return _links;
    }

    public void set_links(LinkConfig _links) {
        this._links = _links;
    }
}
