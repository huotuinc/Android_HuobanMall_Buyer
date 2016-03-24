package com.huotu.android.library.buyer.bean.Data;

import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ClassificationConfig {
    private links _links;

    public links get_links() {
        return _links;
    }

    public void set_links(links _links) {
        this._links = _links;
    }

    public class links{
        private HrefConfig classification;
        private HrefConfig namedPage;
        private HrefConfig self;
        private HrefConfig widgets;

        public HrefConfig getClassification() {
            return classification;
        }

        public void setClassification(HrefConfig classification) {
            this.classification = classification;
        }

        public HrefConfig getNamedPage() {
            return namedPage;
        }

        public void setNamedPage(HrefConfig namedPage) {
            this.namedPage = namedPage;
        }

        public HrefConfig getSelf() {
            return self;
        }

        public void setSelf(HrefConfig self) {
            this.self = self;
        }

        public HrefConfig getWidgets() {
            return widgets;
        }

        public void setWidgets(HrefConfig widgets) {
            this.widgets = widgets;
        }
    }

    public class HrefConfig {
        private String href;

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
